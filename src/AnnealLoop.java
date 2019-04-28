import java.util.ArrayList;
import java.util.Random;

public class AnnealLoop extends Thread{
	private Solution solution, solution_best, solution_best_glob;
	private Solution solution_init = new Solution();
	private ArrayList<Request> requests = new ArrayList<Request>();
	private ArrayList<Zone> zones = new ArrayList<Zone>();
	private ArrayList<Car> cars = new ArrayList<Car>();
	private OverlapMatrix matrix;
	private boolean carbool;
	private int nNeighbours = 3;
	private boolean run=false;
	private int tid;
	private int start = 100;
	
	public AnnealLoop(int id,ArrayList<Request> r,ArrayList<Zone> z,ArrayList<Car> c, OverlapMatrix m, int nNeighbours, int start) {
		this.start = start;
		this.nNeighbours = nNeighbours;
		tid = id+1;
		matrix = m;
		requests = r;
		zones = z;
		cars = c;
		solution_init.generateInitial(requests, matrix, zones,cars);
		solution = new Solution(solution_init);
		solution_best = new Solution(solution_init);
		solution_best_glob = solution_init;
	}
	
	public void run() {
		int delta;
		Random random = new Random();
		while(run) {
			if (random.nextFloat() < 70.0/100.0){
				carbool = true;
			}
			else{
				carbool = false;
			}
			solution.mutate(cars, zones, requests, carbool, nNeighbours);
			delta = solution.getCost() - solution_best.getCost();
			if(delta < 0) {
				solution_best.copySolution(solution);
				if (solution_best_glob.getCost() > solution_best.getCost()){
					solution_best_glob.copySolution(solution_best);
				}
			}
			else {
				if(Math.random() >= 1-Math.exp(-delta/start*1.0)) {
					solution_best.copySolution(solution);
				}
			}
			solution.copySolution(solution_best);
		}
	}
	
	public void startLoop() {
		run = true;
		this.start();
		System.out.println("Thread "+ tid + ": started.");
	}
	
	public void setCarbool() {
		carbool = false;
	}

	public Solution stopLoop() {
		run = false;
		return solution_best_glob;
	}

}