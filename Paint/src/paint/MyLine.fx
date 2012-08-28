package paint;

import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 * @author Ashraf Saleh
 */

public class MyLine extends MyShape {


    public var startX = 0.0 ;
    public var startY = 0.0 ;
    public var endX = 0.0  ;
    public var endY = 0.0  ;


  override   var node = Line {fill:bind fill startX: bind startX  startY: bind startY   endX:bind endX  endY: bind endY};

//start
override     var corner1 = Rectangle { width: 5 height: 5   x: bind startX-2.5 fill: Color.RED
                y: bind startY-2.5
                onMouseDragged: function(e: MouseEvent): Void {
                    startX = e.x ;
                    startY = e.y ;
                } }

//end
   override  var corner2 = Rectangle { width: 5 height: 5  x: bind endX-2.5
                y: bind endY-2.5 fill: Color.RED
                onMouseDragged: function(e: MouseEvent): Void {
                    endX = e.x ;
                    endY = e.y ;
                } }
override function create():Node {
    Group{
        content:[node, corner1 , corner2]
        }
    }
}
