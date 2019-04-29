
public class Test {
	public void runProgram(){
		Solution sol1 = new Solution();
		Solution sol2 = new Solution();
		
		int[] e = new int[3];
		e[0] = 1;
		e[1] = 2;
		e[2] = 3;
		sol1.getAssigned_Requests().add(e);
		sol2.copySolution(sol1);
		sol1.getAssigned_Requests().remove(0);
		System.out.println(sol2.getAssigned_Requests().get(0)[0]);
		System.out.println(sol2.getAssigned_Requests().get(0)[1]);
		System.out.println(sol2.getAssigned_Requests().get(0)[2]);
	}
}
