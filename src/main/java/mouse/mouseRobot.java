package mouse;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.MouseInfo;

public class mouseRobot implements Runnable {

	//time in miliseconds
	private int frequency;
	private long startTime;
	private int durationTime = 0;
	private long finishTime = 0;
	
	private static Robot bot;
	
	public volatile boolean paused; 
	
	public mouseRobot () {
		try{
			bot = new Robot();
			frequency = 60000;
			paused = true;
		}catch (AWTException e) {
			System.out.println(e);
		}
	}
	
	public void run(){
		try{
			startTime = System.currentTimeMillis();
			if(durationTime!=0) {
				finishTime = startTime + durationTime;
			}
			while (!Thread.interrupted() & (System.currentTimeMillis()<=finishTime || finishTime == 0)) {
				int [] currentCoordinates = getMouseCoordinates();
				System.out.println("X:  " + currentCoordinates[0]);
				System.out.println("Y:  " + currentCoordinates[1]);
				bot.mouseMove(currentCoordinates[0] + 1, currentCoordinates[1]);
				Thread.sleep (frequency);
			}
			paused=true;
			MickeyGUI.changeButtons();
			
		} catch (InterruptedException e) {
			System.out.println("Interrupted Threat: " + e);	
		}
	}
	
	
	private int[] getMouseCoordinates() {
		int[] mouseCoordinates = new int[2];
		mouseCoordinates[0] = MouseInfo.getPointerInfo().getLocation().x;
		mouseCoordinates[1] = MouseInfo.getPointerInfo().getLocation().y;
		return mouseCoordinates;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency*1000;
	}
	
	
	public void setDurationTimeMins(int durationTime) {
		this.durationTime = durationTime*1000*60;
	}
	
	
	public int getFrequencySec() {
		return frequency/1000;
	}

	public long getDurationTimeMins() {
		return durationTime/60000;
	}
}