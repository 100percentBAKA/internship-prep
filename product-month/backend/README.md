# Voice Transcription Backend

This is a FastAPI backend for the Voice Recorder application. It provides endpoints for health checking and audio transcription.

## Setup

1. Create a virtual environment (recommended):
```
python -m venv venv
```

2. Activate the virtual environment:
- Windows:
```
venv\Scripts\activate
```
- macOS/Linux:
```
source venv/bin/activate
```

3. Install dependencies:
```
pip install -r requirements.txt
```

## Running the Server

Start the server with:
```
uvicorn main:app --reload
```

The server will run at http://localhost:8000

## API Endpoints

- `GET /api/health`: Check if the server is running
- `POST /api/transcribe`: Upload an audio file for transcription

## API Documentation

FastAPI automatically generates interactive API documentation:
- Swagger UI: http://localhost:8000/docs
- ReDoc: http://localhost:8000/redoc
