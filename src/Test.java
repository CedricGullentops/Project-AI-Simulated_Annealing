
public class Test {
	public void runProgram(){
		Solution sol1 = new Solution();
		int[] e = new int[2];
		e[0] = 1;
		e[1] = 2;
		sol1.getVehicle_assignments().add(e);
		Solution sol2 = new Solution(sol1);
		
		int[] e2 = new int[2];
		e2[0] = 55555;
		e2[1] = 66666;
		sol1.getVehicle_assignments().add(e2);
		
		System.out.println(sol2.getVehicle_assignments().get(0)[0]);
		System.out.println(sol2.getVehicle_assignments().get(0)[1]);
		System.out.println(sol1.getVehicle_assignments().get(1)[0]);
		System.out.println(sol1.getVehicle_assignments().get(1)[1]);
		
		System.out.println(sol1.getVehicle_assignments().get(0)[0]);
		System.out.println(sol1.getVehicle_assignments().get(0)[1]);
	}
}
