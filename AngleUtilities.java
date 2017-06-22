
public class AngleUtilities {
	
	public static double coterminal(int a){
		if(a >= 360){
			return a - 360;
		}
		
		if(a < 0){
			return a + 360;
		}
		
		return a;
	}
}
