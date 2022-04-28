package visualizer;

import javax.sound.sampled.AudioInputStream;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class AudioFrame extends JFrame {
	AudioPanel panel;

	public AudioFrame(int[][] processedAudio, AudioInputStream song) {
		panel = new AudioPanel(processedAudio, song);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(panel);
		// why
		this.pack();
		// center of screen
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
