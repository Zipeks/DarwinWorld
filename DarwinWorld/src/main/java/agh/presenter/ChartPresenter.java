package agh.presenter;

import agh.model.StatsListener;
import agh.model.util.SimulationStats;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;

public class ChartPresenter implements StatsListener {
    private final XYChart.Series<Number, Number> animalSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> grassSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> fieldsSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> lifetimeSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> childSeries = new XYChart.Series<>();
    private final XYChart.Series<Number, Number> energySeries = new XYChart.Series<>();
    @FXML
    private LineChart<Number, Number> chart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private NumberAxis yAxis;
    @FXML
    public void initialize(){
        choiceBox.getItems().addAll("Animals Count", "Grass Count", "Empty Fields","Avg lifetime","Avg Child Count", "Avg energy");
        choiceBox.setValue("Animals Count");
        xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(xAxis) {
            @Override
            public String toString(Number object) {
                return String.valueOf(object.intValue());
            }
        });
        xAxis.setLabel("Day");
        xAxis.setTickUnit(1);
        yAxis.setLabel("Value");
        chart.setLegendVisible(false);
        chart.setCreateSymbols(false);
    }

    public void onSeriesChange(ActionEvent e){
        String selected = choiceBox.getValue();
        chart.getData().clear();
        switch (selected) {
            case "Animals Count" -> chart.getData().add(animalSeries);
            case "Grass Count" -> chart.getData().add(grassSeries);
            case "Empty Fields" -> chart.getData().add(fieldsSeries);
            case "Avg lifetime" -> chart.getData().add(lifetimeSeries);
            case "Avg Child Count" -> chart.getData().add(childSeries);
            case "Avg energy" -> chart.getData().add(energySeries);
        }
    }


    private void updateStats(SimulationStats stats) {
        animalSeries.getData().add(new XYChart.Data<>(stats.currentDate(), stats.animalsCount()));
        grassSeries.getData().add(new XYChart.Data<>(stats.currentDate(), stats.grassesCount()));
        fieldsSeries.getData().add(new XYChart.Data<>(stats.currentDate(), stats.emptyFields()));
        lifetimeSeries.getData().add(new XYChart.Data<>(stats.currentDate(), stats.avgLifeTime()));
        childSeries.getData().add(new XYChart.Data<>(stats.currentDate(), stats.avgChildCount()));
        energySeries.getData().add(new XYChart.Data<>(stats.currentDate(), stats.avgEnergyLevel()));

    }
    @Override
    public void statsChanged(SimulationStats stats) {
        Platform.runLater(() -> updateStats(stats));
    }
}
