
package paint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Math;
import java.lang.Void;
import paint.MyEllipse;
import javafx.scene.Group;

/**
 * @author Ashraf Saleh
 */

public class MyEllipse extends MyShape {

    public var centerX = 0.0;
    public var centerY = 0.0;
    public var radiusX = 0.0;
    public var radiusY = 0.0;

override var node = Ellipse {
                centerX: bind centerX  centerY: bind centerY 
                radiusX: bind radiusX - 2 radiusY: bind radiusY - 2 fill: bind fill;
            }

override var stroke = Ellipse {
                centerX: bind centerX  centerY: bind centerY
                radiusX: bind radiusX radiusY: bind radiusY fill: Color.BLACK;
            }

// Left
  override  var corner1 = Rectangle { width: 5 height: 5 x: bind centerX -radiusX fill: Color.RED
                y: bind centerY -2.5
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.x < centerX-5){radiusX = centerX - e.x ;}
} }

//Down
  override   var corner2 = Rectangle { width: 5 height: 5 x: bind centerX-2.5 fill: Color.RED
                y: bind centerY + radiusY - 5
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.y > centerY+5){ radiusY = e.y - centerY;}
                } }

//Right
  override   var corner3 = Rectangle { width: 5 height: 5 x: bind centerX + radiusX - 5
                y: bind centerY - 2.5 fill: Color.RED
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.x > centerX+5){radiusX = e.x - centerX ;}
                } }

//Up
   override  var corner4 = Rectangle { width: 5 height: 5 x: bind centerX - 2.5 fill: Color.RED
                y: bind centerY - radiusY
                onMouseDragged: function(e: MouseEvent): Void {
                     if (e.y < centerY-5){radiusY = centerY - e.y;}
                    
                } }

    override function contains(x, y) {
        println(Math.pow(x  - centerX, 2) / Math.pow(radiusX, 2) +
            Math.pow(y  - centerY, 2) / Math.pow(radiusY, 2) < 1);
        
        return(Math.pow(x  - centerX, 2) / Math.pow(radiusX, 2) +
            Math.pow(y  - centerY, 2) / Math.pow(radiusY, 2) < 1);

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
function run(){
Stage {
    title: "Paint"
    scene: Scene {
       width:500
       height:500
        content:[MyEllipse{centerX:150 centerY:150 radiusX:100 radiusY:50 fill:Color.GREEN        }]
    }
}
}