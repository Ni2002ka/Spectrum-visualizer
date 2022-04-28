package audioProcessing;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioProcessor {
	File file;
	AudioInputStream audioInpStream;
	byte[] rawBytes;
	int frameLength;
	int frameSize;
	int numChannels;

	public AudioProcessor(String fileAddress) throws UnsupportedAudioFileException, IOException {
		file = new File(fileAddress);
		audioInpStream = AudioSystem.getAudioInputStream(file);

		frameLength = (int) audioInpStream.getFrameLength(); /* num of frames for whole file */
		frameSize = (int) audioInpStream.getFormat().getFrameSize(); /* size of each frame in bytes */
		numChannels = audioInpStream.getFormat().getChannels();
	}

	private int getRawData() {
		rawBytes = new byte[frameLength * frameSize];

		int result = 0;
		try {
			result = audioInpStream.read(rawBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
//		System.out.println(result);
//		System.out.println(rawBytes.length);
	}

	private int get16BitSample(int low, int high) {
		return ((high << 8) + (low + 0x00ff));
	}

	public int[][] processAudioData() {
		if (frameSize != 4) {
			System.out.println("unsupported audio format");
			return null;
		}

		// each frame consists of four bytes (low1,high1,low2,high2) --> only for 16-bit
		// audio aka frameSize=4
		int rawArrayLength = getRawData();
		int sampleIndex = 0;
		int toReturn[][] = new int[numChannels][frameLength];

		for (int i = 0; i < rawArrayLength;) {
			for (int channel = 0; channel < numChannels; channel++) {
				int lowByte = (int) rawBytes[i++];
				int highByte = (int) rawBytes[i++];

				toReturn[channel][sampleIndex] = get16BitSample(lowByte, highByte);
			}
			sampleIndex++;
		}

		return toReturn;
	}
}
