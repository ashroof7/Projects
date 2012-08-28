package queryProcessor;

import java.util.ArrayList;
import java.util.Observable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CreateTable extends Application {

	/**
	 * @param args
	 */
	int x =0 ,y =1 ;
	private TextField col = new TextField();
	private ChoiceBox<String> type = new ChoiceBox<String>(
			FXCollections.observableArrayList("varchar", "integer", "double","primary_key"));
	private VBox hb1 = new VBox(7);
	private Font font = new Font("Ubuntu", 15);
	private VBox vb = new VBox(7);
	private GridPane grid = new GridPane();
	Button addColumn = new Button("Add Column");
	Button addTable = new Button("Add Table");
	GridPane tableInstances = new GridPane();
	ScrollPane scroll = new ScrollPane();
	Label error = new Label("Syntax Error or No ColumnType Choosed ");
	
	
	private void setNodes(){
		 Label colName = new Label("Column Name");
		 Label colType   = new Label("Column Type");
		 error.setTextFill(Color.RED);
		 error.setFont(font);
		 error.setVisible(false);
		colName.setFont(font);
		colType.setFont(font);
		col.setPrefWidth(100);
		grid.setVgap(10);
		grid.setHgap(10);
		tableInstances.setHgap(25);
		tableInstances.setVgap(10);
		Label title1 = new Label("   Column        ");
		title1.setFont(new Font("Ubuntu", 20));
		Label title2 = new Label("   Type");
		title2.setFont(new Font("Ubuntu", 20));
	tableInstances.add(title1, 0, 0);
	tableInstances.add(title2, 1,0 );
		grid.add(colName,0,0);
		grid.add(colType, 0,1);
		grid.add(col, 1,0);
		grid.add(type, 1,1);
		addColumn.setPrefSize(110, 30);
		addColumn.setFont(font);
		addTable.setPrefSize(110, 30);
		addTable.setFont(font);
		 
		scroll.setContent(tableInstances);
		scroll.setPrefSize(150, 150);
		scroll.setPannable(false);
	hb1.getChildren().addAll(grid, addColumn);
		vb.setAlignment(Pos.CENTER);
		hb1.setAlignment(Pos.CENTER_LEFT);
		vb.setPadding(new Insets(10, 10, 10, 10));
	vb.getChildren().addAll(hb1,error,scroll,addTable );
	
	}
	
public static void main(String[] args) {
		launch(args);
	}

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		 setNodes();	
			primaryStage.setTitle("Create Table");
	        Group root = new Group(vb);
	        Scene scene = new Scene(root ,300,335);
	        addColumn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
						if (col.getText().matches("\\b*\\w+\\b*")&&type.getSelectionModel().getSelectedItem()!=null){
							error.setVisible(false);
							Label temp1 = new Label("       "+col.getText());
							temp1.setFont(font);
							Label temp2 = new Label("       "+type.getSelectionModel().getSelectedItem());
							temp2.setFont(font);
							tableInstances.add(temp1,  x++%2, y);
							tableInstances.add(temp2, x++%2, y++);
							col.setText("");
						}
						else{
							error.setVisible(true);
						} 
				}
			});
	        
	        addTable.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
				//add table logic
					if (tableInstances.getChildren().size()<=2){
						// no table to add
					}
					else {
						// add table methods
						System.out.println("table added");
					}
				}
			});
	        
	        primaryStage.setScene(scene);
			primaryStage.show();
		}

	
}
