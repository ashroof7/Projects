package calculator;

import calculator.NumButton;
import javafx.scene.layout.VBox;
import calculator.OperationButton;
import javafx.scene.control.Label;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.util.StringTokenizer;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.scene.control.TextBox;
import javafx.scene.layout.HBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Exception;
import javafx.util.Math;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 * @author Ashraf Saleh
 */
var s = "";
var current = "0";
var clear = false;
var cont = false;
var calculated = false;
var firstOp = true;
var start = true;
var var1: Double;
var var2: Double;
var res: Double;
var var22 =bind var2.longValue();
var var11 =bind var1.longValue();
var ress =bind res.longValue();

var lastPressed = false;
var tk;
var oper;
var cell = 40;
var space = 3;
var scWidth = (cell + space) * 5 - space;
var scHeight = 30;
var fonty = Font { name: "Comic Sans MS" size: 13 };
var operationChar = ["+", "-", "*", "-"];
var basicOperations: OperationButton[] = [
            OperationButton { translateY: 0 translateX: (cell + space) * 3
                text: "*" action: function() {
                    operation("*");
                    nums.requestFocus();
                } },
            OperationButton { translateY: cell + space translateX: (cell + space) * 3
                text: "/" action: function() {
                    operation("/");
                    nums.requestFocus();
                } },
            OperationButton { translateY: (cell + space) * 2 translateX: (cell + space) * 3
                text: "+" action: function() {
                    operation("+");
                    nums.requestFocus();
                } },
            OperationButton { translateY: (cell + space) * 3 translateX: (cell + space) * 3
                text: "-" action: function() {
                    operation("-");
                    nums.requestFocus();
                } }];
var secondaryOperations: OperationButton[] = [
            OperationButton { translateX: (cell + space) * 4
                text: "AC" action: AC },
            OperationButton {
                translateY: (cell + space) translateX: (cell + space) * 4
                text: "Del" action: function() {
                    current = current.substring(0, current.length() - 1);
                    nums.requestFocus();
                } },
            OperationButton { translateY: (cell + space) * 2 translateX: (cell + space) * 4
                text: "=" height: cell * 2 + space action: function() {
                    operation("=");
                    nums.requestFocus();
                } },
        ];
var lastLine: NumButton[] = [
    NumButton { translateY: (cell + space) * 3 text: "{0}" width: cell * 2 + space
                action: function() {
                    NumPressed(0);
                } },
            NumButton { translateY: (cell + space) * 3 translateX: (cell + space) * 2 text: "."
                action: function() {
                    if (lastPressed) return ;
                         else   { lastPressed =  true  ;
                         if(current.equals("")){ current ='0.';} else { current += '.'; } }
            nums.requestFocus(); }}
];
var  primaryScreen = Label { text: bind   current translateX:3
layoutInfo:LayoutInfo{width:scWidth-6  height: scHeight + 5 }
font:fonty  textAlignment:TextAlignment.RIGHT  }
var secondryScreen = Label{ text:bind  s  translateX : 3
    layoutInfo: LayoutInfo { width: scWidth - 6  height: scHeight+5 }
            font: fonty textAlignment: TextAlignment.RIGHT
        };
var screen = Group {translateX:5 translateY:5 content: [
                Rectangle { width: scWidth  arcWidth:7 arcHeight:7 height: scHeight * 2 + 10
                fill: Color.web("#949494") }
                VBox { content: [secondryScreen, primaryScreen] }] }
var numpad = for (i in[0..2]){for(j in [0..2]){
    NumButton{translateX:(cell+space)*(2-j mod 3) translateY:(cell+space)*i
    text:"{9 - i*3-j}" action:function(){
        NumPressed(9 - i*3-j);
        nums.requestFocus();}}
    }}
var previousOperations = ListView{
   layoutInfo:LayoutInfo{ width:150 height:245 } translateX:(cell+space)*5+10; translateY:5
    items: []
    }
var ind = bind previousOperations.selectedIndex on replace {
    println(ind);
    if (ind >= 0){
    s = previousOperations.selectedItem.toString();
    res = Double.parseDouble(s.substring(s.indexOf("=")+2 , s.length()));
    }};
