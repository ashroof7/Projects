
package paint;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Math;

/**
 * @author Ashraf Saleh
 */

public class MyCircle extends MyShape {

    public var centerX = 0.0;
    public var centerY = 0.0;
    public var radius = 0.0;

override var node = Circle {
                centerX: bind centerX  centerY: bind centerY
                radius: bind radius- 2  fill: bind fill;
            }

override var stroke = Circle {
                centerX: bind centerX  centerY: bind centerY
                radius: bind radius fill: Color.BLACK;
            }

// Left
   override var corner1 = Rectangle { width: 5 height: 5 x: bind centerX -radius fill: Color.RED
                y: bind centerY -2.5
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.x < centerX-5){radius= centerX - e.x ;}
} }

//Down
   override var corner2 = Rectangle { width: 5 height: 5 x: bind centerX-2.5 fill: Color.RED
                y: bind centerY + radius - 5
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.y > centerY+5){radius = e.y - centerY;}
                } }

//Right
   override var corner3 = Rectangle { width: 5 height: 5 x: bind centerX + radius- 5
                y: bind centerY - 2.5 fill: Color.RED
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.x > centerX+5){radius= e.x - centerX ;}
                } }

//Up
    override var corner4 = Rectangle { width: 5 height: 5 x: bind centerX - 2.5 fill: Color.RED
                y: bind centerY - radius
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.y < centerY-5){radius = centerY - e.y;}

                } }

    override function contains(x, y) {
        println(Math.pow(x  - centerX, 2) + Math.pow(y  - centerY, 2)
        < Math.pow(radius, 2));

        return(Math.pow(x  - centerX, 2) + Math.pow(y  - centerY, 2)
        < Math.pow(radius, 2));

    }

override public function move(e:MouseEvent){
    translateX = e.x - centerX;
    translateY = e.y - centerY;
    }

public function resize(e:MouseEvent){
    return false }

    public override function create(): Node {
        Group {
            content: [stroke,  node,
                corner1, corner2, corner3, corner4
            ]
        }
    }
}
