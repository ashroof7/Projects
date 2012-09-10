import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	
	Button zoomIn = new Button("+");
	Button zoomOut = new Button("-");
	Button add  = new Button("Add");
	TextField tf = new TextField();
	Button delete = new Button("Del");
	Button draw = new Button("Draw");
	Group root = new Group();
	VBox vb =new VBox();
	BST<Integer> bt ;
	ToolBar tb= new ToolBar();
	HBox menu = new HBox();
	ScrollPane scroll = new ScrollPane();
	TreeVisualizer tv  ; 
	StackPane nodeContainer = new StackPane();
	
	public void setNodes(){
		
		draw.getStyleClass().addAll("last", "capsule");
		root.setId("background");
		scroll.setTranslateX(7);
		scroll.setTranslateY(7);
		menu.getStyleClass().setAll("segmented-button-bar");
		menu.getChildren().addAll(zoomIn,zoomOut, tf,add,delete,draw);
		
zoomIn.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				tv.setScaleX(tv.getScaleX()*1.1);
				tv.setScaleY(tv.getScaleY()*1.1);
				       
				nodeContainer.setPrefSize(
			              Math.max(nodeContainer.getBoundsInParent().getMaxX()*1.1, scroll.getViewportBounds().getWidth()),
			              Math.max(nodeContainer.getBoundsInParent().getMaxY()*1.1, scroll.getViewportBounds().getHeight())
			            );
			}
		});

		zoomOut.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				tv.setScaleX(tv.getScaleX()/1.1);
				tv.setScaleY(tv.getScaleY()/1.1);
					
				nodeContainer.setPrefSize(
			              Math.max(nodeContainer.getBoundsInParent().getWidth()/1.1, scroll.getViewportBounds().getWidth()),
			              Math.max(nodeContainer.getBoundsInParent().getHeight()/1.1, scroll.getViewportBounds().getHeight())
						);
			}
		});
		
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				int temp = 0 ;
				try{
					temp = Integer.parseInt(tf.getText());
				if (bt == null){
					bt = new BST<Integer>(temp);
				}else 
					bt.add(temp);
				
				drawFn();
				tf.setText("");
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		delete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				int temp = 0 ;
				try {
					temp = Integer.parseInt(tf.getText());
					bt.remove(bt.get(temp));
					drawFn();
					tf.setText("");
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});

		draw.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
			drawFn();
			}
		});
		
		zoomIn.getStyleClass().addAll("first");
		tf.setPrefWidth(50);
		tb.getItems().addAll(menu);
		menu.setPrefWidth(780);
		menu.setAlignment(Pos.CENTER);
		scroll.setPrefSize(60, 513);
		scroll.setMaxWidth(777);
		tb.setPrefWidth(800);
		vb.getChildren().addAll(tb,scroll);
		
	}
	
	
	public void drawFn(){
		tv = new TreeVisualizer(bt);
		
		if (!nodeContainer.getChildren().isEmpty())
		nodeContainer.getChildren().remove(0);
		
		nodeContainer .getChildren().add(tv);
		nodeContainer.setPrefSize(
	              Math.max(nodeContainer.getBoundsInParent().getMaxX(), scroll.getViewportBounds().getWidth()),
	              Math.max(nodeContainer.getBoundsInParent().getMaxY(), scroll.getViewportBounds().getHeight())
	            );
		scroll.setContent(nodeContainer);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		setNodes();
		Stage stage = new Stage();
		Scene scene= new Scene(root);
		
		stage.setWidth(800);
		stage.setHeight(600);
		
		scene.getStylesheets().add(getClass().getResource("segmented.css").toExternalForm());
		root.getChildren().addAll(vb);
		stage.setScene(scene);
		stage.setTitle("BST");
		stage.show();
	}

}
