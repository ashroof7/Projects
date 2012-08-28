

package calculator;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 * @author Ashraf Saleh
 */

public class OperationButton extends CustomNode {
public var width = 40;
public var height = 40;
public var text = "";
public var action :function() ;

    public override function create(): Node {
       return  Button{
    layoutInfo:LayoutInfo{width:width height:height}
    style:"-fx-color:#15bbd6; -fx-font: 13pt\"Comic Sans MS\"; "
    "-fx-text-fill:white;"
    ".button:hover\{-fx-color:#2cd412; \}; "
    text : text
    textAlignment:TextAlignment.CENTER
    action:action;
    }
}
    }