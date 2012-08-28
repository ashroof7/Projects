
package sudoku;

import javafx.scene.CustomNode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Label;
import javafx.geometry.HPos;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
/**
 *
 * @author Ashraf Saleh
 * @version 1.0
 */
public class CustomAlertMsg extends CustomNode {
// a class that constructs a cutom alert msg
    public var msg = "CustomAlertMsg !";
    public var fill = Color.web("#333333");
    public var icon = Image { url: "{__DIR__}warning.png" };

    override function create(): Node {
        return Group { content: [Rectangle { width: 310 height: 150 fill: fill },

                        HBox {
                          layoutInfo:LayoutInfo {   width:310 height:100}
                        spacing:5
                        nodeVPos:VPos.CENTER
                        nodeHPos:HPos.CENTER
                        vpos:VPos.CENTER
                        hpos:HPos.CENTER
                        content: [
                                ImageView { image: icon     }
                        Label { text: msg
                            textAlignment: TextAlignment.CENTER
                            textWrap: true
                            textFill: Color.web("#00aaff")
                            font: Font { name: "Comic Sans MS" size: 21 } },] },
                        Button {
                            text: "OK"
                            style: ".button \{ -fx-color:#22aaff; -fx-font:17pt \"Comic Sans MS\";-fx-cursor: hand;"
                            "-fx-text-fill: #e4f3fc; \}"
                            ".button:hover\{-fx-color: #0c97e2;\}  ";
                            strong: true
                            layoutInfo: LayoutInfo { width: 60 height: 30 }
                            translateX: 155 - 30
                            translateY: 100
                            action: function(): Void {
                                parent.scene.stage.close();
                            }
                        }
                        Label {
                            text: "icon used from http://dryicons.com"
                            translateY: 135 translateX: 5
                            textFill: Color.LIGHTGREY
                        }
                    ] }
    }

    }


