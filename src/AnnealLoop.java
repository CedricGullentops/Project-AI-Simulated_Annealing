import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AnnealLoop extends Thread{
	
	private Solution solution,solution_best,solution_best_glob,solution_init;
	private static ArrayList<Request> requests = new ArrayList<Request>();
	private static ArrayList<Zone> zones = new ArrayList<Zone>();
	private static ArrayList<Car> cars = new ArrayList<Car>();
	private static OverlapMatrix matrix;
	private boolean carbool;
	private static int nNeighbours = 3;
	private boolean run=false;
	private int tid;
	private final int MAXITER = 500;
	private long iter_counter = 0;
	private int init_counter = 1;
	
	public AnnealLoop(int id,ArrayList<Request> r,ArrayList<Zone> z,ArrayList<Car> c, OverlapMatrix m) {
		tid = id+1;
		matrix = m;
		requests = r;
		zones = z;
		cars = c;
		solution_init = new Solution();
		solution_init.generateInitial(requests, matrix, zones,cars);
		solution = solution_init;
		solution_best = solution_init;
		solution_best_glob = solution_init;
	}
	
	public void run() {
		int delta;
		int start = 100;
		while(run) {
			solution.mutate(cars, zones, requests, carbool, nNeighbours);	//GIVES ERRORS -> comment out to run without(ctrl + /)
			delta = solution.getCost() - solution_best.getCost();
			if (iter_counter >= MAXITER) {
				init_counter++;
				solution = solution_init;
				solution_best = solution_init;
				continue;
			}
			if(delta < 0) {
				solution_best = solution;
				solution_best_glob = solution;
				iter_counter = 0;
			}
			else {
				iter_counter++;
				if(Math.random() >= Math.exp(-delta/start*1.0)) {
					solution_best = solution;
				}
			}			
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
		System.out.println("Thread "+ tid + ": stopped after "+ iter_counter + " iterations after "+ init_counter +" inits.");
		return solution_best_glob;
	}

}