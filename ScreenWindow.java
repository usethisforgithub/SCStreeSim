
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;



public class ScreenWindow extends Frame implements WindowListener, Runnable, KeyListener, MouseListener, MouseMotionListener{

	//window stuff
	private int trajIDIndex;
	private boolean isRunning,isDone;
	private Image imgBuffer;
	private final int windowSizeX = 1600;
	private final int windowSizeY = 900;
	private Trajectory ghostTrajBuild;
	private ArrayList<Robot> listBot;
	
	//SCS stuff
	private int trajSize;
	private ArrayList<Trajectory> map;
	
	//toggles
	private boolean addTrajToggle, resetMapToggle, pauseToggle, addDroneToggle, helpToggle, addTrajDroneToggle;
	

	
	public ScreenWindow(){
		super();
		listBot = new ArrayList<Robot>();
		addDroneToggle = false;
		pauseToggle = true;
		trajIDIndex = 1;
		addTrajToggle = false;
		resetMapToggle = false;
		
		//adds the first trajectory to the map
		trajSize = 100;
		map = new ArrayList<Trajectory>();
		map.add(new Trajectory(new Coordinate(1000,450), trajSize,0));
		ghostTrajBuild = null;
		
	
		//more window stuff
		imgBuffer = this.createImage(windowSizeX, windowSizeY);
		this.addWindowListener(this);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setSize(windowSizeX, windowSizeY);
		this.setTitle("SCS Tree Simulator");
		isRunning = true;
		isDone = false;
		this.setVisible(true);
		
		this.setResizable(false);
		
	}
	
	public void run(){
		while(isRunning){
			draw();
			
			
			if(!pauseToggle){
				for(int i = 0; i < listBot.size(); i++){
					//increments drone positions
					Robot r = listBot.get(i);
					if(listBot.get(i).getDirection() == 1){
						listBot.get(i).setAngle(listBot.get(i).getAngle() + 1);
						listBot.get(i).setAngle(AngleUtilities.coterminal(listBot.get(i).getAngle()));
					}else{
						listBot.get(i).setAngle(listBot.get(i).getAngle() - 1);
						listBot.get(i).setAngle(AngleUtilities.coterminal(listBot.get(i).getAngle()));
					}
					//System.out.println(listBot.get(i).getAngle());
					
					
					
					
					for(int j = 0; j < listBot.get(i).getTraj().getNeighbors().size(); j++){
						if(listBot.get(i).getAngle() == listBot.get(i).getTraj().getNeighbors().get(j).getAngle()){
							boolean sensed = false;
							for(int l = 0; l < listBot.size(); l++){
								if(listBot.get(i).inRange(listBot.get(l)) && l != i){
									sensed = true;
								}
							}
							
							
							
							
							if(!sensed){
							
							int oldAngle = listBot.get(i).getAngle();
							
							Trajectory tempTraj = listBot.get(i).getTraj().getNeighbors().get(j).getTraj();
							int newAngle= -9999999;
							
							for(int k = 0; k < tempTraj.getNeighbors().size();k++){
								if(tempTraj.getNeighbors().get(k).getTraj().getID() == listBot.get(i).getTraj().getID()){
									newAngle = tempTraj.getNeighbors().get(k).getAngle();
								}
							}
							
							System.out.println("switched from " + oldAngle + " to " + newAngle);
							
							if(listBot.get(i).getDirection() == 1){
								listBot.get(i).setDirection(-1);
								
							}else{
								listBot.get(i).setDirection(1);
							}
							
							listBot.get(i).getTraj().removeBot(listBot.get(i));
							listBot.get(i).setTrajectory(tempTraj);
							tempTraj.addBot(listBot.get(i));
							listBot.get(i).setAngle(newAngle);
							j = 999999999;
						}
						}
					}
					
					
					
					
					
					
					//checks to switch trajectories
					/*
					boolean loopFix = true;
					for(int j = 0; j < listBot.get(i).getTraj().getNeighbors().size(); j++){
						//counterclockwise
						if(listBot.get(i).getDirection() == 1){
							if(listBot.get(i).getAngle() == listBot.get(i).getTraj().getNeighbors().get(j).getAngle()){
								
								for(int k = 0; k < listBot.size(); k++){
									if(k != i && listBot.get(i).inRange(listBot.get(k))){
										
										listBot.get(i).setHasSensed(true);
									}
								}
								
								if(!listBot.get(i).hasSensed() && loopFix){
									
									int critAngle = listBot.get(i).getTraj().getNeighbors().get(j).getAngle();
									listBot.get(i).getTraj().removeBot(listBot.get(i));
									listBot.get(i).setTrajectory(listBot.get(i).getTraj().getNeighbors().get(j).getTraj());
									listBot.get(i).getTraj().addBot(listBot.get(i));
									listBot.get(i).setAngle(listBot.get(i).getAngle()+180+2*(critAngle-listBot.get(i).getAngle()));
									listBot.get(i).setDirection(-1);
									listBot.get(i).setHasSensed(false);
									j = -1;
									loopFix = false;
								}
								listBot.get(i).setHasSensed(false);
								
							}
						}else{//clockwise
							if(listBot.get(i).getAngle() == listBot.get(i).getTraj().getNeighbors().get(j).getAngle()){
								
								for(int k = 0; k < listBot.size(); k++){
									if(k != i && listBot.get(i).inRange(listBot.get(k))){
										
										listBot.get(i).setHasSensed(true);
									}
								}
								
								if(!listBot.get(i).hasSensed() && loopFix){
									
									int critAngle = listBot.get(i).getTraj().getNeighbors().get(j).getAngle();
									listBot.get(i).getTraj().removeBot(listBot.get(i));
									listBot.get(i).setTrajectory(listBot.get(i).getTraj().getNeighbors().get(j).getTraj());
									listBot.get(i).getTraj().addBot(listBot.get(i));
									listBot.get(i).setAngle(listBot.get(i).getAngle()+180+2*(critAngle-listBot.get(i).getAngle()));
									listBot.get(i).setDirection(1);
									listBot.get(i).setHasSensed(false);
									j = -1;
									loopFix = false;
								}
								listBot.get(i).setHasSensed(false);
								
								
							}
						}
					}
					*/
				}
			}
			
			
			
			try{
				Thread.sleep(10);
				}catch(InterruptedException ie){
					ie.printStackTrace();
				}
		}
		isDone = true;
	}
	
	
	public void draw(){
		imgBuffer = this.createImage(this.getWidth(), this.getHeight());
		Graphics2D g2 = (Graphics2D)imgBuffer.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//background color
		g2.setColor(Color.white);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
				
		if(ghostTrajBuild != null){
			//ghostTrajBuild.setColor(Color.green);
			ghostTrajBuild.draw(g2);
		}
		
		//draws menu background
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 0, 400, 900);
		
