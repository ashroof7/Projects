
import Piece.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import java.lang.System;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author leonardo
 */
def screenWidth = 300;
def screenHeight = 400;
def menuWidth = 100;
def menuButHi = 40;
def help: Label = Label{
    layoutInfo: LayoutInfo {width: screenWidth height: screenHeight}
    translateY:screenHeight/(geti()*geti())
    text:"Use the directional arrows to choose a square."
    " Press Enter to position or remove a piece."
    " Place all pieces so NONE are in the same row or column."
    " Nor are they diagonal from one another. Conflicting pieces will turn red"
    textFill:Color.AZURE

    textWrap:true
    font: Font{ name: "Comic Sans Ms" size: 20}
    };
def text1 = [" Play ", " Help ", " Info ", " Exit "];
def info = Label {
            layoutInfo:LayoutInfo{
                width:screenWidth}
textWrap:true
width:screenWidth
            translateY: screenHeight/(geti())
            text: "\n \n COPYRIGHT (C) 2011 "
            "Ashraf Saleh "
            "(leo_aly7@ymail.com) "
            "All Rights Reserved "
            "version 1.01 11/3/2011 \n "
            "I'd like to thank all my friends who have helped me specially Faiyaz :D "
            textFill: Color.AZURE
            font: Font { name: "Comic Sans MS" size: 20 }
            }
def backGround = ImageView {
            image: Image { url: "{__DIR__}rainbow.png" }
        };
def colorStrips = ImageView {
            image: Image { url: "{__DIR__}rainbow.png" }
        }
var name = Label{
    text : "Put Em All"
            textFill: Color.AZURE
translateX:80
translateY:screenHeight/15
layoutInfo: LayoutInfo {width: screenWidth}
font : Font{ name: "Comic Sans MS" size: 30 }
}
var name2 = Label{
    text : "Put Em All"
            textFill: Color.AZURE
translateX:80
translateY:screenHeight/15
layoutInfo: LayoutInfo {width: screenWidth}
font : Font{ name: "Comic Sans MS" size: 30 }
}
var backButton:Button = Button{
layoutInfo: LayoutInfo {
            width: menuWidth/1.5, height: menuButHi/1.5 }
            translateX:screenWidth  - (15 + menuWidth/1.5)
            translateY:screenHeight - (15 + menuButHi/1.5)
            text: " Back "
            cursor: Cursor.HAND
            effect: DropShadow {
                offsetX: 10
                offsetY: 10
                color: Color.BLACK
                radius: 10
            }
                action: function() {
           insert menu1 into scene1.content;
           delete scene1.content[2] from scene1.content;
           delete backButton from scene1.content;
            }
}
var arcadeButton: Button = Button {
            layoutInfo: LayoutInfo { width: menuWidth, height: menuButHi }
            text: text1[0]
            cursor: Cursor.HAND
            effect: DropShadow {
                offsetX: 10
                offsetY: 10
                color: Color.BLACK
                radius: 10
            }

            action: function() {
           insert menu2 into scene1.content;
           delete menu1 from scene1.content;
           }
        }
var helpButton: Button = Button {
            layoutInfo: LayoutInfo { width: menuWidth, height: menuButHi }
            width: menuWidth, height: menuButHi
            text: text1[1]

            cursor: Cursor.HAND
            effect: DropShadow {
                offsetX: 10
                offsetY: 10
                color: Color.BLACK
                radius: 10
            }

            action: function() {
           insert help into scene1.content;
           insert backButton into scene1.content;
           delete menu1 from scene1.content;
            }
        }
var infoButton: Button = Button {
            layoutInfo: LayoutInfo { width: menuWidth, height: menuButHi }
            width: menuWidth, height: menuButHi
            text: text1[2]
            cursor: Cursor.HAND
            effect: DropShadow {
                offsetX: 10
                offsetY: 10
                color: Color.BLACK
                radius: 10
            }

            action: function() {
            insert info into scene1.content;
           insert backButton into scene1.content;
           delete menu1 from scene1.content;
           }
        }
