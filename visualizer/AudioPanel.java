package visualizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AudioPanel extends JPanel implements Runnable {
	private final int[][] processedAudio;
	private final int scalingFactor;
	private final int width = 1200;
	private final int height = 200;
	private AudioInputStream song;
	private final float frameRate;
	Thread visualizerThread;
	private final ArrayList<Integer> channelZeroNotePresses;
	private final ArrayList<Integer> channelOneNotePresses;

	private void set_up() {
		this.setPreferredSize(new Dimension(width, 2 * height));
		visualizerThread = new Thread(this);
		visualizerThread.start();
		
	}
	public AudioPanel(int[][] processedAudio, AudioInputStream song) {
		this.processedAudio = processedAudio;
		scalingFactor = processedAudio[0].length / width;
		this.song = song;
		frameRate = song.getFormat().getFrameRate();
		channelZeroNotePresses=null;
		channelOneNotePresses=null;
		
		set_up();

	}

	public AudioPanel(int[][] processedAudioArray, AudioInputStream song, ArrayList<Integer> channelZeroNotePresses,
			ArrayList<Integer> channelOneNotePresses) {
		this.processedAudio = processedAudioArray;
		scalingFactor = processedAudioArray[0].length / width;
		this.song = song;
		frameRate = song.getFormat().getFrameRate();
		this.channelZeroNotePresses=channelZeroNotePresses;
		this.channelOneNotePresses=channelOneNotePresses;
		
		set_up();
	}

	private int[] compressedAudioArray(int numChannel) {
		double[] result = new double[width];
		int numFrames = processedAudio[numChannel].length;

		int N = 0;
		for (int i = 0; i < width; i++) {
			int sum = 0, num = 0;

			for (; num < scalingFactor && N < numFrames; num++) {
				sum += processedAudio[numChannel][N++];
			}
			result[i] = sum / (double) num;
		}

		return scaleSpectrum(result);
	}

	private int[] scaleSpectrum(double unscaledResults[]) {
		double MAX = -1;
		for (int i = 0; i < width; i++)
			if (Math.abs(unscaledResults[i]) > MAX)
				MAX = Math.abs(unscaledResults[i]);

		int[] scaledArray = new int[width];
		for (int i = 0; i < width; i++) {
			scaledArray[i] = (int) (unscaledResults[i] * (height - 20) / (double) (2 * MAX));
		}

		return scaledArray;
	}

	private int[] channelZeroCompressed;
	private int[] channelOneCompressed;
	private Clip clip;
	private int i = 0;
	long currentTime;

	public boolean update() {
		if (i < width - 1) {
			repaint();
			return true;
		} else {
			clip.close();
			return false;
		}
	}

	public void run() {
		channelZeroCompressed = compressedAudioArray(0);
		channelOneCompressed = compressedAudioArray(1);
		try {
			clip = AudioSystem.getClip();
			long delay = (long) (1000000000 / (double) frameRate) * scalingFactor;
//			System.out.println(delay);
			clip.open(song);
			clip.start();

			currentTime = System.nanoTime();
			while (update()) {
				while (System.nanoTime() - currentTime < delay) {
				}
				currentTime = System.nanoTime();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Graphics2D g2d;

	private void drawNoteLines() {
		//channel zero
		for (int j : channelZeroNotePresses) {
			int x=j/scalingFactor;
			g2d.drawLine(x, 10, x, height-10);;
		}
		
		//channel one
		for (int j : channelOneNotePresses) {
			int x=j/scalingFactor;
			g2d.drawLine(x, height+10 , x, 2*height-10);;
		}
	}
	
	public void paint(Graphics g) {
		g2d = (Graphics2D) g;

		g2d.setColor(Color.gray);
		g2d.drawLine(0, height / 2, width, height / 2);
		if (channelOneNotePresses !=null) {
			drawNoteLines();
		}

		g2d.setColor(Color.black);
		g2d.drawLine(0, height, width, height);

		g2d.setColor(Color.gray);
		g2d.drawLine(0, 3 * height / 2, width, 3 * height / 2);

		g2d.setColor(Color.blue);
		g2d.drawLine(i, height / 2 - channelZeroCompressed[i], i + 1, height / 2 - channelZeroCompressed[i + 1]);
		g2d.setColor(Color.red);
		g2d.drawLine(i, 3 * height / 2 - channelOneCompressed[i], i + 1, 3 * height / 2 - channelOneCompressed[i + 1]);
		i++;

		Toolkit.getDefaultToolkit().sync();
	}

}
