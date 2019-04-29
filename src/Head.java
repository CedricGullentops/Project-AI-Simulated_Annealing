import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Head {
	private ArrayList<Request> requests = new ArrayList<Request>();
	private ArrayList<Zone> zones = new ArrayList<Zone>();
	private ArrayList<Car> cars = new ArrayList<Car>();
	private Request rq;
	private Zone zone;
	private Car car;
	private OverlapMatrix matrix;
	private Solution solution, solution_best, solution_best_glob;
	private Solution solution_init = new Solution();
	private int runtime_min = 1;
	private int nNeighbours = 1;
	boolean carbool = false;
	private int tcount = 1;
	private final static int SHORTMODE = 1;
	private final static Clock C = new Clock();
	int delta;
	int start = 2000;
	private boolean toPlot = true;
	private final PlotData plot = new PlotData("Score vs Time");
	private int counter=0;
	private final int MAXITER = 25000;
	private Random random;
	private long SEED = 10;
	private double KOELING = 0.97;
	
	public void runProgram(){
		random = new Random();
		random.setSeed(SEED);
		FileInputStream fr;
		int line, counter = 0,ccounter=-1;
		String input = "";
		
			try {
				fr = new FileInputStream("examples/210_5_33_25.csv");
				while((line = fr.read()) != -1) {				
					if((char)line == ',') {
						//System.out.println(ccounter + " . " + counter + ". " + input);
						processInputData(ccounter, counter, Integer.parseInt(input));
						input = ""; 
					}
					else if((char)line == ';') {
						//System.out.println(ccounter + " . " + counter + ". " + input);
						processInputData(ccounter, counter, Integer.parseInt(input));
						input = ""; 
						counter++;
					}
					else if((char)line == '\n') {
						//System.out.println(ccounter + " . " + counter + ". " + input);
						processInputData(ccounter, counter, Integer.parseInt(input));
						input = "";
						counter = 0;
					}
					else if((char)line == '+') {
						ccounter++;
						while((line = fr.read()) != '\n') {
							input += (char)line;
							if((char)line == ':') {
								input = "";
							}
						}
						//System.out.println(ccounter + " . " + counter + ". " + input);
						input = "";
					}
					else {
						if(48 <= line && line <= 57) {
							input += (char)line;
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			createOverlapMatrix();
			System.out.println("Overlap matrix generated @ " + C.displayTime());
			//solution = new Solution();
			//solution.generateInitial(requests, matrix, zones, cars);
			simAnnealing();
			System.out.println("Stopped algorithm @ "+ C.displayTime());
			System.out.println("solution cost: " + solution_best_glob.getCost());
			System.out.println("\t\t\t final size: " + solution_best_glob.getAssigned_Requests().size());
			solution.printCSV();
			System.out.println("Program exitted @ "+ C.displayTime());
			if (toPlot){
				plot.createPlot();
			}
			else{
				System.exit(0);			
			}		
	}
	
	private void simAnnealing(){
		int runtime_ms = runtime_min*60000/(1+SHORTMODE*99);
		ArrayList<AnnealLoop> threads = new ArrayList<AnnealLoop>();		
		Solution temp_sol = new Solution();
		if(tcount > 1)
		{
			for(int i=0;i < tcount-1;i++) {
				threads.add(new AnnealLoop(i,requests,zones,cars,matrix,nNeighbours,start,MAXITER,SEED+i,KOELING,i));
				threads.get(i).startLoop();
			}
		}	
		System.out.println("Number of active threads: " + java.lang.Thread.activeCount());
		
		solution_init = new Solution();
		solution_init.generateInitial(requests, matrix, zones,cars);
		solution = new Solution(solution_init);
		solution_best = new Solution(solution_init);
		solution_best_glob = new Solution(solution_init);
		int nmutations = 1;
		while(true) {
			if(nmutations > 8/*C.cTime() >= runtime_ms*/) {
				System.out.println("Main thread cost: " + Integer.toString(solution_best_glob.getCost()));
				if(tcount > 1)
				{
					for(int i=0;i < tcount-1;i++) {
						temp_sol.copySolution(threads.get(i).stopLoop());
						System.out.println("Thread " + Integer.toString(i) + " cost: " + Integer.toString(temp_sol.getCost()));
						temp_sol.calculateCost();
						System.out.println("temp sol solution cost recalculated: " + solution.getCost());
						
						if(temp_sol.getCost() < solution_best_glob.getCost()) {
							solution_best_glob.copySolution(temp_sol);
						}
					}
				}
				break;
			}
			simLoop();	
			System.out.println("Main thread did " + Integer.toString(nmutations) + " mutations.");
			nmutations++;
		}
		System.out.println("Main thread did " + Integer.toString(nmutations) + " mutations.");
		System.out.println("before recalc: " + solution_best_glob.getCost());
		solution_best_glob.calculateCost();
		solution_best_glob.printCSV();
		System.out.println("\t\t\t this time size: " + solution_best_glob.getAssigned_Requests().size());
		System.out.println("after recalc: " + solution_best_glob.getCost());
	}
	
	public void simLoop() {
		if (true /*random.nextFloat()<(100-100.0*(float)counter/MAXITER)/100.0*/){
			carbool = true;
		}
		else{
			carbool = false;
		}
		solution.mutate(cars, zones, requests, carbool, random.nextInt(nNeighbours)+1,random);
		delta = solution.getCost() - solution_best.getCost();
		System.out.println("delta: " + delta);
		if(delta < 0) {
			System.out.println("Het is beter!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			solution_best.copySolution(solution);
			if (solution_best_glob.getCost() > solution_best.getCost()){
				solution_best_glob.copySolution(solution_best);
				plot.addDataPoint(solution_best_glob.getCost(), C.cTime());
				start *= KOELING;
			}
		}
		else {
			if(random.nextFloat() >= 1-Math.exp(-delta/(float)start)) {
				System.out.println("Het is slechter maar geaccepteerd!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				solution_best.copySolution(solution);
			}
			else
			{
				System.out.println("Het is slechter!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				System.out.println("\t Solution: " + solution.getCost() + " Solution best: " +solution_best.getCost());
				solution.copySolution(solution_best);
				System.out.println("\t Solution: " + solution.getCost() + " Solution best: " +solution_best.getCost());
			}
		}
		counter++;
		if(counter > MAXITER) {
			System.out.println("Maxiter shizzle!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			//solution.copySolution(solution_init);
			//solution_best.copySolution(solution_init);
			start *= KOELING;
			System.out.println("start value: " + start);
			counter = 0;
		}
		
		//System.out.println("before copy, before recalc: " + solution.getCost());
		//solution.calculateCost();
		//System.out.println("before copy, after recalc: " + solution.getCost());
	}

	private void processInputData(int ccounter,int counter,int input){
		switch(ccounter) {
			case 0:
				//Process requests
				switch(counter){
					case 0:
						rq = new Request();
						requests.add(rq);
						rq.setId(input);
						break;
					case 1:
						rq.setZone(input);
						break;
					case 2:
						rq.setStartday(input);
						break;
					case 3:
						rq.setStartminute(input);
						break;
					case 4:
						rq.setDuration(input);
						break;
					case 5:
						rq.addCar(input);
						break;
					case 6:
						rq.setFirstPenalty(input);
						break;
					case 7:
						rq.setSecondPenalty(input);
						break;
					default:
						System.out.println("Invalid counter");
						break;
				}
				break;
			case 1:
				//Process Zones
				switch(counter){
					case 0:
						zone = new Zone();
						zones.add(zone);
						zone.setId(input);
						break;
					case 1:
						zone.addNeighbour(input);
						break;
					default:
						System.out.println("Invalid zone");
						break;
				}
				break;
			case 2:
				//Process Vehicles
				car = new Car(input);
				cars.add(car);
				break;
			default:
				//Invalid ccounter
				System.out.println("Invalid ccounter");
				break;
		}
	} 
	
	public void createOverlapMatrix(){
		int n = requests.size();
		matrix = new OverlapMatrix(n, n);
		for (int col = 0; col < n; col++){
			for (int row = 0; row < n; row++){
				if(col != row && overlaps(matrix, col, row, n) == true){
					matrix.set(row, col, true);
					matrix.set(col, row, true);
				}
				else{
					matrix.set(row, col, false);
					matrix.set(col, row, false);
				}
			}
		}
		//matrix.display();
		try {
			matrix.printOut();
			//System.out.println("Overlapmatrix created in bitmatrix.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean overlaps(OverlapMatrix matrix, int col, int row, int n){
		int rbegin = requests.get(row).getStartday() * 1440 + requests.get(row).getStartminute();		
		int rend = rbegin + requests.get(row).getDuration();		
		int cbegin = requests.get(col).getStartday() * 1440 + requests.get(col).getStartminute();		
		int cend = cbegin + requests.get(col).getDuration();
		if ((cbegin >= rbegin && cbegin <= rend) || (cend >= rbegin && cend <= rend) || (cbegin <= rbegin && cend >= rend)){
			return true;
		}
		return false;
	}
}
