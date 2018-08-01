package src.view;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 * The controller for the terms statistics view.
 * 
 * @author Alby Dev
 */

public class ProbabilityStatisticsController {

	@FXML
	private LineChart<Double, Integer> lineChart;

	/**
	 * Initializes the controller class. This method is automatically called after
	 * the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		lineChart.setTitle("Prediction probability line chart");
		lineChart.setLegendVisible(false);
	}

	/**
	 * Sets the terms to show the statistics for.
	 * 
	 * @param terms
	 */
	public void setTermData() {
        
		//defining a series
        XYChart.Series series = new XYChart.Series();
        //series.setName("My searches");

		//here I shuold read the info from a file, split, loop and add the info
		
        //populating the series with data
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));
        series.getData().add(new XYChart.Data(13, 17));
        series.getData().add(new XYChart.Data(14, 23));
        series.getData().add(new XYChart.Data(15, 67));
        series.getData().add(new XYChart.Data(16, 98));

		// Create a XYChart.Data object for each search. Add it to the series.
		//series.getData().add(new XYChart.Data<>(termsSearched, termProbability));
        
        lineChart.getData().add(series);
	}
}
