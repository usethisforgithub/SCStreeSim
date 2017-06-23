
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
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



public class ScreenWindow extends Frame implements WindowListener, Runnable, KeyListener, MouseListener, MouseMotionListener{

	//window stuff
	private boolean isRunning,isDone;
	private Image imgBuffer;
	private final int windowSizeX = 1600;
	private final int windowSizeY = 900;
	private Trajectory ghostTrajBuild;
	
	//SCS stuff
	private int trajSize;
	private ArrayList<Trajectory> map;
	
	

	
	public ScreenWindow(){
		super();
		
		//adds the first trajectory to the map
		trajSize = 100;
		map = new ArrayList<Trajectory>();
		map.add(new Trajectory(new Coordinate(1000,450), 1, trajSize));
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
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		if(ghostTrajBuild!= null){
		boolean overLap = false;
		
		for(int i = 0; i < map.size(); i++){
			if(ghostTrajBuild.overlaps(map.get(i))){
				overLap = true;
			}
		}
		
		if(overLap){
			ghostTrajBuild.setColor(Color.red);
		}else{
			ghostTrajBuild.setColor(Color.green);
		}
		
		
		if(ghostTrajBuild.isReady() && !overLap){
			ghostTrajBuild.setColor(Color.black);
			map.add(ghostTrajBuild);
			ghostTrajBuild = null;
		}
		
	}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		for(Trajectory f : map){
			if(f.onBorder(new Coordinate(e.getX(),e.getY()))){
				double hDis = f.hDis(new Coordinate(e.getX(),e.getY()));
				double vDis = f.vDis(new Coordinate(e.getX(),e.getY()));
				double trigAngle = f.getTrigAngle(new Coordinate(f.getVertex().getX() - hDis - (hDis/Math.abs(hDis))*(trajSize/6), f.getVertex().getY() - vDis - (vDis/Math.abs(vDis))*trajSize/6));
				double adjustRadius = trajSize/2 + trajSize/6;
				double hCorrector = -1;
				if(hDis < 0){
					hCorrector = 1;
				}
				
				double vCorrector = -1;
				if(vDis < 0){
					vCorrector = 1;
				}
				
				double startingX = f.getVertex().getX()+(trajSize/2)*hCorrector*Math.cos(trigAngle);
				double startingY = f.getVertex().getY()+(trajSize/2)*vCorrector*Math.sin(trigAngle);
				ghostTrajBuild = new Trajectory(new Coordinate(startingX+hCorrector*adjustRadius*Math.cos(trigAngle), startingY +vCorrector* adjustRadius*Math.sin(trigAngle)),1,trajSize);
				
				ghostTrajBuild.setReady(true);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
		for(Trajectory f : map){
			if(f.onBorder(new Coordinate(e.getX(),e.getY()))){
				double hDis = f.hDis(new Coordinate(e.getX(),e.getY()));
				double vDis = f.vDis(new Coordinate(e.getX(),e.getY()));
				double trigAngle = f.getTrigAngle(new Coordinate(f.getVertex().getX() - hDis - (hDis/Math.abs(hDis))*(trajSize/6), f.getVertex().getY() - vDis - (vDis/Math.abs(vDis))*trajSize/6));
				double adjustRadius = trajSize/2 + trajSize/6;
				double hCorrector = -1;
				if(hDis < 0){
					hCorrector = 1;
				}
				
				double vCorrector = -1;
				if(vDis < 0){
					vCorrector = 1;
				}
				
				double startingX = f.getVertex().getX()+(trajSize/2)*hCorrector*Math.cos(trigAngle);
				double startingY = f.getVertex().getY()+(trajSize/2)*vCorrector*Math.sin(trigAngle);
				ghostTrajBuild = new Trajectory(new Coordinate(startingX+hCorrector*adjustRadius*Math.cos(trigAngle), startingY +vCorrector* adjustRadius*Math.sin(trigAngle)),1,trajSize);
				ghostTrajBuild.setReady(true);
			}
		}
	}

	
}