package visualizer;

import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class AudioFrame extends JFrame {
	AudioPanel panel;

	private void set_up() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(panel);
		// why
		this.pack();
		// center of screen
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	public AudioFrame(int[][] processedAudio, AudioInputStream song) {
		panel = new AudioPanel(processedAudio, song);

		set_up();
	}

	public AudioFrame(int[][] processedAudioArray, AudioInputStream song, ArrayList<Integer> channelZeroNotePresses,
			ArrayList<Integer> channelOneNotePresses) {
		panel = new AudioPanel(processedAudioArray, song, channelZeroNotePresses, channelOneNotePresses);

		set_up();
	}
}
