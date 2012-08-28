package paint;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import paint.MyShape;
import javafx.scene.shape.Rectangle;
import paint.MyTriangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import paint.MyEllipse;
import paint.MyCircle;
import javafx.util.Math;
import colorpicker.MyColorChooser;
import javafx.scene.control.ToggleButton;
import javafx.scene.Node;

/**
 * @author Ashraf Saleh
 */

var canavas :Rectangle = Rectangle{
    translateX:63 translateY:75
    width:bind scene.width - 70  height:bind scene.height - 80  fill:Color.WHITESMOKE
    var currentNode : MyShape;
onMousePressed: function(e: MouseEvent): Void {
    if (draw !=0){
        if (draw == 1){
            //rectangle
    insert currentNode = MyRectangle{x:e.x fill:col  y:e.y} into nodes.content;
    currentNode.requestFocus();
    }
    else if (draw == 2){
    //circle
    insert currentNode = MyCircle{centerX:e.x fill:col  centerY:e.y} into nodes.content;
    currentNode.requestFocus();
    }
    else if (draw == 3){
        //ellipse
    insert currentNode = MyEllipse{centerX:e.x fill:col centerY:e.y} into nodes.content;
    currentNode.requestFocus();
    }
    else if (draw == 4){
        //square
    insert currentNode = MySquare{x:e.x fill:col y:e.y} into nodes.content;
    currentNode.requestFocus();
    }
    else if (draw == 5){
        //triangle
    insert currentNode = MyTriangle{fill:col points:[e.x,e.y,e.x,e.y,e.x,e.y]} into nodes.content;
    currentNode.requestFocus();
    }
    else if (draw == 6){
        //line
    insert currentNode = MyLine{startX:e.x  startY:e.y fill:col endX:e.x  endY:e.y } into nodes.content;
    currentNode.requestFocus();
    }
}
}
onMouseReleased: function(e: MouseEvent): Void {
      if (draw !=0){
         draw = 0;
         currentNode = null;
        }
    }

onMouseDragged:function(e:MouseEvent){
    if (draw !=0){
        if (draw == 1){
    var temp = currentNode as MyRectangle;
    temp.width  = e.dragX;
    temp.height = e.dragY;
//    insert temp into nodes.content ;
            }
       else if (draw == 2){
    var temp = currentNode as MyCircle;
    temp.radius  = Math.sqrt(Math.pow(e.dragX,2) + Math.pow(e.dragY,2)) ;
//    insert temp into nodes.content ;
            }
            else if (draw == 3){
    var temp = currentNode as MyEllipse;
    temp.radiusX = e.dragX;
    temp.radiusY = e.dragY;
//    insert temp into nodes.content ;
            }
            else if (draw == 4){
    var temp = currentNode as MySquare;
    temp.side  = Math.sqrt(Math.pow(e.dragX,2) + Math.pow(e.dragY,2)) ;
//    insert temp into nodes.content ;
            }
       else if (draw == 5){
    var temp = currentNode as MyTriangle;
    temp.points  = [temp.points[0],temp.points[1],e.x,e.y,e.x+30,e.y+30];
//    insert temp into nodes.content ;
            }
       else if (draw == 6){
    var temp = currentNode as MyLine;
    temp.endX  = e.x;
    temp.endY  = e.y;
//    insert temp into nodes.content ;
            }


        }

for(node in reverse nodes.content ){
        if (node.contains(e.x - node.translateX , e.y - node.translateY)){
            var temp = node as MyShape;
            if ((toggleGroup.selectedToggle.value as String).equals("Move")){
                temp.move(e);
                break;
            }
        }
    }
}

    }
var nodes : Group = Group{
    translateX:bind canavas.translateX translateY:bind canavas.translateY
    content : []
    }

var toggleGroup = ToggleGroup {};
var myradioButtons = [
    ToggleButton{
        layoutInfo:LayoutInfo{width:60 height:60}
        graphic:{ImageView{image:Image{url:"{__DIR__}hand2.png"}}}
        toggleGroup:toggleGroup value:"Move"
        }
        ];
