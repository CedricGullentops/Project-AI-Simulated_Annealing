import java.util.ArrayList;

public class Request {
	private int id;
	private int zone;
	private int pen1;
	private int pen2;	
	private ArrayList<Integer> cars = new ArrayList<Integer>();
	private int startday;
	private int startminute;
	private int duration;

	public Request() {	
	}
	
	public void addCar(int x) {
		cars.add(x);
	}
	
	public void setZone(int zone){
		this.zone = zone;
	}
	
	public void setFirstPenalty(int penalty){
		this.pen1 = penalty;
	}
	
	public void setSecondPenalty(int penalty){
		this.pen2 = penalty;
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
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public int getStartday() {
		return startday;
	}

	public void setStartday(int startday) {
		this.startday = startday;
	}

	public int getStartminute() {
		return startminute;
	}

	public void setStartminute(int startminute) {
		this.startminute = startminute;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
