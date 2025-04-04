// VoiceRecorder.jsx
import React, { useState, useRef } from 'react';
import axios from 'axios';

const VoiceRecorder = () => {
  const [isRecording, setIsRecording] = useState(false);
  const [recordingStatus, setRecordingStatus] = useState('');
  const [audioURL, setAudioURL] = useState('');
  const [transcription, setTranscription] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  
  const mediaRecorderRef = useRef(null);
  const audioChunksRef = useRef([]);
  
  // Start recording function
  const startRecording = async () => {
    try {
      // Reset previous recording data
      audioChunksRef.current = [];
      setAudioURL('');
      setTranscription('');
      
      // Request microphone access
      const stream = await navigator.mediaDevices.getUserMedia({ audio: {
        sampleRate: 16000, // Match Whisper's preferred input
        channelCount: 1,   // Mono audio
        echoCancellation: true,
        noiseSuppression: true,
      }});
      
      // Create MediaRecorder instance
      // WebM with Opus codec is well-supported and efficient
      const mediaRecorder = new MediaRecorder(stream, {
        mimeType: 'audio/webm;codecs=opus',
      });
      
      // Set up event handlers
      mediaRecorder.ondataavailable = (event) => {
        if (event.data.size > 0) {
          audioChunksRef.current.push(event.data);
        }
      };
      
      mediaRecorder.onstop = () => {
        const audioBlob = new Blob(audioChunksRef.current, { type: 'audio/webm' });
        const url = URL.createObjectURL(audioBlob);
        setAudioURL(url);
        setRecordingStatus('Recording saved.');
      };
      
      // Start recording
      mediaRecorder.start();
      mediaRecorderRef.current = mediaRecorder;
      setIsRecording(true);
      setRecordingStatus('Recording...');
    } catch (error) {
      console.error('Error starting recording:', error);
      setRecordingStatus(`Error: ${error.message}`);
    }
  };
  
  // Stop recording function
  const stopRecording = () => {
    if (mediaRecorderRef.current && isRecording) {
      mediaRecorderRef.current.stop();
      setIsRecording(false);
      
      // Stop all audio tracks
      mediaRecorderRef.current.stream.getTracks().forEach(track => track.stop());
    }
  };
  
  // Send the recorded audio to the backend
  const sendToBackend = async () => {
    if (!audioURL) {
      setRecordingStatus('No recording available to send.');
      return;
    }
    
    try {
      setIsLoading(true);
      
      // Create a Blob from the recorded audio
      const audioBlob = await fetch(audioURL).then(r => r.blob());
      
      // Create FormData to send the file
      const formData = new FormData();
      formData.append('audio', audioBlob, 'recording.webm');
      
      // Send to your backend
      const response = await axios.post('/api/transcribe', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      
      // Update UI with transcription
      setTranscription(response.data.transcription);
      setIsLoading(false);
      setRecordingStatus('Transcription completed.');
    } catch (error) {
      console.error('Error sending audio:', error);
      setRecordingStatus(`Error: ${error.message}`);
      setIsLoading(false);
    }
  };
  
  return (
    <div className="voice-recorder">
      <h2>Voice Recorder</h2>
      
      <div className="controls">
        {!isRecording ? (
          <button onClick={startRecording} disabled={isLoading}>
            Start Recording
          </button>
        ) : (
          <button onClick={stopRecording} disabled={isLoading}>
            Stop Recording
          </button>
        )}
        
        <button 
          onClick={sendToBackend} 
          disabled={!audioURL || isLoading}
        >
          {isLoading ? 'Processing...' : 'Transcribe'}
        </button>
      </div>
      
      <p className="status">{recordingStatus}</p>
      
      {audioURL && (
        <div className="audio-preview">
          <audio src={audioURL} controls />
        </div>
      )}
      
      {transcription && (
        <div className="transcription">
          <h3>Transcription:</h3>
          <p>{transcription}</p>
        </div>
      )}
    </div>
  );
};

export default VoiceRecorder;