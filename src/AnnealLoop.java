import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AnnealLoop extends Thread{
	
	private Solution solution,solution_best;
	private static ArrayList<Request> requests = new ArrayList<Request>();
	private static ArrayList<Zone> zones = new ArrayList<Zone>();
	private static ArrayList<Car> cars = new ArrayList<Car>();
	private static OverlapMatrix matrix;
	private boolean carbool;
	private static int nNeighbours = 3;
	private boolean run=false,running=false;
	private int tid;
	
	public AnnealLoop(int id,ArrayList<Request> r,ArrayList<Zone> z,ArrayList<Car> c, OverlapMatrix m) {
		
		tid = id+1;
		matrix = m;
		requests = r;
		zones = z;
		cars = c;
		solution = new Solution();
		solution.generateInitial(requests, matrix, zones,cars);
		solution_best = solution;
	}
	
	public void run() {
		int delta;
		int start = 100;
		while(run) {
			
			solution.mutate(cars, zones, requests, carbool, nNeighbours);
			delta = solution.getCost() - solution_best.getCost();
			if(delta <= 0) {
				solution_best = solution;
			}
			else {
				if(Math.random() >= Math.exp(-delta/start*1.0)) {
					solution_best = solution;
				}
			}			
		}
		
		
	}
	
	public void startLoop() {
		run = true;
		running = true;
		this.start();
		System.out.println("Thread "+ tid + ": started.");
	}
	
	public void setCarbool() {
		carbool = false;
	}

	public Solution stopLoop() {
		run = false;
		System.out.println("Thread "+ tid + ": stopped.");
		return solution_best;
	}

}