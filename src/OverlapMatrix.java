import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
	 
	public void printOut() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(new File("bitmatrix.csv"));
		StringBuilder sb = new StringBuilder();		
		for(int j=-1;j<bitArr.length;j++) {
			sb.append(j+",");
		}		 
		sb.append("\n");
		for(int i =0;i<bitArr.length;i++) {
			sb.append(i+ ",");
			for(int j=0;j<bitArr.length;j++) {
				if(bitArr[j].get(i)) {
					sb.append("1,");
				}
				else {
					sb.append("0,");
				}
			}			
			sb.append("\n");
		}
		writer.write(sb.toString());
		writer.close();
	}
}
