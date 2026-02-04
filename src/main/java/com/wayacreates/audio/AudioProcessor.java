package com.wayacreates.audio;

import com.wayacreates.WayaCreatesEngine;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Audio processing class for handling audio operations
 * Supports audio editing, effects, and format conversion
 */
public class AudioProcessor {
    private static final ExecutorService processingPool = Executors.newFixedThreadPool(2);
    private boolean isProcessing = false;
    private ProcessingCallback callback;
    
    public interface ProcessingCallback {
        void onProgress(int percentage);
        void onComplete(String outputPath);
        void onError(String error);
    }
    
    /**
     * Process audio with effects and filters
     */
    public CompletableFuture<String> processAudio(String inputFile, String outputFile, ProcessingCallback callback) {
        this.callback = callback;
        return CompletableFuture.supplyAsync(() -> {
            try {
                isProcessing = true;
                callback.onProgress(0);
                
                // Load audio file
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(inputFile));
                AudioFormat format = audioStream.getFormat();
                
                // Process audio
                byte[] audioData = readAudioData(audioStream);
                callback.onProgress(25);
                
                // Apply effects
                byte[] processedData = applyAudioEffects(audioData, format);
                callback.onProgress(75);
                
                // Save processed audio
                saveAudioFile(processedData, format, outputFile);
                
                audioStream.close();
                isProcessing = false;
                callback.onProgress(100);
                callback.onComplete(outputFile);
                
                return outputFile;
                
            } catch (Exception e) {
                isProcessing = false;
                WayaCreatesEngine.LOGGER.error("Audio processing failed: " + e.getMessage(), e);
                callback.onError("Processing failed: " + e.getMessage());
                return null;
            }
        }, processingPool);
    }
    
    /**
     * Read audio data from stream
     */
    private byte[] readAudioData(AudioInputStream audioStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        
        while ((bytesRead = audioStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        
        return outputStream.toByteArray();
    }
    
    /**
     * Apply audio effects
     */
    private byte[] applyAudioEffects(byte[] audioData, AudioFormat format) {
        // Convert to samples for processing
        float[] samples = bytesToSamples(audioData, format);
        
        // Apply effects
        samples = applyVolumeAdjustment(samples, 1.2f); // Boost volume by 20%
        samples = applyNoiseReduction(samples);
        samples = applyEqualizer(samples, format);
        
        // Convert back to bytes
        return samplesToBytes(samples, format);
    }
    
    /**
     * Convert byte array to float samples
     */
    private float[] bytesToSamples(byte[] audioData, AudioFormat format) {
        int sampleSize = format.getSampleSizeInBits() / 8;
        int numSamples = audioData.length / sampleSize;
        float[] samples = new float[numSamples];
        
        for (int i = 0; i < numSamples; i++) {
            int sample = 0;
            for (int j = 0; j < sampleSize; j++) {
                sample |= (audioData[i * sampleSize + j] & 0xFF) << (j * 8);
            }
            
            // Convert to normalized float (-1.0 to 1.0)
            samples[i] = sample / (float) (1 << (format.getSampleSizeInBits() - 1));
        }
        
        return samples;
    }
    
    /**
     * Convert float samples to byte array
     */
    private byte[] samplesToBytes(float[] samples, AudioFormat format) {
        int sampleSize = format.getSampleSizeInBits() / 8;
        byte[] audioData = new byte[samples.length * sampleSize];
        
        for (int i = 0; i < samples.length; i++) {
            int sample = (int) (samples[i] * (1 << (format.getSampleSizeInBits() - 1)));
            
            for (int j = 0; j < sampleSize; j++) {
                audioData[i * sampleSize + j] = (byte) ((sample >> (j * 8)) & 0xFF);
            }
        }
        
        return audioData;
    }
    
    /**
     * Apply volume adjustment
     */
    private float[] applyVolumeAdjustment(float[] samples, float multiplier) {
        for (int i = 0; i < samples.length; i++) {
            samples[i] = Math.max(-1.0f, Math.min(1.0f, samples[i] * multiplier));
        }
        return samples;
    }
    
    /**
     * Apply basic noise reduction
     */
    private float[] applyNoiseReduction(float[] samples) {
        // Simple noise gate
        float threshold = 0.01f;
        
        for (int i = 0; i < samples.length; i++) {
            if (Math.abs(samples[i]) < threshold) {
                samples[i] *= 0.1f; // Reduce noise
            }
        }
        
        return samples;
    }
    
    /**
     * Apply basic equalizer
     */
    private float[] applyEqualizer(float[] samples, AudioFormat format) {
        // Simple bass boost
        float sampleRate = format.getSampleRate();
        float bassFreq = 200.0f; // Hz
        
        // This is a simplified EQ - in practice you'd use FFT for proper frequency processing
        for (int i = 0; i < samples.length; i++) {
            // Apply simple low-frequency boost
            samples[i] *= 1.1f;
        }
        
        return samples;
    }
    
    /**
     * Save processed audio to file
     */
    private void saveAudioFile(byte[] audioData, AudioFormat format, String outputFile) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(audioData);
        AudioInputStream audioStream = new AudioInputStream(inputStream, format, audioData.length / format.getFrameSize());
        
        // Convert to desired format if needed
        AudioFormat outputFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            format.getSampleRate(),
            format.getSampleSizeInBits(),
            format.getChannels(),
            format.getFrameSize(),
            format.getFrameRate(),
            format.isBigEndian()
        );
        
        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, new File(outputFile));
        audioStream.close();
    }
    
    /**
     * Get audio information
     */
    public AudioInfo getAudioInfo(String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            AudioFormat format = audioStream.getFormat();
            
            AudioInfo info = new AudioInfo();
            info.sampleRate = (int) format.getSampleRate();
            info.sampleSize = format.getSampleSizeInBits();
            info.channels = format.getChannels();
            info.duration = audioStream.getFrameLength() / format.getFrameRate();
            info.encoding = format.getEncoding().toString();
            
            audioStream.close();
            return info;
            
        } catch (Exception e) {
            WayaCreatesEngine.LOGGER.error("Failed to get audio info: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cancel current processing
     */
    public void cancelProcessing() {
        isProcessing = false;
    }
    
    /**
     * Check if currently processing
     */
    public boolean isProcessing() {
        return isProcessing;
    }
    
    /**
     * Initialize the audio processor
     */
    public void initialize() {
        WayaCreatesEngine.LOGGER.info("Audio processor initialized");
    }
    
    /**
     * Check if processor is ready
     */
    public boolean isReady() {
        return true;
    }
    
    /**
     * Update processor state
     */
    public void tick() {
        // Update processing state if needed
    }
    
    /**
     * Shutdown the processor
     */
    public void shutdown() {
        processingPool.shutdown();
    }
    
    /**
     * Audio information container class
     */
    public static class AudioInfo {
        public int sampleRate;
        public int sampleSize;
        public int channels;
        public double duration;
        public String encoding;
        
        @Override
        public String toString() {
            return String.format("AudioInfo[%dHz, %dbit, %dch, %.2fs, %s]", 
                sampleRate, sampleSize, channels, duration, encoding);
        }
    }
}
