
public class Clock {
	private long start;
	public Clock() {
		start = System.currentTimeMillis();
	}
	public int cTime() {
		return (int)(System.currentTimeMillis() - start);
	}
	public String displayTime() {
		int ms = (int)(System.currentTimeMillis() - start);
		int min = ms/60000;
		double sec = (double) ((ms%60000.0)/1000.0);
		return (min + " min " + sec + " s");
	}
}
