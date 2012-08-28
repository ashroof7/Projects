package GUI;


import parse.Parser;

import function.Function;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OnlyPlot extends Application{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	Label function = new Label("Function");
	TextField fn = new TextField();
	TextField name = new TextField();
	Label nameLabel = new Label ("  Name  ");
	Button add = new Button("Add");
	Button plot = new Button("Plot");
	HBox up = new HBox(7);
	HBox mid = new HBox(7);
	HBox down = new HBox(7);
	VBox vb = new VBox(7);
	
	LaunchPlotter lp = new LaunchPlotter();
	Parser p;
	
	private void setNodes(){
		up.getChildren().addAll(function,fn);
		mid.getChildren().addAll(nameLabel,name);
		down.getChildren().addAll(add,plot);
		up.setAlignment(Pos.CENTER);
		mid.setAlignment(Pos.CENTER);
		down.setAlignment(Pos.CENTER);
		vb.setPadding(new Insets(11,11,11,11));
		vb.getChildren().addAll(up,mid,down);
	} 
	
	public void setlisteners(){
		add.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				try {
					p = new Parser(fn.getText().trim());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Function f = p.getPoly();
				lp.c.addData(f, name.getText());
				fn.setText("");
				name.setText("");
			}
		});
		
		
		plot.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					lp.start(new Stage());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Group root = new Group(vb);
		Scene scene = new Scene(root);
		
		setNodes();
		setlisteners();
		stage.setScene(scene);
		scene.getStylesheets().add("style.css");
		stage.show();
		
	}


}
