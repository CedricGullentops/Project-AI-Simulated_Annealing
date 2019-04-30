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
	private int start_i,start,end, runtime_min;
	private int counter;
	private int MAXITER = 1000;
	private Random random = new Random();
	private double koeling;
	private final PlotData plot;
	private final Clock C;
	
	public AnnealLoop(int id,ArrayList<Request> r,ArrayList<Zone> z,ArrayList<Car> c, OverlapMatrix m, int nNeighbours, int start,int M,long SEED, double koeling, int threadcount, int end, int runtime_min, Clock clock) {
		this.C = clock;
		this.plot = new PlotData("Score vs Time: thread " + Integer.toString(threadcount));
		this.koeling = koeling;
		this.random.setSeed(SEED);
		this.start = start;
		this.end = end;
		this.nNeighbours = nNeighbours;
		this.runtime_min = runtime_min;
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
		int delta;
		counter = 0;
		int nmutations = 0;
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
					plot.addDataPoint(solution_best_glob.getCost(), C.cTime());
				}
			}
			else {
				if(random.nextFloat() >= 1-Math.exp(-delta/(float)start)) {
					solution_best.copySolution(solution);
				}
				else
				{
					solution.copySolution(solution_best);
				}
			}
			counter++;
			if(counter > MAXITER) {
				//solution.copySolution(solution_init);
				//solution_best.copySolution(solution_init);
				start *= koeling;
				System.out.println("start value: " + start);
				counter = 0;
			}
			nmutations++;
			if (nmutations % MAXITER == 0) {
				//System.out.println("ctime: " + C.cTime());
				float timeleft = runtime_min * 60 * 1000 - C.cTime();
				//System.out.println("timeleft: " + timeleft);
				float ratio = nmutations/C.cTime();
				//System.out.println("ratio: " + ratio);
				float mutationsleft = ratio * timeleft;
				//System.out.println("mutationsleft: " + mutationsleft);
				float templeft = start - end;
				MAXITER = (int) (mutationsleft / templeft);
				System.out.println("\t MAXITER changed to: " + MAXITER);
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
		plot.createPlot();
		return solution_best_glob;
	}

}