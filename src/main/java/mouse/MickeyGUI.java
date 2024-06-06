/* 
 Mickey
 Version 0.2
	+ Added timer to stop mouse movement.
*/

package mouse;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.awt.*;
import java.awt.event.*;

public class MickeyGUI {

	private JFrame frame;
	private static JButton buttonStart;
	private static JButton buttonSetTimer;
	private static JLabel frequencyLabel;
	private static JLabel durationLabel;
	private static JSlider slider;
	
	private static final Color red = new Color (235, 123, 89);
	private static final Color green = new Color (207, 240, 158);
	private static final Color grey = new Color (186, 177, 177);
	private static final Font textFont = new Font("Helvetica", Font.BOLD, 10);
	private static final int minSeconds = 10;
	private static final int maxSeconds = 900;
	private static int defaultSeconds = 60;
	
	private static mouseRobot mouseBot;
	private static Thread t1;
	
	
	public static void main (String[] args){		
		mouseBot = new mouseRobot();
		t1 = new Thread(mouseBot);
		
		MickeyGUI gui = new MickeyGUI();
		gui.prepareGUI();
	}
	
	public void prepareGUI() {
		frame = new JFrame("Mickey");
		
		buttonStart = new JButton("START");
		buttonStart.setFont(textFont);
		buttonStart.setBackground(green);
		buttonStart.setBounds(50,50,120,30);
		
		buttonSetTimer = new JButton("Set Timer");
		buttonSetTimer.setFont(textFont);
		buttonSetTimer.setBackground(grey);
		buttonSetTimer.setBounds(50,50,60,30);
			
		slider = new JSlider(minSeconds, maxSeconds, defaultSeconds);
		slider.setMajorTickSpacing(60);
		slider.setPaintLabels(false);
		slider.setPaintTicks(true);
		
		frequencyLabel = new JLabel("Mouse moves every__ " + timeAdjustment(mouseBot.getFrequencySec()), SwingConstants.CENTER);
		frequencyLabel.setFont(textFont);
		frequencyLabel.setVerticalAlignment(JLabel.CENTER);
		frequencyLabel.setBounds(20,10,200,30);
		
		durationLabel = new JLabel("Stop timer is set at__ " + mouseBot.getDurationTimeMins() + " mins.", SwingConstants.CENTER);
		durationLabel.setFont(textFont);
		durationLabel.setVerticalAlignment(JLabel.CENTER);
		durationLabel.setBounds(20,10,200,30);

		buttonStart.addActionListener(new buttonStartListener());
		slider.addChangeListener(new sliderListener());
		buttonSetTimer.addActionListener(new buttonTimerListener());
		
		GridLayout gridLayout = new GridLayout(5,1,5,0);
		
		frame.getContentPane().add(BorderLayout.CENTER,slider);
		frame.getContentPane().add(BorderLayout.PAGE_START,frequencyLabel);
		frame.getContentPane().add(BorderLayout.PAGE_START,durationLabel);
		frame.getContentPane().add(BorderLayout.PAGE_END,buttonSetTimer);
		frame.getContentPane().add(BorderLayout.PAGE_END,buttonStart);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(270, 180);
		frame.setResizable(false);
		frame.setLayout(gridLayout);
		frame.validate();
		frame.setVisible(true);
	}
	
	public static String timeAdjustment(int timeSec) {
		int minutes = timeSec/60;;
		int seconds = timeSec - minutes*60;
			return minutes + " minutes "+ seconds + " seconds";
	}
	
	public static void changeButtons() {
		if (!mouseBot.paused) {
			buttonStart.setText("STOP");
			buttonStart.setBackground(red);
			buttonSetTimer.setEnabled(false);
		}
		
		else {
			buttonStart.setText("START");
			buttonStart.setBackground(green);
			buttonSetTimer.setEnabled(true);
		}
	}

	class buttonStartListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
			mouseBot.paused = !mouseBot.paused;
			changeButtons();
			if (!mouseBot.paused) {
				t1 = new Thread(mouseBot);
				t1.start();
			}
			else {
				t1.interrupt();
			}
		}
	}
	
	class sliderListener implements ChangeListener {
		public void stateChanged (ChangeEvent event) {
	        JSlider source = (JSlider)event.getSource();
	        frequencyLabel.setText("Mouse moves every__ " + timeAdjustment(source.getValue()));
	        if (!source.getValueIsAdjusting()) {
	            int time = (int)source.getValue();
	            mouseBot.setFrequency(time);
	            }
		}
	}
	
	class buttonTimerListener implements ActionListener {
		public void actionPerformed (ActionEvent event) {
            int duration = Integer.parseInt(JOptionPane.showInputDialog(frame,"Set stop timer in minutes", null));
            if ((duration > 0)) {
            	mouseBot.setDurationTimeMins(duration);
            }
            durationLabel.setText("Stop timer is set at__ " + mouseBot.getDurationTimeMins() + " mins.");
		}
	}
	
}
