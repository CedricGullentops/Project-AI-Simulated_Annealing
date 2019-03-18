import java.util.ArrayList;

public class AnnealLoop extends Thread{
	
	private Solution solution,solution_best;
	private static ArrayList<Request> requests = new ArrayList<Request>();
	private static ArrayList<Zone> zones = new ArrayList<Zone>();
	private static ArrayList<Car> cars = new ArrayList<Car>();
	private static OverlapMatrix matrix;
	
	public AnnealLoop(ArrayList<Request> r,ArrayList<Zone> z,ArrayList<Car> c, OverlapMatrix m) {
		matrix = m;
		requests = r;
		zones = z;
		cars = c;
		solution = new Solution();
		solution.generateInitial(requests, matrix, zones);
		solution_best = solution;
	}
	
	public void run() {
		while(true) {
			try {
				Thread.sleep((int)(Math.random()*3000*soort));
			} catch (InterruptedException e) {
			}
			synchronized(P) {
			if(soort != 1) {
				while(P.getVrij() < 3) {
					P.wachtOp(false);
					try {P.wait();} 
					catch (InterruptedException e) {
					}
				}
				P.goIn(new Bus());
			}
			else{
				while(P.getVrij() < 1) {
					P.wachtOp(true);
					try {P.wait();} 
					catch (InterruptedException e) {
					}
				}
				P.goIn(new Auto());
			}
			kader.setLab(P.getVrij());
		}}
	}
	
	public void startIn() {
		this.start();
	}
}