package visualizer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class AudioPanel extends JPanel {
	int[][] processedAudio;
	int scalingFactor;
	private int width = 800;
	private int height = 200;
	private AudioInputStream song;
	private float frameRate;
	

	public AudioPanel(int[][] processedAudio, AudioInputStream song) {
		this.setPreferredSize(new Dimension(width, height));
		this.processedAudio = processedAudio;
		scalingFactor = processedAudio[0].length / width;
		this.song = song;
		frameRate = song.getFormat().getFrameRate();
		
		doStuff();
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
	private Clip clip;
	private int i=0;
	private Timer timer;
	
	public void doStuff() {
		channelZeroCompressed = compressedAudioArray(0);
		try {
			clip=AudioSystem.getClip();
			int delay=(int)(1000*scalingFactor/frameRate);
			clip.open(song);
			timer = new Timer(delay-10, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (i<width-1) {
						repaint();
					}
					else {
						clip.close();
						timer.stop();
					}
					
				}
			});
            timer.setRepeats(true);
            timer.setCoalesce(false);
            timer.setInitialDelay(0);
            timer.start(); 
			
			clip.start();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawLine(0, height / 2, width, height / 2);
		
		g2d.drawLine(i, 100 - channelZeroCompressed[i], i + 1, 100 - channelZeroCompressed[i + 1]);
		i++;
		
	}

}
