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
	private int start_i,start;
	private int counter;
	private int MAXITER;
	private Random random = new Random();
	
	public AnnealLoop(int id,ArrayList<Request> r,ArrayList<Zone> z,ArrayList<Car> c, OverlapMatrix m, int nNeighbours, int start,int M,long SEED) {
		this.random.setSeed(SEED);
		start_i = start;
		this.nNeighbours = nNeighbours;
		MAXITER = M;
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
		start = start_i;
		int delta;
		counter = 0;
		while(run) {
			if (random.nextFloat()<(100-100.0*(float)counter/MAXITER)/100.0){
				carbool = true;
			}
			else{
				carbool = false;
			}
			solution.mutate(cars, zones, requests, carbool, random.nextInt(nNeighbours)+1,random);
			delta = solution.getCost() - solution_best.getCost();
			if(delta < 0) {
				solution_best.copySolution(solution);
				if (solution_best_glob.getCost() > solution_best.getCost()){
					solution_best_glob.copySolution(solution_best);
					counter = 0;
					start *= 0.85;
					System.out.println("New best "+tid+":" +solution_best.getCost());
				}
			}
			else {
				counter++;
				if(random.nextFloat() >= 1-Math.exp(-delta/(float)start)) {
					solution_best.copySolution(solution);
				}
			}
			if(counter > MAXITER) {
				//solution.copySolution(solution_init);
				//solution_best.copySolution(solution_init);
				start *= 0.85;
				System.out.println("start value: " + start);
				counter = 0;
			}
			else {
				solution.copySolution(solution_best);
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
		return solution_best_glob;
	}

}