package audioProcessing;

import java.util.ArrayList;

public class MusicManager {

	//amplitude at each sample measurement
	private final int []  amp_time;
	private final int size;
	//meaningful increments between notes
	private int physicalFactor=10000;
	
	public MusicManager(int [] amp_time) {
		this.amp_time=amp_time;
		this.size=amp_time.length;
	}

	private int maxAmplitude() {
		int MAX=Math.abs(amp_time[0]); 
		for (int i=1;i<size;i++) {
			if (MAX<Math.abs(amp_time[i]))
				MAX=Math.abs(amp_time[i]);
		}
//		System.out.println(MAX);
		return MAX;
	}
	
	//toggle values for best input
	private int maxChangePerNotePacket() {
		return (maxAmplitude()/3);
	}
	
	public ArrayList<Integer> newNotePresses (){
		System.out.println(size);
		ArrayList<Integer> results=new ArrayList<>(0);
		int prev_sample=Math.abs(amp_time[0]);
		int maxIncrement=maxChangePerNotePacket();
		for (int i=1;i<size;i++) {
			int presentSample=Math.abs(amp_time[i]);
			if (Math.abs(presentSample-prev_sample)>=maxIncrement) {
				results.add(i);
				i+=physicalFactor;
			}
		}
		
		return results;
	}
}
