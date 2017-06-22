import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Robot {

	private boolean sensing, hasFlipped;
	private Trajectory t;
	private int angle;
	private int sizeR;
	private int startingX;
	private int startingY;
	private boolean labelToggle;
	private boolean starving;
	private boolean isoToggle;
	
	
	public Robot( Trajectory traj, int ang, int x, int y)
	{
		labelToggle = false;
		hasFlipped = false;
		sensing = false;
		t = traj;
		angle = ang;
		sizeR = t.getSize()/5;
		startingX = x;
		startingY = y;
		starving = false;
		isoToggle = false;
	}
	
	public void setIsolationToggle(boolean b){
		isoToggle = b;
	}
	
	public void setFlipped(boolean h){
		hasFlipped = h;
	}
	
	public boolean getFlipped(){
		return hasFlipped;
	}
	
	public Trajectory getTraj()
	{
		return t;
	}
	public void setTrajectory(Trajectory tr){
		t=tr;
	}
	
	public void setAngle(int a){
		angle = a;
	}
	
	public int getAngle()
	{
		return angle;
	}
	
	public void setSensing(boolean state){
		sensing = state;
	}
	
	public boolean getSensing(){
		return sensing;
	}
	
	public boolean isStarving(){
		return starving;
	}
	
	public void setStarving(boolean s){
		starving = s;
	}

	
	public boolean contains(Coordinate c){
		double centerX = t.getVertex().getX() + t.getSize()/2*Math.cos(angle);
		double centerY = t.getVertex().getY() - t.getSize()/2*Math.sin(angle);
		double leg1 = Math.abs(centerX - c.getY());
		double leg2 = Math.abs(centerY - c.getY());
		double hypotenuse = Math.sqrt(leg1 * leg1 + leg2 * leg2);
		return hypotenuse < sizeR / 2;
	}
	
	public void setLabelToggle(boolean b){
		labelToggle = b;
	}
	
	public void draw(Graphics2D g2)
	{
		
		
		
		
		if(starving && isoToggle){
			g2.setColor(Color.yellow);
		}else{
			g2.setColor(Color.black);
		}
		//g2.setColor(Color.black);
	
		g2.fill(new Ellipse2D.Double(t.getVertex().getX() + t.getSize()/2*Math.cos(angle) - sizeR/2, t.getVertex().getY() - t.getSize()/2*Math.sin(angle) - sizeR/2, sizeR, sizeR));//t.getSize()*Math.cos(angle)
	
		if(starving && isoToggle){
			g2.setColor(Color.black);
		}else{
			g2.setColor(Color.white);
		}
		
		Font font = new Font("Callibri", Font.PLAIN, sizeR/3);//font.getStringBounds("Waller's Triangle Game", g2.getFontRenderContext()).getWidth())/2)
		
		g2.setFont(font);
		//g2.setStroke(new BasicStroke(sizeR/4));
		if(labelToggle){
		g2.drawString(startingX +","+startingY, (int)(t.getVertex().getX() + t.getSize()/2*Math.cos(angle)-sizeR/6), (int)(t.getVertex().getY() - t.getSize()/2*Math.sin(angle)+sizeR/6));
		}
	
	}
	
	public Coordinate getPosition(){
		return new Coordinate(t.getVertex().getX() + t.getSize()/2*Math.cos(angle) - sizeR/2,t.getVertex().getY() + t.getSize()/2*Math.sin(angle) - sizeR/2);
	}
	
}