var exitButton = Button {
            layoutInfo: LayoutInfo { width: menuWidth, height: menuButHi }
            text: text1[3]
            cursor: Cursor.HAND
            effect: DropShadow {
                offsetX: 10
                offsetY: 10
                color: Color.BLACK
                radius: 10
            }

            action: function() {
                System.exit(0);
            }
        }
var easyButton = Button{
    layoutInfo: LayoutInfo { width: menuWidth, height: menuButHi }
                text:" Easy "
                width: menuWidth, height: menuButHi
                cursor: Cursor.HAND
                effect: DropShadow {
                    offsetX: 10
                    offsetY: 10
                    color: Color.BLACK
                    radius: 10
                }
                action: function() {
                seti(6);
buildBoard(6);
                delete scene1.content;
                insert colorStrips into scene1.content;
                insert name into scene1.content;
                insert myGroup into scene1.content;
                myGroup.requestFocus();
                insert rect into scene1.content;
}
}
var normalButton = Button{
    layoutInfo: LayoutInfo { width: menuWidth, height: menuButHi }
                text:" Normal "
                width: menuWidth, height: menuButHi
                cursor: Cursor.HAND
                effect: DropShadow {
                    offsetX: 10
                    offsetY: 10
                    color: Color.BLACK
                    radius: 10
                }
                action: function() {
                  seti(8);
buildBoard(8);
                delete scene1.content;
                insert colorStrips into scene1.content;
                insert name into scene1.content;
                insert myGroup into scene1.content;
                myGroup.requestFocus();
                insert rect into scene1.content;
}
}
var hardButton = Button{
    layoutInfo: LayoutInfo { width: menuWidth, height: menuButHi }
                text:" Hard "
                width: menuWidth, height: menuButHi
                cursor: Cursor.HAND
                effect: DropShadow {
                    offsetX: 10
                    offsetY: 10
                    color: Color.BLACK
                    radius: 10
                }
                action: function() {
seti(10);
buildBoard(10);
                delete scene1.content;
                insert colorStrips into scene1.content;
                insert name into scene1.content;
                insert myGroup into scene1.content;
                myGroup.requestFocus();
                insert rect into scene1.content;
}
}
var back2Button = Button{
    layoutInfo: LayoutInfo { width: menuWidth, height: menuButHi }
                text:" back "
                width: menuWidth, height: menuButHi
                cursor: Cursor.HAND
                effect: DropShadow {
                    offsetX: 10
                    offsetY: 10
                    color: Color.BLACK
                    radius: 10
                }
                action: function() {
insert menu1 into scene1.content;
           delete scene1.content[2] from scene1.content;
           delete backButton from scene1.content;

}
}
var back3Button = Button{
    layoutInfo: LayoutInfo { width: menuWidth, height: menuButHi }
                text:" Back "
                        style: "{__DIR__}controlStyle.css";
translateX:screenWidth  - (15 + menuWidth)
                translateY:screenHeight - (15 + menuButHi)
                cursor: Cursor.HAND
                effect: DropShadow {
                    offsetX: 10
                    offsetY: 10
                    color: Color.BLACK
                    radius: 10
                }
                action: function() {
delete scene1.content;
insert colorStrips into scene1.content;
insert name into scene1.content;
insert menu1 into scene1.content;
setToStart();
}
}
def menu2 = VBox {
            width: menuWidth, height: menuButHi
            translateX: (screenWidth - menuWidth) / 2
            translateY: screenHeight -  6 * (menuButHi + 10)
            content: [easyButton, normalButton, hardButton, back2Button]
            spacing: 10
        }
