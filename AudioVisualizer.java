package visualizer;

import javax.sound.sampled.AudioInputStream;

public class AudioVisualizer {
	public AudioVisualizer(int[][] processedAudio, AudioInputStream song) {
		new AudioFrame(processedAudio,song);
	}
}
