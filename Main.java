import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import audioProcessing.AudioProcessor;
import visualizer.AudioVisualizer;

public class Main {

	public static void main(String[] args) {
		try {
			AudioProcessor ass = new AudioProcessor("src/test.wav");
			AudioInputStream song = AudioSystem.getAudioInputStream(new File("src/test.wav"));
			int[][] processedAudioArray = ass.processAudioData();

			new AudioVisualizer(processedAudioArray, song);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
