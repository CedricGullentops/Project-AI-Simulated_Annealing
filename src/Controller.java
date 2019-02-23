import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Controller {
	ArrayList<Request> requests;
	ArrayList<Zone> zones;
	ArrayList<Car> cars;
	
	public Controller() {
		requests = new ArrayList<Request>();
		cars = new ArrayList<Car>();
		zones = new ArrayList<Zone>();
	}
	public static void main(String [] args) {
		FileInputStream fr;
		int line;
		
		try {
			fr = new FileInputStream("examples/100_5_14_25.csv");
			while((line = fr.read()) != -1) {
				System.out.print((char)line);
				if((char)line == ';') {
					System.out.println();
				}
			}
			
		} catch(Exception e){System.out.println("Error: file not found");}
	}
}
