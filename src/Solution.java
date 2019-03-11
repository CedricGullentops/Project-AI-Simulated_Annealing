import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Solution {
	private int cost;
	private ArrayList<int[]> Vehicle_assignments;
	private ArrayList<int[]> Assigned_Requests;
	private ArrayList<int[]> Unassigned_Requests;
	private ArrayList<Request> req_temp;
	/**
	 * Constructor
	 */
	public Solution(){
		Vehicle_assignments = new ArrayList<int[]>(); 	// 0 = car, 1 = zone
		Assigned_Requests = new ArrayList<int[]>();		// 0 = id, 1 = car
		Unassigned_Requests = new ArrayList<int[]>();	// 0 = id
	}
	
	
	/**
	 * Generate an initial solution
	 */
	public void GenerateInitial(ArrayList<Request> requests, OverlapMatrix matrix)
	{
		// variables used in function
		int i, j, k, l;
		int carZone;
		Integer currentCar;
		boolean possible, carPossible;
		Request currentRequest;
		int[] currentAssignedRequest, currentAssignedVehicle, assignment, unassignment;
		assignment = new int[2];
		unassignment = new int[1];
		req_temp = requests;
		// print for starting initial solution
		System.out.println("Creating initial solution...");
		// iterate over each element in requests
		for (i = 0 ; i < requests.size() ; i++)
		{
		//for (i = 0 ; i < 10 ; i++)
		//{
			possible = false;
			currentRequest = requests.get(i);
			System.out.println("Request number " + Integer.toString(currentRequest.getId()));
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
				System.out.println("Car number " + currentCar);
				// iterate over each assigned request
				for (j = 0 ; j < this.getAssigned_Requests().size() ; j++)
				{
					currentAssignedRequest = this.getAssigned_Requests().get(j);
					// check if request j uses this car
					System.out.println("Assigned car " + currentAssignedRequest[1] + " current car " + currentCar);
					if(currentAssignedRequest[1] == currentCar)
					{
						// check if current request and assigned request overlap in time
						if (matrix.get(currentRequest.getId(), currentAssignedRequest[0]))
						{
							carPossible = false;
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
						System.out.println("Car number " + currentCar + " was possible for request number " + currentRequest.getId());
						// assign zone to car
						assignment = new int[2];
						assignment[0] = currentCar;
						assignment[1] = currentRequest.getZone();
						System.out.println("Assigning car " + assignment[0] + " to zone " + assignment[1]);
						this.getVehicle_assignments().add(assignment);
						// assign car to request
						assignment = new int[2];
						assignment[0] = currentRequest.getId();
						assignment[1] = currentCar;
						System.out.println("Assigning car " + assignment[1] + " to request " + assignment[0]);
						this.getAssigned_Requests().add(assignment);
						// set possible to true and continue to next request
						possible = true;
						continue;
					}
					else
					{
						// if cars zone is impossible add to cost and continue to next car
						if (Math.abs(currentRequest.getZone() - carZone) > 1)
						{
							continue;
						}
						// if cars zone is possible assign car to request and continue to next request
						else
						{
							System.out.println("Car number " + currentCar + " was possible for request number " + currentRequest.getId());
							// if cars zone is next to requests zone
							if (currentRequest.getZone() != carZone)
							{
								this.setCost(this.getCost() + currentRequest.getP2());
							}
							assignment = new int[2];
							assignment[0] = currentRequest.getId();
							assignment[1] = currentCar;
							System.out.println("Assigning car " + assignment[1] + " to request " + assignment[0]);
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
				System.out.println("Add request " + unassignment[0] + " to unassigned requests");
				this.getUnassigned_Requests().add(unassignment);
			}
		}
	}
	
	public void printCSV() {
		try (PrintWriter writer = new PrintWriter(new File("solution.csv"))) {
			
			System.out.println("Writing output file...");
			System.out.println(cost + " cost");
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

		    System.out.println("Output file written!");

		    } catch (FileNotFoundException e) {
		      System.out.println(e.getMessage());
		    }
	}
	
	private int calcSum() {
		int sum = 0;
	    for(int i=0; i< req_temp.size();i++) {
	    	int id = req_temp.get(i).getId();
	    	for(int j=0; i< Unassigned_Requests.size();i++) {
	    		if(id == Unassigned_Requests.get(j)[0]) {
	    			sum += req_temp.get(i).getP1();
	    		}
	    	}
	    	
	    }
		return sum;
	}


	/**
	 * Getters and setters (Automatically generated)
	 */
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public ArrayList<int[]> getVehicle_assignments() {
		return Vehicle_assignments;
	}

	public void setVehicle_assignments(ArrayList<int[]> vehicle_assignments) {
		Vehicle_assignments = vehicle_assignments;
	}

	public ArrayList<int[]> getAssigned_Requests() {
		return Assigned_Requests;
	}

	public void setAssigned_Requests(ArrayList<int[]> assigned_Requests) {
		Assigned_Requests = assigned_Requests;
	}

	public ArrayList<int[]> getUnassigned_Requests() {
		return Unassigned_Requests;
	}

	public void setUnassigned_Requests(ArrayList<int[]> unassigned_Requests) {
		Unassigned_Requests = unassigned_Requests;
	}
}
