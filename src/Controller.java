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
		int line, counter = 0,ccounter=0;
		String input = "";
		try {
			fr = new FileInputStream("examples/100_5_14_25.csv");
			while((line = fr.read()) != -1) {				
				if((char)line == ',') {
					System.out.println(counter + ". " + input);
					input = ""; 
				}
				else if((char)line == ';') {
					System.out.println(counter + ". " + input);
					input = ""; 
					counter++;
				}
				else if((char)line == '\n') {
					System.out.println(counter + ". " + input);
					input = "";
					counter = 0;
				}
				else if((char)line == '+') {
					while((line = fr.read()) != '\n') {
						input += (char)line;
						if((char)line == ':') {
							input = "";
						}
					}
					System.out.println(ccounter + ". " + input);
					ccounter++;
					input = "";
				}
				else {
					if(48 <= line && line <= 57) {
						input += (char)line;
					}
				}
			}
			
		} catch(Exception e){System.out.println("Error: file not found");}
	}
}
