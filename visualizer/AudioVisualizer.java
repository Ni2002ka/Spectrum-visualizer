package visualizer;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;

public class AudioVisualizer {
	public AudioVisualizer(int[][] processedAudio, AudioInputStream song) {
		new AudioFrame(processedAudio,song);
	}

	public AudioVisualizer(int[][] processedAudioArray, AudioInputStream song,
			ArrayList<Integer> channelZeroNotePresses, ArrayList<Integer> channelOneNotePresses) {
		new AudioFrame(processedAudioArray,song,channelZeroNotePresses,channelOneNotePresses);
	}
}
