package sudoku;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.effect.Glow;
import javafx.scene.CustomNode;
import javafx.scene.Cursor;

/**
 *
 * @author Ashraf Saleh
 * @version 1.0
 */

public class MainMenuItem extends CustomNode {
// a class to return the mainMenuButton as a hyperlink Image depending on its order
    def menuImages = [Image { url: "{__DIR__}let'Sudoku.png" }, Image { url: "{__DIR__}playSudoku.png" }
                Image { url: "{__DIR__}solveIt.png" },Image { url: "{__DIR__}soundOn.png" },
                Image { url: "{__DIR__}help.png" },Image { url: "{__DIR__}about.png" },
                Image { url: "{__DIR__}exit.png" },Image { url: "{__DIR__}soundOff.png" }];
    public var order = 0;
    override function create() {
        return ImageView { image:bind menuImages[order]
                    cursor:Cursor.HAND
                    effect: Glow {
                        level: bind if (hover) { 0.9 } else { 0 }
                    }
                    onMouseEntered: function(e: MouseEvent): Void {
                        hover = true;
                    }
                    onMouseExited: function(e: MouseEvent): Void {
                        hover = false;

                    }
                }
            }
            }



