
package sudoku;

import javafx.scene.CustomNode;
import javafx.scene.control.Label;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Ashraf Saleh
 * @version 1.0
 */
public class MyBox extends CustomNode {
// a class to return the a cutom node 'MyBox' used in building the board View
    public var tex: String;
    public var color = Color.IVORY;
    public var op = 1;
    public var textCol = Color.BLACK;
    public var conflict: Integer[] = [];
    override var children = [

                Rectangle { width: 40 height: 40 fill: bind color opacity: bind op
                    arcHeight: 7 arcWidth: 7 translateX: 1 translateY: 1 },

                Label { text: bind tex layoutInfo: LayoutInfo { width: 40 height: 40 }
                    translateX: 15 textFill: bind textCol translateZ:-3
                    font: Font { size: 19 embolden: true   name: "Comic Sans MS" }
                    textAlignment: TextAlignment.CENTER }
            ]
}