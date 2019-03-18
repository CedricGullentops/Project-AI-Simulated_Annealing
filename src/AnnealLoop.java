import java.util.ArrayList;

public class AnnealLoop extends Thread{
	
	private Solution solution,solution_best;
	private static ArrayList<Request> requests = new ArrayList<Request>();
	private static ArrayList<Zone> zones = new ArrayList<Zone>();
	private static ArrayList<Car> cars = new ArrayList<Car>();
	private static OverlapMatrix matrix;
	boolean carbool;
	private static double mseconds = 0;
	private static int nMinutes = 5;
	private static int nNeighbours = 3;
	
	public AnnealLoop(ArrayList<Request> r,ArrayList<Zone> z,ArrayList<Car> c, OverlapMatrix m) {
		matrix = m;
		requests = r;
		zones = z;
		cars = c;
		solution = new Solution();
		solution.generateInitial(requests, matrix, zones,cars);
		solution_best = solution;
	}
	
	public void run() {
		int delta,n;
		int start = 100;
		while(true) {
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
		}
	}
	
	public void startLoop() {
		this.start();
	}
}