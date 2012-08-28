package GUI;

import java.util.LinkedList;

import function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class Plotter extends Group{

	
	final NumberAxis xAx = new NumberAxis();
	final NumberAxis yAx = new NumberAxis();
	final LineChart<Number, Number> chart =  new LineChart<Number,Number>(xAx , yAx);
	LinkedList<ObservableList<Data<Number, Number>>> indata = new LinkedList<ObservableList<Data<Number,Number>>>();
	LinkedList<Series<Number, Number>> series = new LinkedList<Series<Number,Number>>();
    LinkedList<Function> fns = new LinkedList<Function>();
	
	double startX = -5;
	double endX = 5 ;
	double zoomFactor = 1;
	double step = 0.05;
	
	
	public Plotter() {
		chart.setCreateSymbols(false);
		getChildren().add(chart);
		
	}
	
	public void addFunction(Function fn){
		fns.add(fn);
	}
	
	@SuppressWarnings("unchecked")
	public void addData(Function fn, String name){
		ObservableList<Data<Number, Number>> data  = FXCollections.observableArrayList();
		
		indata.add(data);
		fns.add(fn);
		for (double i = startX ; i < endX ; i+=0.05) {
        	data.add(new Data<Number, Number>(i, fn.getValue(i)));
		}
		series.add(new Series<Number, Number>(name,data));
		chart.getData().addAll(series.getLast());
	}
	
	public ObservableList<Data<Number, Number>> getData(int i){
		return indata.get(i);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