var customOperation:TextBox = TextBox{
    layoutInfo:LayoutInfo{width:(cell+space)*5 - 3}
translateY:(cell+space)*6+10 translateX:5
onKeyPressed: function(e: KeyEvent): Void {
     if(e.code == KeyCode.VK_ENTER)evaluate();
     }
}
var nums :Group = Group{translateX:5 translateY:80
    content : [numpad,lastLine,basicOperations,secondaryOperations]
    onKeyPressed: function(e: KeyEvent): Void {
        var k = e.code ;
        println("k = {e}");

        if (k == KeyCode.VK_0){NumPressed(0);}
        else if (k == KeyCode.VK_1){NumPressed(1);}
        else if (k == KeyCode.VK_2){NumPressed(2);}
        else if (k == KeyCode.VK_3){NumPressed(3);}
        else if (k == KeyCode.VK_4){NumPressed(4);}
        else if (k == KeyCode.VK_5){NumPressed(5);}
        else if (k == KeyCode.VK_6){NumPressed(6);}
        else if (k == KeyCode.VK_7){NumPressed(7);}
        else if (k == KeyCode.VK_8){NumPressed(8);}
        else if (k == KeyCode.VK_9){NumPressed(9);}
        else if (k == KeyCode.VK_PERIOD){if (lastPressed)return; else{lastPressed = true; if(current.equals("")){ current='0.';} else{ current+='.';}}}
        else if (k == KeyCode.VK_EQUALS and e.shiftDown){operation("+");}
        else if (k == KeyCode.VK_MINUS){operation("-");}
        else if (k == KeyCode.VK_8 and e.shiftDown){operation("*");}
        else if (k == KeyCode.VK_DIVIDE or k == KeyCode.VK_SLASH ){operation("/");}
        else if (k == KeyCode.VK_EQUALS or k == KeyCode.VK_ENTER){operation("=");}
        else if (k == KeyCode.VK_DELETE or k == KeyCode.VK_BACK_SPACE){
            current = current.substring(0,current.length()-1)}
        else if (k == KeyCode.VK_F5 ){AC(); }
nums.requestFocus();
    }

    }
var util = HBox{
    translateX:220 translateY:265 spacing:space content:[OperationButton
{width:50 height:25 text:"eval" action: function() {
            evaluate();
            nums.requestFocus();
            }
},
OperationButton{width:50 height:25 text:"Save"
action: function() {
    save();
    nums.requestFocus();
    }
},
OperationButton{width:50 height:25 text:"Load"
action: function() {
    load();
    nums.requestFocus();
    }
}]}
var bin = ImageView{image:Image{url:"{__DIR__}bin.png"} translateX:337 translateY:-7
scaleX:0.5 scaleY:0.5
onMousePressed: function(e: MouseEvent): Void {
    delete previousOperations.items;
    nums.requestFocus();
    }
}
Stage {
    title: "My Calculator"
    icons: [Image{url:"{__DIR__}calcIcon.png"}];
    scene: Scene {
                width: 381
                height: 300
        fill:Color.web("#c1c1c1");
        content: [screen, nums, util, customOperation, previousOperations,bin]
    }
}
nums.requestFocus();

function  AC(){
    s = oper = "";
    current= "0";
    var1 = var2 = res = 0;
    calculated = cont = clear = false ;
    start = firstOp =true ;
    nums.requestFocus();
    }
function operation(op:String):Void{
            oper = op ;
            try {
            operationBig(op);
            }catch(e:Exception){
                JOptionPane.showMessageDialog(null, "Oops it seems that the program "
                "encountered an error", "Error", JOptionPane.ERROR_MESSAGE);
                }
            clear = true ;
    }
function NumPressed (n):Void{
//    if (current.length()>= 8 )return;
    if (oper.equals("=")){
        AC();
        }

if (not clear){
        if (start){current = "{n}";
        }
        else{current ="{current}{n}";}
        }
        else {
            current = "{n}";
            clear = false ;
            }
    start = false;
    if(calculated){
    if (Math.abs(res - ress) > 0.0000000001){
         s = "{res}";}
         else{ s = "{ress}";}
        s += " {oper} {n}";

        cont  = true ;
calculated = false ;
        }

    }
