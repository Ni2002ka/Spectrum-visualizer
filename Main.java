import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import audioProcessing.AudioProcessor;
import audioProcessing.MusicManager;
import visualizer.AudioVisualizer;

public class Main {

	public static void main(String[] args) {
		try {
			String address="src/E1.wav";
			AudioProcessor ass = new AudioProcessor(address);
			AudioInputStream song = AudioSystem.getAudioInputStream(new File(address));
			int avgTime= (int) (song.getFormat().getFrameRate()*0.2);
			int[][] processedAudioArray = ass.processAudioData();
			MusicManager managerZero=new MusicManager(processedAudioArray[0],avgTime);
			MusicManager managerOne=new MusicManager(processedAudioArray[1],avgTime);

			ArrayList<Integer> channelZeroNotePresses=managerZero.newNotePresses();
			ArrayList<Integer> channelOneNotePresses=managerOne.newNotePresses();
			
			new AudioVisualizer(processedAudioArray, song, channelZeroNotePresses, channelOneNotePresses);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