var trash :Node[];
var clChooser = MyColorChooser{}
var draw = 0 ;
var col = bind clChooser.selectedColor ;
var upperMenu = HBox {
    spacing : 1
translateX:5 translateY:5
    content : [Button{layoutInfo:LayoutInfo{width:60 height:60}
        graphic:{ImageView{image:Image{url:"{__DIR__}new1.png"}}}
        action: function () {
        delete nodes.content;
}
        },
        Button{layoutInfo:LayoutInfo{width:60 height:60}
        graphic:{ImageView{image:Image{url:"{__DIR__}save1.png"}}}
        action: function () {
        Myxml.saveXML(nodes.content as MyShape[]);
}

        },
        Button{layoutInfo:LayoutInfo{width:60 height:60}
        graphic:{ImageView{image:Image{url:"{__DIR__}load1.png"}}}},
        Button{layoutInfo:LayoutInfo{width:60 height:60}
        graphic:{ImageView{image:Image{url:"{__DIR__}undo1.png"}}}
        action: function () {
    insert nodes.content[sizeof nodes.content-1] into trash;
    delete nodes.content[sizeof nodes.content-1];
}
},
        Button{layoutInfo:LayoutInfo{width:60 height:60}
        graphic:{ImageView{image:Image{url:"{__DIR__}redo1.png"}}}
        action: function () {
        insert trash[sizeof trash-1] into nodes.content;
        delete trash[sizeof trash-1];
}

        },
        Button{layoutInfo:LayoutInfo{width:60 height:60}
        graphic:{ImageView{image:Image{url:"{__DIR__}trash1.png"}}}
        action: function () {
            nodes.content = [];
}

        },myradioButtons]
    }
var sideMenu = VBox {
        spacing : 3
    translateY:75 translateX:5
    content : [Button{layoutInfo:LayoutInfo{width:50 height:50}
        graphic:{Rectangle{width : 40 height:30 fill:Color.GRAY} }
        action: function() { draw = 1   }},
        Button{layoutInfo:LayoutInfo{width:50 height:50}
        graphic:{Circle{radius: 20  fill:Color.GRAY} }
        action: function() { draw = 2   }},
        Button{layoutInfo:LayoutInfo{width:50 height:50}
        graphic:{Ellipse{radiusX: 20 radiusY:10 fill:Color.GRAY} }
        action: function() { draw = 3   }},
        Button{layoutInfo:LayoutInfo{width:50 height:50}
        graphic:{Rectangle{width : 35 height:35 fill:Color.GRAY} }
        action: function() { draw = 4   }},
        Button{layoutInfo:LayoutInfo{width:50 height:50}
        graphic:{Polygon{points: [25,10,5,45,45,45] fill:Color.GRAY} }
        action: function() { draw = 5  }},
        Button{layoutInfo:LayoutInfo{width:50 height:50}
        graphic:{Rectangle{width :40 height:3 rotate:125 fill:Color.GRAY} }
        action: function() { draw = 6   }
        },
    ]
    }
var scene :Scene = Scene {
        stylesheets: ["{__DIR__}myCSS.css"]
        width: 500
        height: 500
        content: [
//            ImageView{image:Image{url:"{__DIR__}bk.jpg"}}
Rectangle{width:bind scene.width height:bind scene.height }
            canavas,
            nodes,
Rectangle{width:bind scene.width height:canavas.translateY }
Rectangle{width:canavas.translateX height:bind scene.height }
            upperMenu,
            sideMenu,
//       HBox{content:myradioButtons}
        ]
    }
var mainStage = Stage {
    title: "Paint"
    scene:bind scene
    onClose:function(){ colorStage.close();}
}
var colorStage = Stage{ scene:Scene{ content:[clChooser]}};

nodes.requestFocus();