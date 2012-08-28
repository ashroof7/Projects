package GUI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class debugPlotter extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	double startX = -5;
	double endX = 5 ;
	double zoomFactor = 1;
	double step = 0.05;
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage stage) throws Exception {
        
		final ObservableList<Data<Number, Number>> inData = FXCollections.observableArrayList(); 
		
        final Series<Number, Number> series2 = new Series<Number, Number>();
        for (double i = startX ; i < endX ; i+=0.05) {
        	inData.add(new Data<Number, Number>(i, Math.sin(i)));
		}
        series2.setData(inData);
        
		final NumberAxis xAx = new NumberAxis();
		final NumberAxis yAx = new NumberAxis();
		
		final LineChart<Number, Number> chart =  new LineChart<Number,Number>(xAx , yAx);
		chart.getData().addAll(series2);
		chart.setCreateSymbols(false);

		chart.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode().equals(KeyCode.UP)){
					startX -=step;
					endX +=step;

					series2.getData().add(0,new Data<Number, Number>(startX, Math.sin(startX)));
					series2.getData().add(new Data<Number, Number>(endX, Math.sin(endX)));
					
				}
				else if (e.getCode().equals(KeyCode.DOWN)){
					System.out.println("before = "+ inData.size());
					inData.remove(inData.size()-1);
					System.out.println("after = "+ inData.size());
					
					inData.remove(0);	
					
						
						startX +=step;
						endX -=step;
				} 
				
			}
		});
				
		
		stage = new Stage();
		Group root = new Group(chart);
		Scene scene = new Scene(root);
		
		stage.setWidth(500);
		stage.setHeight(500);
		stage.setScene(scene);
		scene.getStylesheets().add("theme.css");
		stage.show();
		chart.requestFocus();
		
	}

	
	
	
}
