from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import StreamingResponse, FileResponse
import os
import shutil
from typing import Optional
import tempfile
from dotenv import load_dotenv
import logging
import io
import hashlib

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Load environment variables from .env file
load_dotenv()

# Initialize OpenAI client if API key is available
openai_api_key = os.getenv("OPENAI_API_KEY")
client = None
if openai_api_key:
        from openai import OpenAI
        client = OpenAI(api_key=openai_api_key)
        logger.info(f"OpenAI client initialized with API key: {openai_api_key[:4]}...")
else:
    logger.warning("No OpenAI API key found. Transcription will return placeholder text.")

app = FastAPI(title="Voice Transcription API")

# Add CORS middleware to allow requests from the frontend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173"],  # Frontend URL
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Create a temporary directory to store uploaded files
TEMP_DIR = tempfile.mkdtemp()
logger.info(f"Temporary directory created at: {TEMP_DIR}")

@app.get("/api/health")
async def health_check():
    """
    Endpoint to check if the server is healthy.
    """
    return {"status": "healthy", "message": "Server is running"}

@app.post("/api/transcribe")
async def transcribe_audio(audio: UploadFile = File(...)):
    """
    Endpoint to receive audio file and return transcription.
    
    This endpoint receives an audio file from the frontend,
    saves it temporarily, and processes it for transcription using OpenAI's Whisper model.
    """
    if not audio:
        raise HTTPException(status_code=400, detail="No audio file provided")
    
    # Log information about the received file
    logger.info(f"Received file: {audio.filename}, Content-Type: {audio.content_type}")
    
    # Check if the file is an audio file
    if not audio.content_type.startswith("audio/"):
        raise HTTPException(status_code=400, detail="File is not an audio file")
    
    # Save the file temporarily with original extension if possible
    file_extension = os.path.splitext(audio.filename)[1] if audio.filename else ".mp3"
    temp_file_path = os.path.join(TEMP_DIR, f"recording{file_extension}")
    
    logger.info(f"Saving file to: {temp_file_path}")
    
    try:
        with open(temp_file_path, "wb") as buffer:
            shutil.copyfileobj(audio.file, buffer)
        
        logger.info(f"File saved successfully. Size: {os.path.getsize(temp_file_path)} bytes")
    except Exception as e:
        logger.error(f"Error saving file: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Error saving file: {str(e)}")
    finally:
        audio.file.close()
    
    # Check if OpenAI client is initialized
    if not client:
        logger.warning("OpenAI client not initialized. Returning placeholder transcription.")
        return {
            "status": "warning",
            "message": "OpenAI API key not set. Using placeholder transcription.",
            "file_size": os.path.getsize(temp_file_path),
            "transcription": "This is a placeholder transcription. To get actual transcription, please set OPENAI_API_KEY in .env file."
        }
    
    try:
        # Transcribe the audio using OpenAI's Whisper model
        logger.info("Starting transcription with Whisper model")
        
        with open(temp_file_path, "rb") as audio_file:
            transcription = client.audio.transcriptions.create(
                model="whisper-1",
                file=audio_file,
                response_format="text"
            )
        
        logger.info("Transcription completed successfully")
        
        return {
            "status": "success",
            "message": "Audio transcribed successfully",
            "file_size": os.path.getsize(temp_file_path),
            "transcription": transcription
        }
    except Exception as e:
        error_message = str(e)
        logger.error(f"Transcription error: {error_message}")
        
        return {
            "status": "error",
            "message": f"Error transcribing audio: {error_message}",
            "transcription": "An error occurred during transcription."
        }