function operationBig(op:String): Void {
//    res = Double.parseDouble(current);
    if (firstOp and op.equals("=")) then return;
    if (firstOp){s = "{current} {op}";
    cont = true;
    firstOp = false;
    return;
    }

//var var22 =bind var2.longValue();
//var var11 =bind var1.longValue();
//var ress =bind res.longValue();
  if (cont) {
        tk = new StringTokenizer(s);
        var1 = Double.parseDouble(tk.nextToken());
        var operation =  tk.nextToken();
        var2 = Double.parseDouble(current);

        if (operation.equals("+")) {
            res = var1 + var2 ;
         if (Math.abs(var1-var11)>0.00000000000001){
         s = "{var1} + ";}
         else{ s = "{var11} + ";}
         if (Math.abs(var2-var22)>0.00000000000001){
         s += "{var2} = ";}
         else{ s += "{var22} = ";}
         if (Math.abs(res - ress) > 0.0000000001){
         s += "{res}";}
         else{ s += "{ress}";}

        } else if (operation.equals("-")) {
            res = var1 - var2;
if (Math.abs(var1-var11)>0.00000000000001){
         s = "{var1} - ";}
         else{ s = "{var11} - ";}
         if (Math.abs(var2-var22)>0.00000000000001){
         s += "{var2} = ";}
         else{ s += "{var22} = ";}
         if (res - ress > 0.0000000000000001){
         s += "{res}";}
         else{ s += "{ress}";}

        } else if (operation.equals("*")) {
            res = var1 * var2;
            if (Math.abs(var1-var11)>0.00000000000001){
         s = "{var1} * ";}
         else{ s = "{var11} * ";}
         if (Math.abs(var2-var22)>0.00000000000001){
         s += "{var2} = ";}
         else{ s += "{var22} = ";}
         if (res - ress > 0.0000000000000001){
         s += "{res}";}
         else{ s += "{ress}";}


        } else if (operation.equals("/")) {
            if (var2<0.000000000000000000000001){
   JOptionPane.showMessageDialog(null, "cannot divide by zero" , "Arithmetic Exception" , JOptionPane.WARNING_MESSAGE);
return;
}
            res = var1 / var2 ;
  if (Math.abs(var1-var11)>0.00000000000001){
         s = "{var1} / ";}
         else{ s = "{var11} / ";}
         if (Math.abs(var2-var22)>0.00000000000001){
         s += "{var2} = ";}
         else{ s += "{var22} = ";}
         if (res - ress > 0.0000000000000001){
         s += "{res}";}
         else{ s += "{ress}";}

        }
        var temp = s ;
        insert temp into previousOperations.items;
//    cont = false ;
    calculated = true ;
    }
    else{
        if (op.equals("+")) {
            s = "{res} + ";
            oper = "+";
        } else if (op.equals("-")) {
            s = "{res} - ";
            oper = "-";
        } else if (op.equals("*")) {
            s = "{res} * ";
            oper = "*";
        } else if (op.equals("/")) {
            s = "{res} / ";
            oper = "/";
        }
    cont = true ;
        }
      if (op.equals("="))then cont = false;
nums.requestFocus();
}
function save(){
  try{
        var bw = new BufferedWriter( new FileWriter("save.op"));
        var n :Integer = sizeof previousOperations.items;
        bw.write(n.toString());
        println(n);
        bw.newLine();
        for (i in previousOperations.items){
            bw.write(i.toString());
            bw.newLine();
            }
            bw.close();
  }catch(e:IOException ){
        JOptionPane.showMessageDialog(null,"Oops it seems that the "
        "program encountered an error while saving data" ,"Saving Error", JOptionPane.ERROR_MESSAGE);
      }
  }
function load(){
  try{
        var br = new BufferedReader( new FileReader("save.op"));
        var t = Integer.parseInt(br.readLine());
        while (t-->0){
            insert br.readLine() into previousOperations.items
            }
            br.close();
  }catch(e:IOException ){
        JOptionPane.showMessageDialog(null,"Oops it seems that the "
        "program encountered an error while saving data" ,"Saving Error", JOptionPane.ERROR_MESSAGE);
     return;
     }
  }
function evaluate(){
      var ind = 0;
      var temp = customOperation.text;
      temp = temp.replace("=", "") ;
      temp.trim();
      for( i in operationChar){
          if (temp.indexOf(i)!= -1){
              ind = temp.indexOf(i);
              break ;
              }
          }
      NumPressed(temp.substring(0,ind));
      try{operation(temp.substring(ind,ind+1));}catch(e:Exception){
          JOptionPane.showMessageDialog(null, "Oops it seems that "
          "u have enterd a wrong operation","entery error",JOptionPane.WARNING_MESSAGE);
          }
      NumPressed(temp.substring(ind+1,temp.length()));
      operation("=");
      }
