/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package calculator;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import calculator.NumButton;
import javafx.scene.control.Button;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.text.TextAlignment;

/**
 * @author Saleh
 */

public class NumButton extends CustomNode {

public var width = 40;
public var height = 40;
public var text = "0";
public var action :function() ;
    public override function create(): Node {

return  Button{
    layoutInfo:LayoutInfo{width:width height:height}
    style:
    "-fx-color:#676767;"
    " -fx-font: 15pt\"Comic Sans MS\"; "
    "-fx-text-fill:white;"
    ".button:hover\{-fx-color:#2cd412; \}; "
    text : text 
    textAlignment:TextAlignment.CENTER
    action:action;
    }
}

    }