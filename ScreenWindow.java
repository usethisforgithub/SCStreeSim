
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
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
	private boolean addTrajToggle, resetMapToggle, pauseToggle, addDroneToggle;
	

	
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
		
		//draws add trajectory button
		if(addTrajToggle){
			
			g2.setColor(Color.red);
			g2.fillRect(30, 80, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Add In Progress", 85, 105);
		}else{
			
			g2.setColor(Color.green);
			g2.fillRect(30, 80, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Add Trajectory", 85, 105);
		}
		
		//draws pause button
		if(!pauseToggle){
			
			g2.setColor(Color.green);
			g2.fillRect(30, 140, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Resume", 85, 165);
		}else{
			
			g2.setColor(Color.red);
			g2.fillRect(30, 140, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Pause", 85, 165);
		}
		
		//draws remove drone button
		if(true){
			
			g2.setColor(Color.green);
			g2.fillRect(30, 200, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Remove Drones", 85, 225);
		}else{
			
			g2.setColor(Color.red);
			g2.fillRect(30, 200, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Remove Drones", 85, 225);
		}
		
		//draws uncovering resilience button
				if(true){
					
					g2.setColor(Color.green);
					g2.fillRect(30, 260, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Show Uncovering Resilience", 85, 285);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(30, 260, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Uncovering Resilience", 85, 285);
				}
				
		//draws isolation button
				if(true){
					
					g2.setColor(Color.green);
					g2.fillRect(30, 320, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Show Isolation", 85, 345);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(30, 320, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Isolation", 85, 345);
				}
				
				
				//draws add drone button
				if(true){
					
					g2.setColor(Color.green);
					g2.fillRect(30, 380, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Add drones", 85, 405);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(30, 380, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Add Drones", 85, 405);
				}		
				
				
				
		//draws reset map button
				if(!resetMapToggle){
					
					g2.setColor(Color.green);
					g2.fillRect(30, 440, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Reset Map", 85, 465);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(30, 440, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Reset", 85, 465);
				}
				
		//draws save map button
				if(true){
					
					g2.setColor(Color.green);
					g2.fillRect(30, 500, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Save Map", 85, 525);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(30, 500, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Save", 85, 525);
				}
				
				
				
		//draws load map button
				if(true){
					
					g2.setColor(Color.green);
					g2.fillRect(30, 560, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Load Map", 85, 585);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(30, 560, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Isolation", 85, 585);
				}
		
		//draws help button
			g2.setColor(Color.white);
			g2.fillRect(370, 40, 20, 20);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("?", 375, 55);

			
		
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
		if((arg0.getX() >= 30 && arg0.getX() <= 70) && (arg0.getY() >= 380 && arg0.getY() <= 420)){
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

				myPanel.add(new JLabel("Direction (-1 or 1):"));
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
						int direction = Integer.parseInt(temp3);
						
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
						Robot tempBot = new Robot(existingTraj, ((Math.PI/180)*angle));
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
			if((arg0.getX() >= 30 && arg0.getX() <= 70) && (arg0.getY() >= 80 && arg0.getY() <= 120)){
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
										TrajAnglePair tap1,tap2;
										tap1 = new TrajAnglePair(map.get(i),newTraj.angleFrom(map.get(i)));
										tap2 = new TrajAnglePair(newTraj,map.get(i).angleFrom(newTraj));
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
				for(int i = 0; i < map.size(); i++){
					System.out.println("Traj #" + map.get(i).getID() + " neighbors: ");
					for(int j = 0; j < map.get(i).getNeighbors().size(); j++){
						System.out.println("Traj #" + map.get(i).getNeighbors().get(j).getTraj().getID() + " at " + map.get(i).getNeighbors().get(j).getAngle() + " degrees" );
					}
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
			}
			
			
			if((arg0.getX() >= 30 && arg0.getX() <= 70) && (arg0.getY() >= 140 && arg0.getY() <= 180)){
				pauseToggle = !pauseToggle;
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

	
