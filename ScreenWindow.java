
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
	
	//SCS stuff
	private int trajSize;
	private ArrayList<Trajectory> map;
	
	//toggles
	private boolean addTrajToggle;
	

	
	public ScreenWindow(){
		super();
		trajIDIndex = 1;
		addTrajToggle = false;
		
		//adds the first trajectory to the map
		trajSize = 100;
		map = new ArrayList<Trajectory>();
		map.add(new Trajectory(new Coordinate(1000,450), 1, trajSize,0));
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
		if(true){
			
			g2.setColor(Color.green);
			g2.fillRect(30, 140, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Pause", 85, 165);
		}else{
			
			g2.setColor(Color.red);
			g2.fillRect(30, 140, 40, 40);
			g2.setColor(Color.black);
			g2.setFont(new Font("Callibri", Font.PLAIN, 16));
			g2.drawString("Build Mode", 85, 165);
		}
		
		//draws kill drone button
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
				
		//draws reset map button
				if(true){
					
					g2.setColor(Color.green);
					g2.fillRect(30, 440, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Reset Map", 85, 405);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(30, 440, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Isolation", 85, 405);
				}
				
		//draws save map button
				if(true){
					
					g2.setColor(Color.green);
					g2.fillRect(30, 500, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Save Map", 85, 465);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(30, 500, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Isolation", 85, 465);
				}
				
		//draws add drone button
				if(true){
					
					g2.setColor(Color.green);
					g2.fillRect(30, 380, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Add drones", 85, 525);
				}else{
					
					g2.setColor(Color.red);
					g2.fillRect(30, 380, 40, 40);
					g2.setColor(Color.black);
					g2.setFont(new Font("Callibri", Font.PLAIN, 16));
					g2.drawString("Isolation", 85, 525);
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
					
					if (result == JOptionPane.OK_OPTION) {
						String temp1 = aField.getText();
						String temp2 = bField.getText();
						

						if (!temp1.equals("") && !temp2.equals("")) {
							double angle = Double.parseDouble(temp1);
							Trajectory existingTraj;
							
							for(Trajectory t : map){
								int tempInt = Integer.parseInt(temp2);
								if(tempInt == t.getID()){
									existingTraj = t;
								}
							}
							
							map.add(new Trajectory(new Coordinate(existingTraj.getVertex().getX())))
							
						}
						else {
							System.out.println("Field was left empty. New trajectory was not added.");
						}
						
					}
				
					addTrajToggle = false;
				
				}
			}
		
		if(ghostTrajBuild != null){
		boolean overLap = false;
		for(Trajectory t : map){
			if(ghostTrajBuild.overlaps(t)){
				overLap = true;
			}
		}
		
		
		if(!overLap){
			map.add(ghostTrajBuild);
			ghostTrajBuild = null;
		}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		for(int i = 0; i < map.size(); i++){
			if(map.get(i).onBorder(new Coordinate(e.getX(),e.getY()))){
				
				
				//colors
				
				int trigAngle = AngleUtilities.getAngle(map.get(i).getVertex(), new Coordinate(e.getX(),e.getY()));
				double adjustRadius = trajSize/2 + trajSize/6;
				double startingX = map.get(i).getVertex().getX()+(trajSize/2)*Math.cos((Math.PI/180)*trigAngle);
				double startingY = map.get(i).getVertex().getY()-(trajSize/2)*Math.sin((Math.PI/180)*trigAngle);
				System.out.println(trigAngle);
				ghostTrajBuild = new Trajectory(new Coordinate(startingX+adjustRadius*Math.cos((Math.PI/180)*trigAngle), startingY -adjustRadius*Math.sin((Math.PI/180)*trigAngle)),1,trajSize, -1);
				
			}
			}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		boolean ghostControl = false;
		for(int i = 0; i < map.size(); i++){
			
			if(map.get(i).onBorder(new Coordinate(e.getX(),e.getY()))){
				
				
				
				
				int trigAngle = AngleUtilities.getAngle(map.get(i).getVertex(), new Coordinate(e.getX(),e.getY()));
				double adjustRadius = trajSize/2 + trajSize/6;
				double startingX = map.get(i).getVertex().getX()+(trajSize/2)*Math.cos((Math.PI/180)*trigAngle);
				double startingY = map.get(i).getVertex().getY()-(trajSize/2)*Math.sin((Math.PI/180)*trigAngle);
				ghostTrajBuild = new Trajectory(new Coordinate(startingX+adjustRadius*Math.cos((Math.PI/180)*trigAngle), startingY -adjustRadius*Math.sin((Math.PI/180)*trigAngle)),1,trajSize,-1);
				//ghostTrajBuild.setReady(true);
				ghostControl = true;
			}
			
			
			}
		
		if(!ghostControl){
			
				ghostTrajBuild = null;
			
		}
		
		
		if(ghostTrajBuild != null){
			boolean overLap = false;
			for(Trajectory t : map){
				if(ghostTrajBuild.overlaps(t)){
					overLap = true;
				}
			}
			
			
			if(!overLap){
				ghostTrajBuild.setColor(Color.green);
			}else{
				ghostTrajBuild.setColor(Color.red);
			}
			}
		
		
		}
		
	}

	
