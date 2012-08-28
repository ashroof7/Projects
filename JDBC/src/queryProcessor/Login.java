package queryProcessor;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Login  extends Application{

public static Stage stage ;
public static Group root;	
private TextField name = new TextField();
	private PasswordField password = new PasswordField();
	private 	VBox vb = new VBox(7);
	private Label title = new Label("Login");
	private Rectangle rect ;
	private HBox hb1 = new HBox(5);
	private HBox hb2 = new HBox(5);
	private Font font = new Font("Ubuntu", 15) ;
	private Color fill = Color.LIGHTGRAY ;
	private Button log = new Button("Login");
	private Label exception = new Label("Invalid user name or  password");
	public static Scene scene ;
	
	public static void main(String[] args) {
		launch(args);
	}
	public  void  setNodes(){
		int width = 250;
		int height = 150;
		name.setPrefWidth(width-100);
		password.setPrefWidth(width-100);
		title.setFont(new Font("Ubuntu", 25));
		title.setTextFill(fill);
		vb.setAlignment(Pos.CENTER);		
		vb.setTranslateX(10);
		Label nameText = new Label("Name");
		nameText.setFont(font);
		Label passwordText = new Label("Password");
		passwordText.setFont(font);
		nameText.setPrefWidth(75);
		passwordText.setPrefWidth(75);
		nameText.setTextFill(fill);
		passwordText.setTextFill(fill);
		exception.setFont(font);
		exception.setTextFill(Color.RED);
		exception.setVisible(false);
		 
		 
		 hb1.getChildren().addAll(nameText, name);
			hb2.getChildren().addAll(passwordText, password);
			vb.getChildren().addAll(title,hb1,hb2,exception,log);
//			 rect = new Rectangle(width, height, Color.GRAY);
			log.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if(!Authenticator.validate(name.getText(), password.getText())){
						exception.setVisible(true);
						password.setText("");
					}
					else{
						System.out.println("should proceed ");
						stage.close();
					}
				}
			});

	}


	@Override
	public void start(final Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		stage = primaryStage; 
		setNodes();	
		primaryStage.setTitle("Login");
        root = new Group(rect,vb);
        scene = new Scene(root,250 ,150);
        primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
}

