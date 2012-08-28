package paint;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;

/**
 * @author Ashraf Saleh
 */
public class MyRectangle extends MyShape {

    public var width = 0.0;
    public var height = 0.0;
    public var x = 0.0;
    public var y = 0.0;

override var node = Rectangle {
                x: bind x + 2 y: bind y + 2
                width: bind width - 4 height: bind height - 4 fill: bind fill;
            }

override var stroke = Rectangle {
                x: bind x y: bind y
                width: bind width height: bind height fill: Color.BLACK;
            }

// upperleft
    override var corner1 = Rectangle { width: 5 height: 5 x: bind x fill: Color.RED
                y: bind y
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.x < x + width - 10){
                    width += x - e.x;
                    x = e.x;
                    }
                    if (e.y < y + height - 10){
                    height += y - e.y;
                    y = e.y;
                    }
} }

//lowerleft
   override  var corner2 = Rectangle { width: 5 height: 5 x: bind x fill: Color.RED
                y: bind y + height - 5
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.x < x + width - 10){
                    width += x - e.x ;
                    x = e.x ;
                    }
                    if (e.y > y + 10){height = e.y - y ;}
                    
                } }

//upperRight
   override  var corner3 = Rectangle { width: 5 height: 5 x: bind x + width - 5
                y: bind y + height - 5 fill: Color.RED
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.x > x + 10){width = e.x - x ;}
                    if (e.y > y + 10){height = e.y - y;}
                } }

//lowerRight
    override var corner4 = Rectangle { width: 5 height: 5 x: bind x + width - 5 fill: Color.RED
                y: bind y
                onMouseDragged: function(e: MouseEvent): Void {
                    if (e.x > x + 10){width = e.x - x ;}
                    if(e.y < y + height - 10){
                    height += y - e.y ;
                    y = e.y ;
                    }
                } }

//public var resizable = Group{content:[corner1,corner2,corner3,corner4]}

    override function contains(x, y): Boolean {
        def ptx = x - this.x
//        - translateX
        ;
        def pty = y - this.y
//        - translateY
        ;
        println(ptx >= 0 and ptx < width and pty >= 0 and pty < height );
        return(ptx >= 0 and ptx < width and pty >= 0 and pty < height )
    }

public function resize(e:MouseEvent ){return true}

override function move(e:MouseEvent){

 translateX = e.x - x
 - width/2
 ;
 translateY = e.y - y
 - height/2
 ;
    }

    public override function create(): Node {
        Group {
            content: [stroke, node,
                corner1, corner2, corner3, corner4

            ]
        }
    }
}