def menu1:VBox = VBox {
            width: menuWidth, height: menuButHi
            translateX: (screenWidth - menuWidth) / 2
            translateY: screenHeight - 6 * (menuButHi + 10)
            content: [arcadeButton, helpButton, infoButton, exitButton]
            spacing: 10
        }
var scene1:Scene =  Scene {
        width: screenWidth
        height: screenHeight
        stylesheets: "{__DIR__}controlStyle.css";
        content: [colorStrips,name,menu1]

    };

var cell = 25;
var x = 0;
var y = 0;
var yy = [0..geti() step -2];
var xx = [0..geti() step -2];

var Circles2 :Circle[];
var Circles: Circle[];
var coming = 0;
var rect:Rectangle ;
var onWin = Label{
    layoutInfo: LayoutInfo {width: screenWidth height: screenHeight}
translateX:80
text:"You won"
    textFill:Color.AZURE
    textWrap:true
    font: Font{ name: "Comic Sans Ms" size:40}

};
var boardDraw:Group;
var myGroup: Group;
var myScene: Scene = Scene {
            width: screenWidth
            height: screenHeight
            content: [backGround, myGroup, rect,name2]
        }
var scene0:Scene  = scene1;
function contradictionPut (){
state();
var a = 0;

while (a < 10) {
var b = a + 1;
var temp0 = cord[a][0];
            var temp1 = cord[a][1];
            var temp2 = temp0 - temp1;
            var temp3 = temp0 + temp1;
while(b < 10) {
 if (temp0 == cord[b][0] and temp0 != -1) {
                    Circles[a].fill = Color.RED;
                    Circles[b].fill = Color.RED ;
} else if (temp1 == cord[b][1]and temp1 != -1) {
                    Circles[a].fill = Color.RED;
                    Circles[b].fill = Color.RED ;
} else if (temp2 == cord[b][0] - cord[b][1]and temp2 != 0 ) {
                    Circles[a].fill = Color.RED;
                    Circles[b].fill = Color.RED ;
}else if (temp3 == cord[b][0] + cord[b][1]and temp3 != -2 ) {
                    Circles[a].fill = Color.RED;
                    Circles[b].fill = Color.RED ;
                }
                else if (temp1==temp0 and cord[b][0]==cord[b][1] and cord[b][1]!= -1){
                    Circles[a].fill = Color.RED;
                    Circles[b].fill = Color.RED ;
                }



b++}
a++}
}
function contradictionRemove(a: Integer, b:Integer){
for (s in [0..9]){
if(cord[s][0] == a or cord[s][1]==b or cord[s][0]+cord[s][1]==a+b or cord[s][0]-cord[s][1]== a-b  ){
  Circles[s].fill = Color.LIME;
  println({s});

}
}
for ( k in  [0..9]){
    if (board[k][k] != -1){
        removePiece(k, k);
        putPiece(k, k);
    }
}
contradictionPut();


};
function buildBoard (a:Integer) :Void {
boardDraw = Group{
        content:[for (j in [0..a-1 step 2]) {
                    for (t in [0..a  step 2]) {
                        Rectangle { height: cell width: cell fill: Color.BLACK
                            translateX: (t mod i) * cell translateY: j * cell }
                    }
                }
                 for (j in [0..a -1 step 2]) {
                    for (t in [0..a step 2]) {
                        Rectangle { width: cell height: cell fill: Color.WHITE
                            translateX: (t mod i) * cell + cell translateY: (j mod i) * cell }
                    }
                }
                 for (j in [0..a-1 step 2]) {
                    for (t in [0..a step 2]) {
                        Rectangle { height: cell width: cell fill: Color.BLACK
                            translateX: (t mod i) * cell + cell translateY: j * cell + cell }
                    }
                }
                 for (j in [0..a-1 step 2]) {
                for (t in [0..a  step 2]) {
                        Rectangle { width: cell height: cell fill: Color.WHITE
                            translateX: (t mod i) * cell translateY: (j mod i) * cell + cell }
                    }
                }]
                };
Circles2 = for (j in [0..i - 1]) {
            Circle {
                centerX: cell / 2 + j * cell
                centerY: -cell
                visible: bind not Circles[j].visible;
                radius: cell / 2 - 3 fill: Color.LIME }
        };
Circles = for (j in [0..i - 1]) {
            Circle {
                translateY: bind yy[j] - cell - (screenHeight - i * cell - ((screenWidth - i * cell) / 2)) + 0.5 * cell;
                translateX: bind xx[j] - cell - ((screenWidth - i * cell) / 2);
                centerX: ((screenWidth - i * cell) / 2) + cell / 2
                centerY: (screenHeight - i * cell - ((screenWidth - i * cell) / 2))
                visible: false
                focusTraversable: true

                radius: cell / 2 - 3 fill: Color.LIME }
        };
myGroup = Group {
            translateX: (screenWidth - geti() * cell) / 2
            translateY: screenHeight - geti() * cell - ((screenWidth - geti() * cell) / 2)

content: [boardDraw, Circles, Circles2]
            onKeyPressed: function(e: KeyEvent): Void {
                if (e.code == KeyCode.VK_RIGHT and x < (geti() * cell - cell)) {
                    x += cell;
                } else if (e.code == KeyCode.VK_LEFT and x > 0) {
                    x -= cell;

                } else if (e.code == KeyCode.VK_UP and y > 0) {
                    y -= cell;
                } else if (e.code == KeyCode.VK_DOWN and y < (geti() * cell - cell)) { y += cell;
                } else if (e.code == KeyCode.VK_ENTER) {
                    println("x , y ({x},{y})");

                    if (getElement(x / cell, y / cell) != -1 and coming >= 0) {//cell is occupied
                        println("main going to remove at ({x / cell},{y / cell}");
                        xx[getElement(x / cell, y / cell)] = 0 - cell;
                        yy[getElement(x / cell, y / cell)] = 0 - cell;
                        Circles[getElement(x / cell, y / cell)].visible = false;
                        removePiece(x / cell, y / cell);

                        coming--;
contradictionRemove(x/cell, y/cell);
                    } else if (getElement(x / cell, y / cell) == -1 and coming < geti()) {//cell is empty
                        println("main going to put at ({x / cell},{y / cell}");
                        putPiece(x / cell, y / cell);
                        xx[getElement(x / cell, y / cell)] = x + cell;
                        yy[getElement(x / cell, y / cell)] = y + cell;
                        Circles[getElement(x / cell, y / cell)].visible = true;
                        coming++;
                        if(coming==geti()){
                            println("calc score");
                            println("lose is {isRepeated()}");
                            if(isRepeated()==false){
                                delete myGroup from scene1.content;
                                delete rect from scene1.content;
                                insert onWin into scene1.content;
                                insert back3Button into scene1.content;
                        }
                        }
                    }
contradictionPut();
}
            }
        };
rect= Rectangle { width: cell height: cell strokeWidth: 2 stroke: Color.DEEPPINK
            arcWidth: 5 arcHeight: 5 fill: Color.TRANSPARENT
            translateX: bind x + (screenWidth - geti() * cell) / 2
            translateY: bind y + screenHeight - geti() * cell - ((screenWidth - geti() * cell) / 2)

        };
}
function setToStart(){
  for (j in [0..10 - 1]) {
    pieces[j].setCol(-1);
    pieces[j].setRow(-1);
    xx[j]=-1;
    yy[j]=-1;
    v[j]=0;
}
coming=0;
x=0;
y=0;
setElementsToStart();
}

function run(){
Stage {


  title: "Put Em All"
    scene:bind scene1
}
    for (j in [0..10 - 1]) {
    pieces[j] = new Piece();
}
for (a in [0..10 - 1]) {
    for (b in [0..10 - 1])
        board[a][b] = -1;
}
myGroup.requestFocus();
}
