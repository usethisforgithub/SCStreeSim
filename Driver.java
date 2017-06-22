
public class Driver {
	public static void main(String[] args){
		ScreenWindow window = new ScreenWindow();
		new Thread(window).start();
	}
}
