package queryProcessor;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class GUI extends Application {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
	
		TreeItem<String> item1 = new TreeItem<String>("root");
		item1.setExpanded(true);
		item1.getChildren().addAll(new TreeItem<String>("ch1"),new TreeItem<String>("ch2"));
	
		TreeView<String> tree = new TreeView<String>(item1);
		TreeCell<Label> test = new TreeCell<Label>();
		Group root = new Group();
		root.getChildren().addAll(tree);
		Scene  scene = new Scene(root , 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
