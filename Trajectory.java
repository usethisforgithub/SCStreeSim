import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Trajectory {

	private Coordinate vertex;
	private int direction;
	private int sizeT;
	private ArrayList<Robot> bots;
	private Arc arc1;
	private Arc arc2;
	private Arc arc3;
	private Arc arc4;
	
	public Trajectory(Coordinate v, int dir, int s)
	{
		
		
		arc1 = new Arc(this,1);
		arc2 = new Arc(this,2);
		arc3 = new Arc(this,3);
		arc4 = new Arc(this,4);
		bots = new ArrayList<Robot>();
		vertex = v;
		direction = dir;
		sizeT = s;
	}
	
	public void setColor(Color c){
		arc1.setColor(c);
		arc2.setColor(c);
		arc3.setColor(c);
		arc4.setColor(c);
	}	
	
	public void setArc1(Arc a){
		arc1 = a;
	}
	
	public void setArc2(Arc a){
		arc2 = a;
	}
	
	public void setArc3(Arc a){
		arc3 = a;
	}
	
	public void setArc4(Arc a){
		arc4 = a;
	}
	
	public Arc getArc1(){
		return arc1;
	}
	public Arc getArc2(){
		return arc2;
	}
	
	public Arc getArc3(){
		return arc3;
	}
	
	public Arc getArc4(){
		return arc4;
	}
	
	public void addBot(Robot b){
		bots.add(b);
	}
	
	public void removeBot(Robot b){
		bots.remove(b);
	}
	
	public Coordinate getVertex()
	{
		return vertex;
	}
	
	public int getDirection()
	{
		return direction;
	}
	
	public int getSize()
	{
		return sizeT;
	}
	
	public ArrayList<Robot> getRobotList(){
		return bots;
	}
	
	public boolean contains(Coordinate c){
		double centerX = vertex.getX();
		double centerY = vertex.getY();
		double leg1 = Math.abs(centerX - c.getX());
		double leg2 = Math.abs(centerY - c.getY());
		double hypotenuse = Math.sqrt(leg1 * leg1 + leg2 * leg2);
		return hypotenuse < sizeT / 2;
	}
	
	public double getTrigAngle(Coordinate c){
		double centerX = vertex.getX();
		double centerY = vertex.getY();
		double leg1 = Math.abs(centerX - c.getX());
		double leg2 = Math.abs(centerY - c.getY());
		double hypotenuse = Math.sqrt(leg1 * leg1 + leg2 * leg2);
		return Math.atan(leg2/leg1);
	}
	
	public double hDis(Coordinate c){
		double centerX = vertex.getX();
		double leg1 = centerX - c.getX();
		return leg1;
	}
	
	public double vDis(Coordinate c){
		double centerY = vertex.getY();
		double leg2 = centerY - c.getY();
		return leg2;
	}
	
	public boolean onBorder(Coordinate c){
		double centerX = vertex.getX();
		double centerY = vertex.getY();
		double leg1 = Math.abs(centerX - c.getX());
		double leg2 = Math.abs(centerY - c.getY());
		double hypotenuse = Math.sqrt(leg1 * leg1 + leg2 * leg2);
		return Math.abs(hypotenuse - sizeT/2)  <= 10;
	}
	
	
	
	public void draw(Graphics2D g2)
	{
		g2.setColor(arc1.getColor());
		g2.drawArc(vertex.getX()- sizeT / 2, vertex.getY()- sizeT / 2, sizeT, sizeT, 0, 90);
		
		g2.setColor(arc2.getColor());
		g2.drawArc(vertex.getX()- sizeT / 2, vertex.getY()- sizeT / 2, sizeT, sizeT, 90, 90);
		
		g2.setColor(arc3.getColor());
		g2.drawArc(vertex.getX()- sizeT / 2, vertex.getY()- sizeT / 2, sizeT, sizeT, 180, 90);
		
		g2.setColor(arc4.getColor());
		g2.drawArc(vertex.getX()- sizeT / 2, vertex.getY()- sizeT / 2, sizeT, sizeT, 270, 90);
		//if(direction == 1){
		//	g2.setColor(Color.blue);
		//}else{
		//	g2.setColor(Color.red);
		//}
		
		//original
		//g2.setColor(Color.black);
		//g2.draw(new Ellipse2D.Double(vertex.geti() - sizeT / 2, vertex.getj() - sizeT / 2, sizeT, sizeT));
	}
}