@app.post("/api/text-to-speech")
async def text_to_speech(text: str, voice: str = "nova"):
    """
    Convert text to speech using OpenAI's TTS API.
    
    Args:
        text: The text to convert to speech
        voice: The voice to use (default: nova)
        
    Returns:
        An MP3 audio file of the synthesized speech
    """
    if not text:
        raise HTTPException(status_code=400, detail="No text provided")
    
    # Check if OpenAI client is initialized
    if not client:
        raise HTTPException(
            status_code=500, 
            detail="OpenAI API key not set. Please set OPENAI_API_KEY in .env file."
        )
    
    try:
        logger.info(f"Converting text to speech using voice: {voice}")
        
        # Create a unique filename for the speech file
        hash_object = hashlib.md5(text.encode())
        file_hash = hash_object.hexdigest()[:8]
        speech_file_path = os.path.join(TEMP_DIR, f"speech_{file_hash}.mp3")
        
        # Generate speech using OpenAI's TTS API
        response = client.audio.speech.create(
            model="gpt-4o-mini-tts",
            voice=voice,
            input=text,
            response_format="mp3"
        )
        
        # Save the audio file
        with open(speech_file_path, "wb") as f:
            response.stream_to_file(speech_file_path)
        
        logger.info(f"Speech generated successfully and saved to {speech_file_path}")
        
        # Return the audio file for download
        return FileResponse(
            path=speech_file_path,
            media_type="audio/mpeg",
            filename="generated_speech.mp3"
        )
        
    except Exception as e:
        error_message = str(e)
        logger.error(f"Text-to-speech error: {error_message}")
        raise HTTPException(status_code=500, detail=f"Error generating speech: {error_message}")

@app.post("/api/transcribe-and-speak")
async def transcribe_and_speak(audio: UploadFile = File(...), voice: str = "nova"):
    """
    Endpoint to transcribe audio and then convert the transcription to speech.
    
    This endpoint combines the transcribe and text-to-speech functionalities.
    """
    # First transcribe the audio
    transcription_result = await transcribe_audio(audio)
    
    # Check if transcription was successful
    if transcription_result.get("status") != "success":
        return transcription_result
    
    # Get the transcribed text
    transcribed_text = transcription_result.get("transcription")
    
    try:
        logger.info(f"Converting transcription to speech using voice: {voice}")
        
        # Create a unique filename for the speech file
        hash_object = hashlib.md5(transcribed_text.encode())
        file_hash = hash_object.hexdigest()[:8]
        speech_file_path = os.path.join(TEMP_DIR, f"speech_{file_hash}.mp3")
        
        # Generate speech using OpenAI's TTS API
        response = client.audio.speech.create(
            model="gpt-4o-mini-tts",
            voice=voice,
            input=transcribed_text,
            response_format="mp3"
        )
        
        # Save the audio file
        with open(speech_file_path, "wb") as f:
            response.stream_to_file(speech_file_path)
        
        logger.info(f"Speech generated successfully and saved to {speech_file_path}")
        
        # Return both the transcription and the path to the audio file
        return {
            "status": "success",
            "message": "Audio transcribed and converted to speech successfully",
            "transcription": transcribed_text,
            "speech_file": speech_file_path,
            "download_url": f"/api/download-speech?file_path={os.path.basename(speech_file_path)}"
        }
        
    except Exception as e:
        error_message = str(e)
        logger.error(f"Text-to-speech error: {error_message}")
        return {
            "status": "error",
            "message": f"Error generating speech: {error_message}",
            "transcription": transcribed_text
        }

@app.get("/api/download-speech")
async def download_speech(file_path: str):
    """
    Endpoint to download a generated speech file.
    
    Args:
        file_path: The filename of the speech file to download
    """
    full_path = os.path.join(TEMP_DIR, file_path)
    
    if not os.path.exists(full_path):
        raise HTTPException(status_code=404, detail="Speech file not found")
    
    return FileResponse(
        path=full_path,
        media_type="audio/mpeg",
        filename="generated_speech.mp3"
    )

@app.on_event("shutdown")
def cleanup():
    """Clean up temporary files when the application shuts down."""
    logger.info(f"Cleaning up temporary directory: {TEMP_DIR}")
    shutil.rmtree(TEMP_DIR, ignore_errors=True)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
