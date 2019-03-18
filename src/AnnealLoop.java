import java.util.ArrayList;

public class AnnealLoop extends Thread{
	
	private Solution solution,solution_best;
	private static ArrayList<Request> requests = new ArrayList<Request>();
	private static ArrayList<Zone> zones = new ArrayList<Zone>();
	private static ArrayList<Car> cars = new ArrayList<Car>();
	private static OverlapMatrix matrix;
	boolean carbool;
	private static Double mseconds;
	private static int nMinutes = 5;
	private static int nNeighbours = 3;
	
	public AnnealLoop(ArrayList<Request> r,ArrayList<Zone> z,ArrayList<Car> c, OverlapMatrix m,Double ms) {
		matrix = m;
		requests = r;
		zones = z;
		cars = c;
		mseconds = ms;
		solution = new Solution();
		solution.generateInitial(requests, matrix, zones,cars);
		solution_best = solution;
	}
	
	public void run() {
		int delta;
		int start = 100;
		/*
		while(mseconds < nMinutes*100) {
			if (Math.random() > 0.7) {
				carbool = false;
			}
			else {
				carbool = true;
			}
			carbool = true;
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
		*/
		while(mseconds < 5000) {
			System.out.println(mseconds);
		}
		
	}
	
	public void startLoop() {
		this.start();
	}
}