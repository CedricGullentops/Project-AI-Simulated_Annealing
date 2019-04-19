import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
	private static ArrayList<Request> requests = new ArrayList<Request>();
	private static ArrayList<Zone> zones = new ArrayList<Zone>();
	private static ArrayList<Car> cars = new ArrayList<Car>();
	private static Request rq;
	private static Zone zone;
	private static Car car;
	private static OverlapMatrix matrix;
	private static Solution solution,solution_best,solution_best_glob,solution_init;
	private static int runtime_min = 1;
	private static int nNeighbours = 3;
	static boolean carbool = false;
	private static int tcount = 4;
	private final static int SHORTMODE = 0;
	private final static Clock C = new Clock();
	static int delta;
	static int start = 100;
	public static int carboolcount = 0;
	
	public static void main(String [] args) {
		FileInputStream fr;
		int line, counter = 0,ccounter=-1;
		String input = "";
		
			try {
				fr = new FileInputStream("examples/100_5_14_25.csv");
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
			simAnnealing();
			//solution = new Solution();
			//solution.generateInitial(requests, matrix, zones, cars);
			System.out.println("Stopped algorithm @ "+ C.displayTime());
			solution.printCSV();
			System.out.println("Program exitted @ "+ C.displayTime());
			System.exit(0);			
	}
	
	private static void simAnnealing(){
		int runtime_ms = runtime_min*60000/(1+SHORTMODE*99);
		ArrayList<AnnealLoop> threads = new ArrayList<AnnealLoop>();		
		Solution temp_sol;
		if(tcount > 1)
		{
			for(int i=0;i < tcount-1;i++) {
				threads.add(new AnnealLoop(i,requests,zones,cars,matrix));
				threads.get(i).startLoop();
			}
		}	
		System.out.println("Number of active threads: " + java.lang.Thread.activeCount());
		
		solution_init = new Solution();
		solution_init.generateInitial(requests, matrix, zones,cars);
		solution = solution_init;
		solution_best = solution_init;
		solution_best_glob = solution_init;
		int nmutations = 1;
		while(true) {
			if(C.cTime() >= runtime_ms) {
				if(tcount > 1)
				{
					for(int i=0;i < tcount-1;i++) {
						temp_sol = threads.get(i).stopLoop();
						if(temp_sol.getCost() <= solution_best_glob.getCost()) {
							solution_best_glob = temp_sol;
						}
					}
				}
				break;
			}
			simLoop();	
			//if (nmutations >= 100){
			//	break;
			//}
			nmutations++;
		}
		System.out.println("Did " + Integer.toString(nmutations) + " mutations.");
		solution = solution_best_glob;
	}
	
	static public void simLoop() {
		Random random = new Random();
		if (random.nextFloat() < 70.0/100.0){
			carbool = true;
			carboolcount++;
		}
		else{
			carbool = false;
		}
		solution.mutate(cars, zones, requests, carbool, nNeighbours);
		delta = solution.getCost() - solution_best.getCost();
		if(delta < 0) {
			solution_best = solution;
			solution_best_glob = solution;
		}
		else {
			if(Math.random() >= Math.exp(-delta/start*1.0)) {
				solution_best = solution;
			}
		}
		
	}

	static private void processInputData(int ccounter,int counter,int input){
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
	
	static public void createOverlapMatrix(){
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
	
	static private boolean overlaps(OverlapMatrix matrix, int col, int row, int n){
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