		//draws Toolbar
		g2.setColor(Color.black);
		g2.setFont(new Font("Callibri", Font.PLAIN, 18));
		g2.drawString("Toolbar", 160, 60);
		
		//draws angle chart
		if(helpToggle)
		{
			g2.drawOval(200, 700, trajSize + 30, trajSize + 30);
			g2.setFont(new Font("Callibri", Font.PLAIN, 12));
			g2.drawString("0", 200 + trajSize + 40, 700 + (trajSize + 30)/2);
			g2.drawString("180", 200 - 30, 700 + (trajSize + 30)/2);
			g2.drawString("90", 208 + trajSize/2, 695);
			//System.out.println(Math.cos(120));
			g2.drawString("60", 147 + trajSize/2 + (int)(trajSize * (0.952)), 745 + trajSize/2 - (int)(trajSize * (0.952)));
			g2.drawString("45", 211 + trajSize/2 + (int)(trajSize * (0.525)), 714 + trajSize/2 - (int)(trajSize * (0.525)));
			g2.drawString("30", 263 + trajSize/2 + (int)(trajSize * (0.154)), 695 + trajSize/2 - (int)(trajSize * (0.154)));
			g2.drawString("120", 170 + trajSize/2, 745 + trajSize/2 - (int)(trajSize * (0.952)));
			g2.drawString("135", 146 + trajSize/2, 716 + trajSize/2 - (int)(trajSize * (0.525)));
			g2.drawString("150", 130 + trajSize/2, 700 + trajSize/2 - (int)(trajSize * (0.154)));
			g2.drawString("210", 128 + trajSize/2, 760 + trajSize/2 - (int)(trajSize * (0.154)));
			g2.drawString("225", 139 + trajSize/2, 818 + trajSize/2 - (int)(trajSize * (0.525)));
			g2.drawString("240", 160 + trajSize/2, 836 + trajSize/2 - (int)(trajSize * (0.525)));
			g2.drawString("270", 200 + trajSize/2, 844);
			g2.drawString("300", 145 + trajSize/2 + (int)(trajSize * (0.952)), 837);
			g2.drawString("315", 215 + trajSize/2 + (int)(trajSize * (0.525)), 824);
			g2.drawString("330", 264 + trajSize/2 + (int)(trajSize * (0.154)), 798);
		}
		
