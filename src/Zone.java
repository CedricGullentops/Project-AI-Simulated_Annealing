import java.util.ArrayList;

public class Zone {
	private int id;
	private ArrayList<Integer> nextto =  new ArrayList<Integer>();
	
	public Zone(){
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
	
	public int isNeighbour(int zone)
	{
		if(this.id == zone)
		{
			return 1;
		}
		for(int i = 0 ; i < nextto.size() ; i++)
		{
			if(nextto.get(i) == zone)
			{
				return 2;
			}
		}
		return 0;
	}
}
