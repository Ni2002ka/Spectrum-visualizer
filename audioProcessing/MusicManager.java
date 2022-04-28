package audioProcessing;

import java.util.ArrayList;

public class MusicManager {

	//amplitude at each sample measurement
//	private final int []  amp_time;
	private int size;
	//meaningful increments between notes
//	private int physicalFactor=10000;
	//Average (abs. value) samples over this period
	private final int avgTime;
	private int [] averagedSamples;
	
	public MusicManager(int [] amp_time, int averagingTime) {
//		this.amp_time=amp_time;
		this.size=amp_time.length;
		this.avgTime=averagingTime;
		
		AverageSamples(amp_time);
	}
	
	private void AverageSamples(int[] amp_time) {
		averagedSamples=new int [size/avgTime+1];
		int N=0;
		for (int i=0;i<size;i+=avgTime) {
			int sum=0,num=0;
			for (int j=0;j<avgTime&&i+j<size;j++) {
				sum+=Math.abs(amp_time[i+j]);
				num++;
			}
//			System.out.println(sum/num);
			averagedSamples[N++]=sum/num;
		}
		
		size=size/avgTime+1;
	}

	private int maxAmplitude() {
		int MAX=Math.abs(averagedSamples[0]); 
		for (int i=1;i<size;i++) {
			if (MAX<Math.abs(averagedSamples[i]))
				MAX=Math.abs(averagedSamples[i]);
		}
		return MAX;
	}
	
	//toggle values for best input
	private int maxChangePerNotePacket() {
		return (maxAmplitude()/10);
	}
	
	public ArrayList<Integer> newNotePresses (){
		ArrayList<Integer> results=new ArrayList<>(0);
		int prev_sample=averagedSamples[0];
		int maxIncrement=maxChangePerNotePacket();
		for (int i=1;i<size;i++) {
			int presentSample=averagedSamples[i];
			if (presentSample-prev_sample>=maxIncrement) {
				results.add(i*avgTime);
//				i+=physicalFactor;
			}
			prev_sample=presentSample;
		}
		
		System.out.println(results.size());
		return results;
	}
}