		//draws add trajectory button
		if(addTrajToggle){
			
			g2.setColor(Color.red);
			g2.fillRect(30, 140, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Add In Progress", 85, 165);
		}else{
			
			g2.setColor(Color.green);
			g2.fillRect(30, 140, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Add Trajectory", 85, 165);
		}
		
		//draws add trajectory and drone button
			if(addTrajDroneToggle){
				
				g2.setColor(Color.blue);
				g2.fillRect(220, 140, 40, 40);
				g2.setColor(Color.black);
				g2.setFont(new Font("Callibri", Font.PLAIN, 16));
				g2.drawString("Add In Progress", 275, 165);
			}else{
				
				g2.setColor(Color.green);
				g2.fillRect(220, 140, 40, 40);
				g2.setColor(Color.black);
				g2.setFont(new Font("Callibri", Font.PLAIN, 16));
				g2.drawString("Add Trajectory", 275, 165);
				g2.drawString("and drone", 290, 185);
			}
		
		//draws pause button
		if(!pauseToggle){
			
			g2.setColor(Color.red);
			g2.fillRect(30, 80, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Pause", 85, 105);
		}else{
			
			g2.setColor(Color.green);
			g2.fillRect(30, 80, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Resume", 85, 105);
		}
		
		//draws remove drone button
		if(true){
			
			g2.setColor(Color.magenta);
			g2.fillRect(220, 200, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Remove Drones", 270, 225);
		}else{
			
			g2.setColor(Color.red);
			g2.fillRect(220, 200, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Remove Drones", 270, 225);
		}
		
		//draws show neighbors button
				if(true){
					
					g2.setColor(Color.green);
					g2.fillRect(30, 260, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("show neighbors", 85, 285);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(30, 260, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("show neighbors", 85, 285);
				}
				
		//draws isolation button
				if(true){
					
					g2.setColor(Color.green);
					g2.fillRect(220, 260, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Show Isolation", 270, 285);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(220, 260, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Isolation off", 270, 345);
				}
				
				
				//draws add drone button
				if(true){
					
					g2.setColor(Color.magenta);
					g2.fillRect(30, 200, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Add drones", 85, 225);
				}	
				
				
				
		//draws reset map button
				if(!resetMapToggle){
					
					g2.setColor(Color.red);
					g2.fillRect(30, 440, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Reset Map", 85, 465);
				}
				
		//draws save map button
				if(true){
					
					g2.setColor(Color.cyan);
					g2.fillRect(30, 320, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Save Map", 85, 345);
				}	
								
		//draws load map button
				if(true){
					
					g2.setColor(Color.cyan);
					g2.fillRect(220, 320, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Load Map", 270, 345);
				}
		
		//draws help button
			if(!helpToggle)
			{
				g2.setColor(Color.white);
				g2.fillRect(370, 40, 20, 20);
				g2.setColor(Color.black);
				g2.setFont(new Font("Callibri", Font.PLAIN, 16));
				g2.drawString("?", 375, 55);
			}
			else
			{
				g2.setColor(Color.black);
				g2.fillRect(370, 40, 20, 20);
				g2.setColor(Color.white);
				g2.setFont(new Font("Callibri", Font.PLAIN, 16));
				g2.drawString("?", 375, 55);
			}

			
		
		//draws the trajectories in the map
		for(int i = 0; i < map.size(); i++){
			map.get(i).setColor(Color.black);
			map.get(i).draw(g2);
		}
		
		//draws drones
		for(int i = 0; i < listBot.size(); i++){
			listBot.get(i).draw(g2);
		}
		
			
		
		g2 = (Graphics2D)this.getGraphics();
		g2.drawImage(imgBuffer, 0, 0, this.getWidth(), this.getHeight(), 0, 0, this.getWidth(), this.getHeight(), null);
		g2.dispose();
	}
	
	
	

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		while(true){
			if(isDone){
				System.exit(0);
			}try{
				Thread.sleep(100);
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		this.setVisible(false);
		isRunning = false;
		this.dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
	
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
		
		
		
		
		//add drone
		if((arg0.getX() >= 30 && arg0.getX() <= 70) && (arg0.getY() >= 200 && arg0.getY() <= 240)){
			if(!addDroneToggle){
				addDroneToggle = true;
				
				JTextField aField = new JTextField(5);
				JTextField bField = new JTextField(5);
				JTextField cField = new JTextField(5);
				JTextField dField = new JTextField(5);

				JPanel myPanel = new JPanel();
				myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

				myPanel.add(new JLabel("Enter Angle:"));
				myPanel.add(aField);

				myPanel.add(Box.createVerticalStrut(15));

				myPanel.add(new JLabel("Trajectory's ID:"));
				myPanel.add(bField);

				myPanel.add(Box.createVerticalStrut(15));
				
				myPanel.add(new JLabel("Direction(1 or -1)"));
				myPanel.add(cField);

				myPanel.add(Box.createVerticalStrut(15));


				int result = JOptionPane.showConfirmDialog(null, myPanel, " Enter Values For New SCS Simulation", JOptionPane.OK_CANCEL_OPTION);
				Trajectory existingTraj = null;
				int tempInt = 0;
				
				if (result == JOptionPane.OK_OPTION) {
					String temp1 = aField.getText();
					String temp2 = bField.getText();
					String temp3 = cField.getText();
					

					if (!temp1.equals("") && !temp2.equals("") && !temp3.equals("")) {
						int angle = Integer.parseInt(temp1);
						
						
						for(Trajectory t : map){
							tempInt = Integer.parseInt(temp2);
							if(tempInt == t.getID()){
								existingTraj = t;
							}
						}
						
						int tempDir = Integer.parseInt(temp3); 
						
						/*
						int newDir = 1;
						if(existingTraj.getDirection() > 0){
							newDir = -1;
						}
						*/
						Robot tempBot = new Robot(existingTraj, (angle),tempDir);
						existingTraj.addBot(tempBot);
						listBot.add(tempBot);
						
						
						
						
					}
					else {
						System.out.println("Field was left empty. New trajectory was not added.");
					}
					
				}
			
				addDroneToggle = false;
			
			}
			
			
			
			
		}
		
		
		
		//add trajectory button
			if((arg0.getX() >= 30 && arg0.getX() <= 70) && (arg0.getY() >= 140 && arg0.getY() <= 180)){
				if(!addTrajToggle){
					addTrajToggle = true;
					
					JTextField aField = new JTextField(5);
					JTextField bField = new JTextField(5);
					JTextField cField = new JTextField(5);
					JTextField dField = new JTextField(5);

					JPanel myPanel = new JPanel();
					myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

					myPanel.add(new JLabel("Enter Angle:"));
					myPanel.add(aField);

					myPanel.add(Box.createVerticalStrut(15));

					myPanel.add(new JLabel("Existing Trajectory's ID:"));
					myPanel.add(bField);

					myPanel.add(Box.createVerticalStrut(15));

					
					/*
					myPanel.add(new JLabel("Enter initial direction of first trajectory"));
					myPanel.add(new JLabel("-1 (clockwise) or 1 (counterclockwise):"));
					myPanel.add(cField);

					myPanel.add(Box.createVerticalStrut(15));

					myPanel.add(new JLabel("Enter initial angle of first drone"));
					myPanel.add(new JLabel("in radians between (0.00 - 6.28):"));
					myPanel.add(dField); */

					int result = JOptionPane.showConfirmDialog(null, myPanel, " Enter Values For New SCS Simulation", JOptionPane.OK_CANCEL_OPTION);
					Trajectory existingTraj = null;
					int tempInt = 0;
					
					if (result == JOptionPane.OK_OPTION) {
						String temp1 = aField.getText();
						String temp2 = bField.getText();
						

						if (!temp1.equals("") && !temp2.equals("")) {
							double angle = Double.parseDouble(temp1);
							
							
							for(Trajectory t : map){
								tempInt = Integer.parseInt(temp2);
								if(tempInt == t.getID()){
									existingTraj = t;
								}
							}
							
							/*
							int newDir = 1;
							if(existingTraj.getDirection() > 0){
								newDir = -1;
							}
							*/
							Trajectory newTraj = new Trajectory (new Coordinate(existingTraj.getVertex().getX() + (existingTraj.getSize()+(existingTraj.getSize()/6))*Math.cos((Math.PI/180)*angle), existingTraj.getVertex().getY() - (existingTraj.getSize()+(existingTraj.getSize()/6))*Math.sin((Math.PI/180)*angle)),existingTraj.getSize(), trajIDIndex);
							boolean overlaps = false;
							for(int i = 0; i < map.size(); i++)
							{
								if(!newTraj.overlaps(map.get(i))){
									
								}
								else
								{
									System.out.println("Overlaps.");
									overlaps = true;
								}
							}
							if(!overlaps){
								map.add(newTraj);
								trajIDIndex++;
				
							
								//loop to set neighbors
								for(int i = 0; i < map.size(); i++) {
									if(newTraj.tangent(map.get(i))) {
										
										//newTraj.addNeighbor(map.get(i));
										
										
										
										
										TrajAnglePair tap1,tap2;
										tap1 = new TrajAnglePair(map.get(i), map.get(i).angleFrom(newTraj));
										tap2 = new TrajAnglePair(newTraj, newTraj.angleFrom(map.get(i)));
										//System.out.println("first: "  + newTraj.angleFrom(map.get(i)));
										//System.out.println("second: " + map.get(i).angleFrom(newTraj));
										newTraj.addNeighbor(tap1);
										map.get(i).addNeighbor(tap2);
									}
								}
							
							
							
							
							}
							
						}
						else {
							System.out.println("Field was left empty. New trajectory was not added.");
						}
						
					}
				
					addTrajToggle = false;
				
				}
				
				
				//displays neighbors
				/*
				for(int i = 0; i < map.size(); i++){
					System.out.println("Traj #" + map.get(i).getID() + " neighbors: ");
					for(int j = 0; j < map.get(i).getNeighbors().size(); j++){
						System.out.println("Traj #" + map.get(i).getNeighbors().get(j).getTraj().getID() + " at " + map.get(i).getNeighbors().get(j).getAngle() + " degrees" );
					}
				}
				System.out.println();
				*/
				
			
				
			}
			
			
			//add traj and drone button 
			if((arg0.getX() >= 220 && arg0.getX() <= 260) && (arg0.getY() >= 140 && arg0.getY() <= 180)){
				if(!addTrajDroneToggle){
					addTrajDroneToggle = true;
					
					JTextField aField = new JTextField(5);
					JTextField bField = new JTextField(5);
					JTextField cField = new JTextField(5);
					JTextField dField = new JTextField(5);

					JPanel myPanel = new JPanel();
					myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

					myPanel.add(new JLabel("Enter Angle:"));
					myPanel.add(aField);

					myPanel.add(Box.createVerticalStrut(15));

					myPanel.add(new JLabel("Existing Trajectory's ID:"));
					myPanel.add(bField);

					myPanel.add(Box.createVerticalStrut(15));

					
					/*
					myPanel.add(new JLabel("Enter initial direction of first trajectory"));
					myPanel.add(new JLabel("-1 (clockwise) or 1 (counterclockwise):"));
					myPanel.add(cField);

					myPanel.add(Box.createVerticalStrut(15));

					myPanel.add(new JLabel("Enter initial angle of first drone"));
					myPanel.add(new JLabel("in radians between (0.00 - 6.28):"));
					myPanel.add(dField); */

					int result = JOptionPane.showConfirmDialog(null, myPanel, " Enter Values For New Trajectory", JOptionPane.OK_CANCEL_OPTION);
					Trajectory existingTraj = null;
					int tempInt = 0;
					
					if (result == JOptionPane.OK_OPTION) {
						String temp1 = aField.getText();
						String temp2 = bField.getText();
						

						if (!temp1.equals("") && !temp2.equals("")) {
							double angle = Double.parseDouble(temp1);
							
							
							for(Trajectory t : map){
								tempInt = Integer.parseInt(temp2);
								if(tempInt == t.getID()){
									existingTraj = t;
								}
							}
							
							/*
							int newDir = 1;
							if(existingTraj.getDirection() > 0){
								newDir = -1;
							}
							*/
							Trajectory newTraj = new Trajectory (new Coordinate(existingTraj.getVertex().getX() + (existingTraj.getSize()+(existingTraj.getSize()/6))*Math.cos((Math.PI/180)*angle), existingTraj.getVertex().getY() - (existingTraj.getSize()+(existingTraj.getSize()/6))*Math.sin((Math.PI/180)*angle)),existingTraj.getSize(), trajIDIndex);
							
							//adds new bot
							if(!existingTraj.getRobotList().isEmpty())
							{
								int ang = Math.abs(existingTraj.getRobotList().get(0).getAngle());
								int angNeigh = newTraj.angleFrom(existingTraj);
								int distance = Math.abs(360 - ang + angNeigh);
								int newAng = AngleUtilities.coterminal(existingTraj.angleFrom(newTraj) + distance);
								int dir = existingTraj.getRobotList().get(0).getDirection();
								if(dir == 1){
									dir = -1;
								}else{
									dir = 1;
								}
								Robot r = new Robot(newTraj, newAng, dir);
								newTraj.addBot(r);
								listBot.add(r);		
							}
									
							boolean overlaps = false;
							for(int i = 0; i < map.size(); i++)
							{
								if(!newTraj.overlaps(map.get(i))){
									
								}
								else
								{
									System.out.println("Overlaps.");
									overlaps = true;
								}
							}
							if(!overlaps){
								map.add(newTraj);
								trajIDIndex++;
				
							
								//loop to set neighbors
								for(int i = 0; i < map.size(); i++) {
									if(newTraj.tangent(map.get(i))) {
										
										//newTraj.addNeighbor(map.get(i));
										
										
										
										
										TrajAnglePair tap1,tap2;
										tap1 = new TrajAnglePair(map.get(i), map.get(i).angleFrom(newTraj));
										tap2 = new TrajAnglePair(newTraj, newTraj.angleFrom(map.get(i)));
										//System.out.println("first: "  + newTraj.angleFrom(map.get(i)));
										//System.out.println("second: " + map.get(i).angleFrom(newTraj));
										newTraj.addNeighbor(tap1);
										map.get(i).addNeighbor(tap2);
									}
								}
							
							
							
							}
							
						}
						else {
							System.out.println("Field was left empty. New trajectory was not added.");
						}
						
					}
				
					addTrajDroneToggle = false;
				
				}
			}
			
			
			//reset map button
			if((arg0.getX() >= 30 && arg0.getX() <= 70) && (arg0.getY() >= 440 && arg0.getY() <= 480)){
				System.out.println("Map has been reset");
				if(!resetMapToggle){
					resetMapToggle = true;
					map = new ArrayList<Trajectory>();
					map.add(new Trajectory(new Coordinate(1000,450), trajSize,0));
					trajIDIndex = 1;
					resetMapToggle = false;
				}
				
				listBot = new ArrayList<Robot>();
			}
			
			
			//save map button
			if((arg0.getX() >= 30 && arg0.getX() <= 70) && (arg0.getY() >= 320 && arg0.getY() <= 360)){
				JFrame parentFrame = new JFrame();
				 
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");   
				 
				int userSelection = fileChooser.showSaveDialog(parentFrame);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					try{
						File file = fileChooser.getSelectedFile();
						String filename = file.getAbsolutePath() + ".txt";
						FileWriter fw = new FileWriter(filename, true);
						BufferedWriter bw = new BufferedWriter(fw);
						
						for(Trajectory t : map)
						{
							bw.write(Integer.toString(t.getID()));
							bw.newLine();
							bw.write(t.getVertex().getX() + " " + t.getVertex().getY());
							bw.newLine();
						}
						//bw.newLine();
						bw.write("List of robots");
						bw.newLine();
						for(Trajectory t : map)
						{
							for(Robot r : t.getRobotList())
							{
								bw.write(Integer.toString(t.getID()));
								bw.newLine();
								bw.write(Double.toString((int)r.getAngle()) + " " + r.getDirection());
								bw.newLine();
							}
						}
						bw.close();
						System.out.println(file + ".txt");
					}
					catch(IOException ioe)
					{
						System.err.println(":(");
					}
				}
				}

			//neighbor button
			if((arg0.getX() >= 30 && arg0.getX() <= 70) && (arg0.getY() >= 260 && arg0.getY() <= 300)){
				for(int i = 0; i < map.size(); i++){
					System.out.println("Traj #" + map.get(i).getID());
					for(int j = 0; j < map.get(i).getNeighbors().size(); j++){
						System.out.println("#" + map.get(i).getNeighbors().get(j).getTraj().getID() + " at " + map.get(i).getNeighbors().get(j).getAngle());
					}
				}
			}
			
			//load map button
			if((arg0.getX() >= 220 && arg0.getX() <= 260) && (arg0.getY() >= 320 && arg0.getY() <= 360)){
				JFrame parentFrame = new JFrame();
				JFileChooser fileChooser = new JFileChooser();
				boolean trajlist = true;
				int returnVal = fileChooser.showOpenDialog(parentFrame);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					File file = fileChooser.getSelectedFile();
					try {
						Scanner scan = new Scanner(file);
						map = new ArrayList<Trajectory>();
						listBot = new ArrayList<Robot>();
						while(scan.hasNext())
						{
							String first = scan.next();
							if(first.equals("List"))
							{
								trajlist = false;
							}
							if(trajlist == true)
							{
								int tempId = Integer.parseInt(first);
								scan.nextLine();
								double x = Double.parseDouble(scan.next());
								double y = (double)scan.nextInt();
								scan.nextLine();
								Trajectory traj = new Trajectory(new Coordinate(x, y), trajSize, tempId);
								map.add(traj);
								
								
								
					
								//loop to set neighbors
								for(int i = 0; i < map.size(); i++) {
									if(traj.tangent(map.get(i))) {
										
										//newTraj.addNeighbor(map.get(i));
										
										
										TrajAnglePair tap1,tap2;
										tap1 = new TrajAnglePair(map.get(i), map.get(i).angleFrom(traj));
										tap2 = new TrajAnglePair(traj, traj.angleFrom(map.get(i)));
										//System.out.println("first: "  + newTraj.angleFrom(map.get(i)));
										//System.out.println("second: " + map.get(i).angleFrom(newTraj));
										traj.addNeighbor(tap1);
										map.get(i).addNeighbor(tap2);
									}
								}
								
								
								
								
								
								
								
							}
							else if(trajlist == false)
							{
								int tempTraj = 2;
								String next = scan.nextLine();
								if(next.equals(" of robots"))
								{
									tempTraj = Integer.parseInt(scan.next());	
									scan.nextLine();
								}
								else
								{
									tempTraj = Integer.parseInt(first);
								}
								int angle = (int)scan.nextDouble();
								int dir = scan.nextInt();
								Trajectory t = null;
								
								for(int i = 0; i < map.size(); i++){
									if(map.get(i).getID() == tempTraj){
										t = map.get(i);
									}
								}
								Robot rob = new Robot(t, angle, dir);
								t.addBot(rob);
								listBot.add(rob);
							}
						}
						scan.close();
					} catch (FileNotFoundException e) {
						System.out.println("File not found.");
					} catch(NumberFormatException e){
						
					}catch(NoSuchElementException e){
						
					}
					
				}
			}
			
			//removes robot
			if((arg0.getX() >= 220 && arg0.getX() <= 260) && (arg0.getY() >= 200 && arg0.getY() <= 240)){
				listBot = new ArrayList<Robot>();
			}
			
			
			//pause button
			if((arg0.getX() >= 30 && arg0.getX() <= 70) && (arg0.getY() >= 80 && arg0.getY() <= 120)){
				pauseToggle = !pauseToggle;
			}
			
			//help button
			if((arg0.getX() >= 370 && arg0.getX() <= 390) && (arg0.getY() >= 40 && arg0.getY() <= 60))
			{
				helpToggle = !helpToggle;
			}
			
			
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		}
		
	}

	
