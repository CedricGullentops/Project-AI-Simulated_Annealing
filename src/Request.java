import java.util.ArrayList;

public class Request {
	private int id;
	private int zone;
	private int pen1;
	private int pen2;	
	private ArrayList<Integer> cars;
	
	public Request(int x,int y,int p1,int p2) {
		id = x;		
		cars = new ArrayList<Integer>();
		zone = y;
		pen1 = p1;
		pen2 = p2;
	}
	
	public void addCar(int x) {
		cars.add(x);
	}
	
	public ArrayList<Integer> getCars(){
		return cars;
	}
	
	public int getZone() {
		return zone;
	}
	
	public int getP1() {
		return pen1;
	}
	
	public int getP2() {
		return pen2;
	}
	
	public int getId() {
		return id;
	}
}
