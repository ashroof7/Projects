import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class VNode extends Group{
	
	
	int raduis = 30 ;
	final Font f = new Font("ubnutu",17);
	
	public VNode(int data){
		Circle c = new Circle(raduis,Color.rgb(39, 40, 40)) ;
		Label element = new Label(data+"");
		element.setTextFill(Color.WHITE);
		element.setFont(f);
		element.setPrefSize(raduis*2, raduis*2);
		element.setTranslateX(-raduis);
		element.setTranslateY(-raduis);
		element.setTextAlignment(TextAlignment.CENTER);
		element.setAlignment(Pos.CENTER);
		getChildren().addAll(	c,	element);
		
	}
}
