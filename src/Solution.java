import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class Solution {
	private int cost;
	private ArrayList<int[]> Vehicle_assignments;
	private ArrayList<int[]> Assigned_Requests;
	private ArrayList<int[]> Unassigned_Requests;
	private ArrayList<Request> req_temp;
	private OverlapMatrix matrix;
	/**
	 * Constructor
	 */
	public Solution(){
		Vehicle_assignments = new ArrayList<int[]>(); 	// 0 = car, 1 = zone
		Assigned_Requests = new ArrayList<int[]>();		// 0 = id, 1 = car, 2 = (0 if car is in zone, 1 if car is in neighbouring zone)
		Unassigned_Requests = new ArrayList<int[]>();	// 0 = id
	}
	
	public Solution(Solution another){
		this.cost = another.cost;
		this.Vehicle_assignments = new ArrayList<int[]>(another.Vehicle_assignments);
		this.Assigned_Requests =  new ArrayList<int[]>(another.Assigned_Requests);
		this.Unassigned_Requests = new ArrayList<int[]>(another.Unassigned_Requests);
		this.req_temp =  new ArrayList<Request>(another.req_temp);
		this.matrix = another.matrix;
	}
	
	public void copySolution(Solution another){
		this.cost = another.cost;
		this.Vehicle_assignments = new ArrayList<int[]>(another.Vehicle_assignments);
		this.Assigned_Requests =  new ArrayList<int[]>(another.Assigned_Requests);
		this.Unassigned_Requests = new ArrayList<int[]>(another.Unassigned_Requests);
		this.req_temp =  new ArrayList<Request>(another.req_temp);
		this.matrix = another.matrix;
	}
	
	/**
	 * Generate an initial solution
	 */
	public void generateInitial(ArrayList<Request> requests, OverlapMatrix matrix, ArrayList<Zone> zones, ArrayList<Car> cars)
	{
		// variables used in function
		this.matrix = matrix;
		int i, j, k, l, m;		
		int carZone;
		Integer currentCar;
		boolean possible, carPossible;
		Request currentRequest;
		int[] currentAssignedRequest, currentAssignedVehicle, assignment, unassignment, unassignedVehicles;
		assignment = new int[2];
		unassignment = new int[1];
		req_temp = new ArrayList<Request>(requests);
		zones = new ArrayList<Zone>(zones); // we assume zones are ordened !!!!!!!!!!!!!!!
		// print for starting initial solution
		// set requests
		// add all vehicles to the unassigned vehicles list
		// iterate over vehicles
		unassignedVehicles = new int[cars.size()];
		for (m = 0; m < cars.size(); m++)
		{
			unassignedVehicles[m] = cars.get(m).getId();
		}
			
		// iterate over each element in requests
		for (i = 0 ; i < requests.size() ; i++)
		{
		//for (i = 0 ; i < 10 ; i++)
		//{
			possible = false;
			currentRequest = requests.get(i);
			//System.out.println("Request number " + Integer.toString(currentRequest.getId()));
			// iterate over each car in current request
			for (k = 0 ; k < currentRequest.getCars().size() ; k ++)
			{
				// if previous car was an option got to next request
				if (possible)
				{
					continue;
				}
				carPossible = true;
				currentCar = currentRequest.getCars().get(k);
				//System.out.println("Car number " + currentCar);
				// iterate over each assigned request
				for (j = 0 ; j < this.getAssigned_Requests().size() ; j++)
				{
					currentAssignedRequest = this.getAssigned_Requests().get(j);
					// check if request j uses this car
					if(currentAssignedRequest[1] == currentCar)
					{
						// check if current request and assigned request overlap in time
						//System.out.println("Current request " + currentRequest.getId() + " assigned request " + currentAssignedRequest[0]);
						if (matrix.get(currentRequest.getId(), currentAssignedRequest[0]))
						{
							//System.out.println("car not possible");
							carPossible = false;
							break;
						}
					}
				}
				// if car is possible and car has no zone or is in neighbouring zone assign car to request
				if(carPossible)
				{
					carZone = -1;
					// iterate over assigned vehicles
					for (l = 0 ; l < this.getVehicle_assignments().size() ; l++)
					{
						currentAssignedVehicle = this.getVehicle_assignments().get(l);
						// check if this is the vehicle in question
						if(currentCar == currentAssignedVehicle[0])
						{
							carZone = currentAssignedVehicle[1];
							break;
						}
					}
					// if car has no zone yet assign the requests zone and assign car to request
					if(carZone == -1)
					{
						//System.out.println("Car number " + currentCar + " was possible for request number " + currentRequest.getId());
						// assign zone to car
						assignment = new int[2];
						assignment[0] = currentCar;
						assignment[1] = currentRequest.getZone();
						//System.out.println("Assigning car " + assignment[0] + " to zone " + assignment[1]);
						this.getVehicle_assignments().add(assignment);
						// set cars location in unassigned vehicle list to -1
						unassignedVehicles[currentCar] = -1;
						// assign car to request
						assignment = new int[3];
						assignment[0] = currentRequest.getId();
						assignment[1] = currentCar;
						assignment[2] = 0;
						//System.out.println("Assigning car " + assignment[1] + " to request " + assignment[0]);
						this.getAssigned_Requests().add(assignment);
						// set possible to true and continue to next request
						possible = true;
						continue;
					}
					else
					{
						// if cars zone is impossible continue to next car
						// if (Math.abs(currentRequest.getZone() - carZone) > 1)
						if (zones.get(currentRequest.getZone()).isNeighbour(carZone) == 0)
						{
							continue;
						}
						// if cars zone is possible assign car to request and continue to next request
						else
						{
							//System.out.println("Car number " + currentCar + " was possible for request number " + currentRequest.getId());
							assignment = new int[3];
							assignment[0] = currentRequest.getId();
							assignment[1] = currentCar;
							assignment[2] = 0;
							// if cars zone is next to requests zone
							if (currentRequest.getZone() != carZone)
							{
								cost += currentRequest.getP2();
								assignment[2] = 1;
							}
							//System.out.println("Assigning car " + assignment[1] + " to request " + assignment[0] + " is in zone " + assignment[2]);
							this.getAssigned_Requests().add(assignment);
							// set possible to true and continue to next request
							possible = true;
							continue;
						}
					}
				}
			}
			// if request is not possible add to unassigned and add cost
			if(!possible)
			{
				unassignment = new int[1];
				unassignment[0] = currentRequest.getId();
				cost += currentRequest.getP1();
				//System.out.println("Add request " + unassignment[0] + " to unassigned requests");
				this.getUnassigned_Requests().add(unassignment);
			}
		}
		// check if every car has received a zone
		// iterate over unassigned vehicles
		for (m = 0; m < unassignedVehicles.length; m++)
		{
			if (unassignedVehicles[m] != -1)
			{
				// assign zone to car
				assignment = new int[2];
				assignment[0] = m;
				assignment[1] = 0;
				this.getVehicle_assignments().add(assignment);
			}
		}
		this.calculateCost();
		System.out.println("Initial cost: " + this.getCost());
	}
	
	
	/**
	 * Mutation of solution
	 * input ncars: amount of cars
	 * input car: true if mutating car, false if mutating request
	 * input step_amount: amount of mutations
	 * if mutating car: unassign all requests to this car and assign now possible unassigned requests.
	 * if mutating request: 
	 */
	public void mutate(ArrayList<Car> cars, ArrayList<Zone> zones, ArrayList<Request> requests, boolean car, int step_amount, Random random)
	{
		int nzones = zones.size();
		int ncars = cars.size();

		//Mutate a number of cars
		if (car){
			//Create a list of unique random cars to unassign
			ArrayList<Integer> freecars = new ArrayList<Integer>();
			for (int i=0; i<step_amount; i++){	
				int randomcar;
				do {
		        	randomcar = random.nextInt(ncars);
		       }while (freecars.contains(randomcar));
		        freecars.add(randomcar);
		        //Change the car's zone
		        for (int k=0; k<Vehicle_assignments.size(); k++){
		        	if (Vehicle_assignments.get(k)[0] == randomcar){
		        		Vehicle_assignments.get(k)[1] = random.nextInt(nzones);
		        		break;
		        	}
		        }
		        //Add the requests with that car to unassigned and remove from assigned
		        ArrayList<Integer> toRemove = new ArrayList<Integer>(); 
		        for (int j=0; j<Assigned_Requests.size(); j++){
		        	if (Assigned_Requests.get(j)[1] == randomcar){
		        		int[] assignment = new int[1];
						assignment[0] = Assigned_Requests.get(j)[0];
		        		Unassigned_Requests.add(assignment);
		        		toRemove.add(Assigned_Requests.get(j)[0]);
		        		System.out.println("\t to be removed: " + Assigned_Requests.get(j)[0]);
		        	}
		        }
		        for (int j=toRemove.size()-1; j>=0; j--){
		        	for (int k = Assigned_Requests.size()-1; k >= 0 ; k--){
		        		if (toRemove.get(j) == Assigned_Requests.get(k)[0]){
		        			int rm = Assigned_Requests.get(k)[0];
		        			this.Assigned_Requests.remove(k);
		        			System.out.println("\t\t removed: " + rm);
		        			System.out.println("\t\t\t size: " + Assigned_Requests.size());
		        			break;
		        		}
		        	}
		        }		        
			}
			
			//Run through the Unassigned list and try to assign an available car to each unassigned request.
			ArrayList<Integer> toRemove =  new ArrayList<Integer>();
	        for (int l=0; l<Unassigned_Requests.size(); l++){
	        	boolean breakfree = false;
	        	for (int m=0; m<cars.size(); m++){
	        		//int zoneid = -1;
	        		//If a free car is listed in the requests possible car list check if it is in a neighbouring zone and assign it.
	        		if (requests.get(Unassigned_Requests.get(l)[0]).getCars().contains(cars.get(m).getId())){
	        			int zoneid = -1;
	        			//Todo: Als lijst altijd gesorteed is is het niet nodig om deze te doorlopen
	        			for (int i=0; i<Vehicle_assignments.size(); i++){
	        				if (Vehicle_assignments.get(i)[0] == cars.get(m).getId()){
	        					zoneid = Vehicle_assignments.get(i)[1];
	        				}
	        			}
	        			if (zoneid == -1){
	        				continue;
	        			}
	        			//int neighbour = 0;
	        			int neighbour = zones.get(requests.get(Unassigned_Requests.get(l)[0]).getZone()).isNeighbour(zoneid);
	        			if (neighbour != 0){
	        				boolean overlaps = false;
	        				for (int j = 0; j < Assigned_Requests.size(); j++){
	        					if (Assigned_Requests.get(j)[1] == cars.get(m).getId()){
		        					if (matrix.get(Assigned_Requests.get(j)[0], Unassigned_Requests.get(l)[0])){
		        						overlaps = true;
		        						break;
		        					}
	        					}
	        				}
	        				if (!overlaps){
	        					int[] new_assigned = {Unassigned_Requests.get(l)[0], cars.get(m).getId(), neighbour-1};
    	        				Assigned_Requests.add(new_assigned);
    	        				System.out.println("Added: " + new_assigned[0]);
    	        				toRemove.add(Unassigned_Requests.get(l)[0]);
    	        				breakfree = true;
	        				}
	        			}
	        		}
	        		if (breakfree){
	        			break;
	        		}
	        	}
	        }
	        for (int i=toRemove.size()-1; i>=0; i--){
	        	for (int j = 0; j<Unassigned_Requests.size(); j++){
	        		if (toRemove.get(i) == Unassigned_Requests.get(j)[0]){
	        			Unassigned_Requests.remove(j);
	        			break;
	        		}
	        	}
	        }
	        /*
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< Assigned_Requests.size();i++) {
	            sb.append("req");
	            sb.append(Assigned_Requests.get(i)[0]);
	            sb.append(";");
	            sb.append("car");
	            sb.append(Assigned_Requests.get(i)[1]);
	            sb.append("\n");
	        }
	        System.out.println(sb);
	        */
		}
		
		//Mutate a number of requests
		else{
			ArrayList<Integer> New_Unassigned = new ArrayList<Integer>();
			ArrayList<Integer> freecars = new ArrayList<Integer>();
			for (int i=0; i<step_amount; i++){	
				int randomrequest;
				do {
					randomrequest = random.nextInt(Assigned_Requests.size());
		       } while (New_Unassigned.contains(randomrequest));
				New_Unassigned.add(randomrequest);
				//Add the new unassigned requests to unassigned, remove from assigned and put the freed car in a list
		        for (int j=0; j<Assigned_Requests.size(); j++){
		        	if (Assigned_Requests.get(j)[0] == randomrequest){
		        		int[] newrequest = new int[1];
		        		newrequest[0] = randomrequest;
		        		Unassigned_Requests.add(newrequest);
		        		freecars.add(Assigned_Requests.get(j)[1]);
		        		Assigned_Requests.remove(j);
		        	}
		        }
			}
			//Run through the Unassigned list and try to assign an available car to each unassigned request.
			ArrayList<Integer> toRemove =  new ArrayList<Integer>();
			for (int l=0; l<Unassigned_Requests.size(); l++){
				boolean breakfree = false;
	        	for (int m=0; m<cars.size(); m++){
	        		//If a free car is listed in the requests possible car list check if it is in a neighbouring zone and assign it.
	        		if (requests.get(Unassigned_Requests.get(l)[0]).getCars().contains(cars.get(m).getId())){
	        			int zoneid = -1;
	        			//Todo: Als lijst altijd gesorteed is is het niet nodig om deze te doorlopen
	        			for (int i=0; i<Vehicle_assignments.size(); i++){
	        				if (Vehicle_assignments.get(i)[0] == cars.get(m).getId()){
	        					zoneid = Vehicle_assignments.get(i)[1];
	        				}
	        			}
	        			if (zoneid == -1){
	        				continue;
	        			}
	        			int neighbour = zones.get(requests.get(Unassigned_Requests.get(l)[0]).getZone()).isNeighbour(zoneid);
	        			if (neighbour != 0){
	        				boolean overlaps = false;
	        				for (int j = 0; j < Assigned_Requests.size(); j++){
	        					if (Assigned_Requests.get(j)[1] == cars.get(m).getId()){
		        					if (matrix.get(Assigned_Requests.get(j)[0], Unassigned_Requests.get(l)[0])){
		        						overlaps = true;
		        						break;
		        					}
	        					}
	        				}
	        				if (!overlaps){
	        					int[] new_assigned = {Unassigned_Requests.get(l)[0], cars.get(m).getId(), neighbour-1};
    	        				Assigned_Requests.add(new_assigned);
    	        				toRemove.add(Unassigned_Requests.get(l)[0]);
    	        				breakfree = true;
	        				}
	        			}
	        		}
	        		if (breakfree){
	        			break;
	        		}
	        	}
	        }
			for (int i=toRemove.size()-1; i>=0; i--){
	        	for (int j = Unassigned_Requests.size()-1; j >= 0; j--){
	        		if (toRemove.get(i) == Unassigned_Requests.get(j)[0]){
	        			Unassigned_Requests.remove(j);
	        		}
	        	}
	        }
		}
		calculateCost();
	}
	
	public void printCSV() {
		try (PrintWriter writer = new PrintWriter(new File("solution.csv"))) {
			
			System.out.println("Writing output file...");
			
			System.out.println("Final solution cost: "+ cost);
			System.out.println(Vehicle_assignments.size() + " vehicle assignments");
			System.out.println(Assigned_Requests.size() + " request assignments");
			System.out.println(Unassigned_Requests.size() + " request unassignments");
			
		    StringBuilder sb = new StringBuilder();
		    sb.append(cost);
		    sb.append("\n");
		    sb.append("+Vehicle assignments\n");
		    for(int i=0; i< Vehicle_assignments.size();i++) {
		    	sb.append("car");
		    	sb.append(Vehicle_assignments.get(i)[0]);
		    	sb.append(";");
		    	sb.append("z");
		    	sb.append(Vehicle_assignments.get(i)[1]);
		    	sb.append("\n");
		    }
		    sb.append("+Assigned requests\n");
		    for(int i=0; i< Assigned_Requests.size();i++) {
		    	sb.append("req");
		    	sb.append(Assigned_Requests.get(i)[0]);
		    	sb.append(";");
		    	sb.append("car");
		    	sb.append(Assigned_Requests.get(i)[1]);
		    	sb.append("\n");
		    }
		    sb.append("+Unassigned requests\n");
		    for(int i=0; i< Unassigned_Requests.size();i++) {
		    	sb.append("req");
		    	sb.append(Unassigned_Requests.get(i)[0]);
		    	sb.append("\n");
		    }	

		    writer.write(sb.toString());
		    writer.close();

		    System.out.println("Output file written!");

		    } catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
		    }
	}
	
	/**
	 * calculate the cost of a solution
	 */
	public void calculateCost()
	{
		int i, j;
		int cost_tmp = 0;
		// iterate over assigned requests
		for (i = 0; i < Assigned_Requests.size(); i++)
		{
			if (Assigned_Requests.get(i)[2] == 1)
			{
				cost_tmp += req_temp.get(Assigned_Requests.get(i)[0]).getP2();
			}
		}
		// iterate over assigned requests
		for (j = 0; j < Unassigned_Requests.size(); j++)
		{
			cost_tmp += req_temp.get(Unassigned_Requests.get(j)[0]).getP1();
		}
		cost = cost_tmp;
	}
	
	/**
	 * Getters and setters (Automatically generated)
	 */
	public int getCost() {
		return cost;
	}

	public ArrayList<int[]> getVehicle_assignments() {
		return Vehicle_assignments;
	}

	public ArrayList<int[]> getAssigned_Requests() {
		return Assigned_Requests;
	}

	public ArrayList<int[]> getUnassigned_Requests() {
		return Unassigned_Requests;
	}
	
	public ArrayList<Request> getReq_temp() {
		return req_temp;
	}
}
