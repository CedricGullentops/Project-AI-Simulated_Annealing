import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PlotData extends ApplicationFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final XYSeries series = new XYSeries("Score");
	
	
	public PlotData(final String title){
		super(title);
	}
	
	public void addDataPoint(int score, int msec){
		this.series.add(msec, score);
	}
	
	public void createPlot(){
		final XYSeriesCollection data = new XYSeriesCollection(series);
	    final JFreeChart chart = ChartFactory.createXYLineChart(
	        "Score vs Time",
	        "X", 
	        "Y", 
	        data,
	        PlotOrientation.VERTICAL,
	        true,
	        true,
	        false
	    );
	    final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    setContentPane(chartPanel);
	    this.pack();
	    RefineryUtilities.centerFrameOnScreen(this);
	    this.setVisible(true);
	}
	

}
