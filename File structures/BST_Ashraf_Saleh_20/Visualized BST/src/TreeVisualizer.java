import javafx.scene.Group;
import javafx.scene.shape.Line;


public class TreeVisualizer extends Group{

	final int gap = 15 ; 
	int width;
	int height;
	int levels;
	int raduis = 30;
	
	public TreeVisualizer(BST<Integer> bt){
		levels = bt.depth();
		int lastLevelNodes = 1<<(levels-1);
		width = gap*(lastLevelNodes+1)  +raduis*2*lastLevelNodes ;
		height = gap*(levels+1) + raduis*2*levels;
		draw(width/2+raduis , gap+raduis , bt.getRoot(),1);
	}
	
	private void draw(int x, int y, Node<Integer> current,int l) {
		if (current==null )return ;
		
		if (current.right != null){
		getChildren().add(new Line(x, y, x+gap+raduis + (levels-l-1)*(raduis+gap), y+gap+3*raduis));
		draw(x+gap+raduis + (levels-l-1)*(raduis+gap), y+gap+3*raduis, current.right,l+1);
		}
		
		if (current.left !=null){
		getChildren().add(new Line(x, y, x-gap-raduis - (levels-l-1)*(raduis+gap), y+gap+3*raduis));
		draw(x -gap -raduis-(levels-l-1)*(raduis+gap), y+gap+3*raduis, current.left,l+1);
		}
		
		VNode temp = new VNode(current.data);
		temp.setTranslateX(x);
		temp.setTranslateY(y);
		getChildren().add(temp);
		
	}

}
