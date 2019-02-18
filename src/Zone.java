import java.util.ArrayList;

public class Zone {
	private int id;
	private ArrayList<Integer> nextto;
	
	public Zone(int x) {
		id = x;
		nextto = new ArrayList<Integer>();
	}
	
	public void addNeighbour(int x) {
		nextto.add(x);
	}
	
	public ArrayList<Integer> getNeighbours(){
		return nextto;
	}
	
	public void setId(int x) {
		id = x;
	}
	
	public int getId() {
		return id;
	}
}
