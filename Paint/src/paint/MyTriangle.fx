package paint;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 * @author Ashraf Saleh
 */
public class MyTriangle extends MyShape {

    public var points = [0.0, 0.0, 0.0, 0.0, 0.0, 0.0];
override     var node = Polygon {
                points: bind points fill: bind fill
            }
//p1
    override var corner1 = Rectangle { width: 5 height: 5 x: bind points[0] - 2.5 fill: Color.RED
                y: bind points[1] - 2.5
                onMouseDragged: function(e: MouseEvent): Void {
                    points[0] = e.x;
                    points[1] = e.y;
                } }
//p2
    override var corner2 = Rectangle { width: 5 height: 5 x: bind points[2] - 2.5
                y: bind points[3] - 2.5 fill: Color.RED
                onMouseDragged: function(e: MouseEvent): Void {
                    points[2] = e.x;
                    points[3] = e.y;
                } }
//p3
   override  var corner3 = Rectangle { width: 5 height: 5 x: bind points[4] - 2.5
                y: bind points[5] - 2.5 fill: Color.RED
                onMouseDragged: function(e: MouseEvent): Void {
                    points[4] = e.x;
                    points[5] = e.y;
                } }

    override function contains(x, y) {
        /*
        i found this optimized method on
        http://justbasic.wikispaces.com/SpriteCollisionDetection#Function04
         */
        var b0 = (points[2] - points[0]) * (points[5] - points[1]) - (points[4] - points[0]) * (points[3] - points[1]);
        var b1 = ((points[2] - x) * (points[5] - y) - (points[4] - x) * (points[3] - y)) / b0;
        if (b1 <= 0){println(false); return false;}
        var b2 = ((points[4] - x) * (points[1] - y) - (points[0] - x) * (points[5] - y)) / b0;
        if (b2 <= 0){println(false); return false;}
        var b3 = ((points[0] - x) * (points[3] - y) - (points[2] - x) * (points[1] - y)) / b0;
        if (b3 <= 0){println(false); return false;}
        println(true);
        return true;
    }

override public function move(e : MouseEvent){
    translateX = e.x - (points[0]+points[2]+points[4])/3;
    translateY = e.y - (points[1]+points[3]+points[5])/3;;
    }
    override function create(): Node {
        Group {
            content: [node, corner1, corner2, corner3]
        }
    }

}
