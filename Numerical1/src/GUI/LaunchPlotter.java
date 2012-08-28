package GUI;


import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LaunchPlotter extends Application{

	Plotter c = new Plotter();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}



@Override
	public void start(Stage stage) throws Exception {
		
//		c.addData(new SinFun(new XFun(1, 1)), "sin");
//		c.addData(new CosFun(new XFun(1, 1)), "cos");
			
		
		c.chart.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode().equals(KeyCode.UP)){
					c.startX -=c.step;
					c.endX +=c.step;
					int i = 0 ;
					
					for (Series<Number, Number> s : c.series) {
						s.getData().add(0,new Data<Number, Number>(c.startX,c.fns.get(i).getValue(c.startX)));
						s.getData().add(new Data<Number, Number>(c.endX,c.fns.get(i).getValue(c.endX)));
						i++;
					}
				}
				else if (e.getCode().equals(KeyCode.DOWN)){
					for (ObservableList<Data<Number, Number>> d : c.indata) {
//						System.out.println("before = "+ inData.size());
						d.remove(d.size()-1);
//						System.out.println("after = "+ inData.size());
						d.remove(0);	
					}
						
					c.startX +=c.step;
					c.endX -=c.step;
				} 
				
			}
		});

	

		Group root = new Group(c);
		Scene scene = new Scene(root);
		scene.getStylesheets().add("style.css");
		stage.setWidth(500);
		stage.setHeight(450);
		stage.setScene(scene);
		stage.show();
		
		c.chart.requestFocus();
		
	}

}
