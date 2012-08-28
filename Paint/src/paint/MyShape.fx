package paint;
/**
 * @author Ashraf Saleh
 */

import javafx.scene.CustomNode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Node;

public abstract class MyShape extends CustomNode {
       // default access is private
      protected var node :Node;
      protected var stroke :Node;
      public var fill = Color.BLACK;
      public function move(e : MouseEvent){}
      protected var corner1: Node;
      protected var corner2: Node;
      protected var corner3: Node;
      protected var corner4: Node;

    }
