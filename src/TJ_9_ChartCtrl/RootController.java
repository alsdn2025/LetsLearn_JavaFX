package TJ_9_ChartCtrl;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {
    @FXML private PieChart pieChart;
    @FXML private BarChart barChart;
    @FXML private AreaChart areaChart;
    @FXML private LineChart lineChart;
    @FXML private ScatterChart scatterChart;
    @FXML private HBox hBoxDown;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPieChartDataWithSponge();
        setXYChartDataWithSponge(barChart);
        setXYChartDataWithSponge(areaChart);
        setXYChartDataWithSponge(lineChart);
        setXYChartDataWithSponge(scatterChart);
        this.hBoxDown.getChildren().add(getBubbleChartWithMeat());


    }


    private BubbleChart getBubbleChartWithMeat(){
        NumberAxis xAxis = new NumberAxis(2005,2020,2);
        NumberAxis yAxis = new NumberAxis(100,200,2);
        BubbleChart bubbleChart = new BubbleChart(xAxis,yAxis);

        XYChart.Series series1 = new XYChart.Series(
                FXCollections.observableArrayList(
                    new XYChart.Data(2005,121),
                        new XYChart.Data(2007,133,1.3),
                        new XYChart.Data(2009,111,1.1),
                        new XYChart.Data(2010,129,1.3),
                        new XYChart.Data(2015,150,1.5),
                        new XYChart.Data(2020,188,1.8)
                )
        );
        series1.setName("Pork");

        XYChart.Series series2 = new XYChart.Series(
                FXCollections.observableArrayList(
                        new XYChart.Data(2005,103,1.0),
                        new XYChart.Data(2007,186,1.8),
                        new XYChart.Data(2009,150,1.5),
                        new XYChart.Data(2010,155,1.5),
                        new XYChart.Data(2015,167,1.6),
                        new XYChart.Data(2020,128,1.3)
                )
        );
        series2.setName("Beef");

        bubbleChart.getData().add(series1);
        bubbleChart.getData().add(series2);
        return bubbleChart;
    }


    private void setPieChartDataWithSponge(){
        pieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("AWT", 100),
                new PieChart.Data("SWING", 100),
                new PieChart.Data("SWT", 100),
                new PieChart.Data("JavaFX", 300)
        ));
    }

    private void setXYChartDataWithSponge(XYChart chart){
        XYChart.Series seriesSponge = new XYChart.Series();
        seriesSponge.setName("Sponge");
        seriesSponge.setData(FXCollections.observableArrayList(
                new XYChart.Data("2004", 75),
                new XYChart.Data("2012", 60),
                new XYChart.Data("2020", 50)
        ));

        XYChart.Series seriesStarfish = new XYChart.Series(
                FXCollections.observableArrayList(
                        new XYChart.Data("2004", 20),
                        new XYChart.Data("2012", 30),
                        new XYChart.Data("2020", 37)
                )
        );
        seriesStarfish.setName("Starfish");

        XYChart.Series seriesSquid = new XYChart.Series(
                FXCollections.observableArrayList(
                        new XYChart.Data("2004", 7),
                        new XYChart.Data("2012", 10),
                        new XYChart.Data("2020", 13)
                )
        );
        seriesSquid.setName("Squid");

        chart.getData().add(seriesStarfish);
        chart.getData().add(seriesSponge);
        chart.getData().add(seriesSquid);
    }

}
