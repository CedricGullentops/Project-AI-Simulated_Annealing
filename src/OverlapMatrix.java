import java.util.BitSet;

public class OverlapMatrix {
	 private BitSet[] bitArr;
	 
	 public OverlapMatrix(int rows, int cols)
	 {
		 bitArr = new BitSet[rows];
	     for (int i = 0; i < rows; i++)
	    	 bitArr[i] = new BitSet(cols);
	 } 
	 
	 public boolean get(int r, int c)
	 {
		 return bitArr[r].get(c);
	 }
	
	 public void set(int r, int c, boolean bool)
	 {
		 bitArr[r].set(c, bool);
	 }
	 
	 public void setFalse(int r, int c)
	 {
		 bitArr[r].clear(c);
	 }  

	 public void display()
	 {
	     System.out.println("\nBit Matrix : ");
	     for (BitSet bs : bitArr)
	    	 System.out.println(bs);
	     System.out.println();
	}    
}
