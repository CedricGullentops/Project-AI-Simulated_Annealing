public class Controller {
	public static void main(String [] args) {
		boolean test = false;
		String input;
		String output;
		int seconds;
		long seed;
		int nthreads;
		if (test){
			input = "examples/210_5_44_25.csv";
			output = "solution.csv";
			seconds = 60;
			seed = 10;
			nthreads = 4;
		}
		else{
			input = args[0];
			output = args[1];
			seconds = Integer.parseInt(args[2]);
			seed = Long.parseLong(args[3]);
			nthreads = Integer.parseInt(args[4]);
		}	
		Head head = new Head(input, output, seconds, seed, nthreads);
		head.runProgram();
	}
}
