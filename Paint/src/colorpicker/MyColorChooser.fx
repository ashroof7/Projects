/*
 * MyColorChooser.fx
 *
 * Created on Nov 5, 2011, 12:39:39 AM
 */
package colorpicker;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

/**
 * @author leonardo
 */
public class MyColorChooser extends CustomNode {

    var color = [
                "000000", "333333", "666666", "999999", "CCCCCC", "FFFFFF", "FF0000", "00FF00", "0000FF",
                "000000", "000000", "000000", "000000", "000000", "000000", "000000", "000000", "000000",
                "000000", "000033", "000066", "000099", "0000CC", "0000FF", "990000", "990033", "990066",
                "003300", "003333", "003366", "003399", "0033CC", "0033FF", "993300", "993333", "993366",
                "006600", "006633", "006666", "006699", "0066CC", "0066FF", "996600", "996633", "996666",
                "009900", "009933", "009966", "009999", "0099CC", "0099FF", "999900", "999933", "999966",
                "00CC00", "00CC33", "00CC66", "00CC99", "00CCCC", "00CCFF", "99CC00", "99CC33", "99CC66",
                "00FF00", "00FF33", "00FF66", "00FF99", "00FFCC", "00FFFF", "99FF00", "99FF33", "99FF66",
                "330000", "330033", "330066", "330099", "3300CC", "3300FF", "CC0000", "CC0033", "CC0066",
                "333300", "333333", "333366", "333399", "3333CC", "3333FF", "CC3300", "CC3333", "CC3366",
                "336600", "336633", "336666", "336699", "3366CC", "3366FF", "CC6600", "CC6633", "CC6666",
                "339900", "339933", "339966", "339999", "3399CC", "3399FF", "CC9900", "CC9933", "CC9966",
                "33CC00", "33CC33", "33CC66", "33CC99", "33CCCC", "33CCFF", "CCCC00", "CCCC33", "CCCC66",
                "33FF00", "33FF33", "33FF66", "33FF99", "33FFCC", "33FFFF", "CCFF00", "CCFF33", "CCFF66",
                "660000", "660033", "660066", "660099", "6600CC", "6600FF", "FF0000", "FF0033", "FF0066",
                "663300", "663333", "663366", "663399", "6633CC", "6633FF", "FF3300", "FF3333", "FF3366",
                "666600", "666633", "666666", "666699", "6666CC", "6666FF", "FF6600", "FF6633", "FF6666",
                "669900", "669933", "669966", "669999", "6699CC", "6699FF", "FF9900", "FF9933", "FF9966",
                "66CC00", "66CC33", "66CC66", "66CC99", "66CCCC", "66CCFF", "FFCC00", "FFCC33", "FFCC66",
                "66FF00", "66FF33", "66FF66", "66FF99", "66FFCC", "66FFFF", "FFFF00", "FFFF33", "FFFF66",
            ];
   public-read var selectedColor :Color;
    var index = 0;

    override function create(): Node {
Group{content:[
Rectangle {stroke : Color.BLACK strokeWidth:3 fill:bind selectedColor
width:50 height:50 translateY:10}
 Group {translateX:55
                content:
        for (col in [0..19]) {
                        for (row in [0..8]) {
                                var temp = Color.web("#{color[index++]}");
                            Rectangle{width:10 height:10 translateX:(col mod 25)*10
                            translateY:row*10  fill:temp
                            onMouseClicked:function(e: MouseEvent):Void{
                                    selectedColor = temp }
                        }
                    }
                }
}]}
    }
}
