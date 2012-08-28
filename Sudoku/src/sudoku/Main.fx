package sudoku;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import sudoku.Sudoko;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.VBox;
import java.lang.System;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sudoku.MainMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.effect.Glow;
import sudoku.CustomAlertMsg;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;

/**
 *
 * @author Ashraf Saleh
 * @version 1.0
 */
//var lightBlueColor = Color.web("#00aaff");
//var greenColor = Color.web("#61C700");
//var lightGreenColor = Color.rgb(132, 255, 21);
//var pinkColor = Color.web("#ff0ddb");
//var lightGrayColor = Color.web("#c0c0c0");
var lightGreenColor = Color.web("#55de3d");
var greyColor = Color.web("#333333");
var darkBlueColor = Color.web("#1f80ae");
var conflictColor = Color.RED;
var bkColor = Color.IVORY;
var StrongColor = darkBlueColor;
var scaleFactor = 1;
var cell = bind sq_i * 9 + sq_j;
var x = 0;
var y = 0;
var sq_i = 0;
var sq_j = 0;
var screenWidth = 608;
var screenHeight = 517;
var Solvermode = false;
var map: Integer[] = [];
var isStrong: Boolean[];
var board :MyBox[] = for (i in [0..8]) {
            // variable array contains the cells
            for (j in [0..8]) {
                MyBox {
                    tex: bind getN(map[i * 9 + j])
                    translateY: (i * 43 + 9 * (i / 3)) translateX: (j * 43 + 9 * (j / 3))
                    color: if (isStrong[i * 9 + j]) { StrongColor } else { bkColor };
                    textCol: if (isStrong[i * 9 + j]) { Color.WHITE } else { Color.BLACK };
                    onMouseClicked: function(e: MouseEvent): Void {
    sq_i=i ; sq_j = j;
    x = board[cell].translateX  as Integer ;
    y = board[cell].translateY  as Integer ;
    square.requestFocus();

    }

                }
            }
        }
var functions = [
            // functions of the main menu buttons
            function(e: MouseEvent): Void {
            // title
            }
            function(e: MouseEvent): Void {
                // new game
                startNewGame(1);
            },
            function(e: MouseEvent): Void {
                // solve it Mode
                startNewGame(2);
            },
            function(e: MouseEvent): Void {
                // Sound Button
                if (myplayer.paused) {
                    myplayer.play();
                    menuButtons[3].order = 3;
                } else {
                    myplayer.pause();
                    menuButtons[3].order = 7;
                }
            },
            function(e: MouseEvent): Void {
                // help
                currentScene = helpScene;
            },
            function(e: MouseEvent): Void {
                // about
                currentScene = aboutScene;
            },
            function(e: MouseEvent): Void {
                // exit
                System.exit(0);
            },
        ];

var music = Media {
            source: "d:Mix.mp3"
//        source:"{__DIR__}Mix.mp3"
        }
      
var myplayer = MediaPlayer { media: music
            repeatCount: MediaPlayer.REPEAT_FOREVER
            autoPlay: true
        }
var square: Rectangle = Rectangle {
            //    the select Square
            x: 4 y: 4 width: 34 height: 34 fill: Color.TRANSPARENT
            strokeWidth: 2.5 stroke: lightGreenColor arcHeight: 3 arcWidth: 3
            translateX: bind x translateY: bind y
            onKeyPressed: function(e: KeyEvent): Void {
                if (not isStrong[cell]) then changeNo(e.code);
                moveSq(e.code);
            }
        }
var menuButtons: MainMenuItem[] = for (i in [0..6]) {
            MainMenuItem { order: if (i == 3 and myplayer.paused) 7 else i
                onMousePressed: functions[i]
            }
        }
var board_sq = Group { translateX: 25 translateY: 30 scaleX: scaleFactor scaleY: scaleFactor
            content: [board, square, Label {
                    text: "press 'S' to make a cell strong" textFill: Color.WHITE
                    font: Font { size: 12 name: "Comic Sans MS" } translateY: 407
                    visible: bind Solvermode
                }] }
var sideMenu: VBox = VBox {
            // the side bar which contains the buttons
            width: screenWidth - 440
            height: screenHeight
            translateX: 438
            translateY: 50
            hpos: HPos.CENTER
            vpos: VPos.CENTER
            nodeVPos: VPos.CENTER
            spacing: 11
            content: [
                ImageView {
                    translateX: -11
                    effect: Glow { level: 0.3 }
                    image: Image { url: "{__DIR__}doku.png" } }
                Button {
                    layoutInfo: LayoutInfo { width: 130 }
                    id: "hint";
                    effect: Glow { level: 0.1 }
                    text: "Hint"
                    action: function() {
                        if (map[cell] != 0) { Stage { scene: Scene { content: CustomAlertMsg { msg: "Choose an empty cell" } } }
                        } else {
                            for (i in [0..80]) {
                                Sudoko.map[i] = map[i];
                            }
                            var sol = Sudoko.hint(cell);
                            if (sol == -1) { Stage { scene: Scene { content: CustomAlertMsg {
                                            icon: Image { url: "{__DIR__}delete.png" }
                                            msg: "Can't Solve" } } }
                            } else { map[cell] = sol; }
                        }
                        square.requestFocus();
                    }
                },
                Button {
                    layoutInfo: LayoutInfo { width: 130 }
                    text: "Solve"
                    id: "solve"
                    effect: Glow { level: 0.1 }
                    action: function() {
                        for (k in board) {
                            if (not k.conflict.isEmpty()) {
                                Stage { scene: Scene { content: CustomAlertMsg {
                                            icon: Image { url: "{__DIR__}warning.png" }
                                            msg: "Pls remove conflicts" } } }
                                square.requestFocus();
                                return;
                            }
                        }
                        Sudoko.deleteResources();
                        for (i in [0..80]) {
                            Sudoko.map[i] = map[i];
                        }
                        Sudoko.solve(0);
                        if (Sudoko.sols.isEmpty()) {
                            Stage { scene: Scene { content: CustomAlertMsg {
                                        icon: Image { url: "{__DIR__}delete.png" }
                                        msg: "Can't Solve" } }
                            }
                        } else {
                            var tempSol = Sudoko.sols.getFirst();
                            for (i in [0..80]) {
                                if (not Solvermode) {
                                    board[i].color = if (isStrong[i]) StrongColor else bkColor;
                                    board[i].textCol = if (isStrong[i]) Color.WHITE else Color.BLACK;
                                } else { board[i].color = if (board[i].color == StrongColor) StrongColor else bkColor;
                                    board[i].textCol = if (board[i].color == StrongColor) Color.WHITE else Color.BLACK;
                                }
                                map[i] = tempSol[i];
                            }
                        }
                        square.requestFocus();
                    } },
                Button {
                    layoutInfo: LayoutInfo { width: 130 }
                    text: "Submit"
                    id: "submit"
                    effect: Glow { level: 0.1 }
                    action: function() {
                        if (validSubmition()) { Stage { scene: Scene { content: CustomAlertMsg {
                                        icon: Image { url: "{__DIR__}accept.png" }
                                        msg: "You Won" } } }
                        } else { Stage { scene: Scene { content: CustomAlertMsg {
                                        icon: Image { url: "{__DIR__}delete.png" }
                                        msg: "Wrong Answer" } } };
                            square.requestFocus();
                        }
                    } },
                Button {
                    layoutInfo: LayoutInfo { width: 130 }
                    id: "sound"
                    text: bind if (not myplayer.paused) "Sound Off" else "Sound On"
                    effect: Glow { level: 0.1 }
                    action: function() {
                        if (myplayer.paused) {
                            myplayer.play();
                        } else {
                            myplayer.pause();
                        }
                        square.requestFocus();
                    }
                },
                Button {
                    layoutInfo: LayoutInfo { width: 130 }
                    text: "Main Menu"
                    id: "mainMenu"
                    effect: Glow { level: 0.1 }
                    action: function() {
                        currentScene = mainMenu;
                        square.requestFocus();
                    }
                },
                Button {
                    layoutInfo: LayoutInfo { width: 130 }
                    text: "Exit"
                    id: "exit"
                    effect: Glow { level: 0.1 }
                    action: function() {
                        System.exit(0);
                    }
                }
            ]
        }
var aboutScene: Scene = Scene {
            stylesheets: "{__DIR__}controlStyle.css"
            content: [ScrollView { layoutInfo: LayoutInfo { width: 590 height: 480 }
                    node: ImageView { image: Image { url: "{__DIR__}totalAbout.png" } } }, Button {
                    text: "back" translateX: 520 translateY: 423
                    action: function() {
                        currentScene = mainMenu;
                    }
                }]
        };
var helpScene: Scene = Scene {
            stylesheets: "{__DIR__}controlStyle.css"

            content: [ScrollView { layoutInfo: LayoutInfo { width: 590 height: 480 }
                    node: ImageView { image: Image { url: "{__DIR__}totalHelp.png" } } }, Button {
                    text: "back" translateX: 520 translateY: 423
                    action: function() {
                        currentScene = mainMenu;
                    }
                }]
        };
var mainMenu: Scene = Scene {
            stylesheets: "{__DIR__}controlStyle.css"
            fill: greyColor
            content: [VBox { translateY: 15 vpos: VPos.CENTER content: [
                Rectangle { height: 10 fill: Color.TRANSPARENT }
                menuButtons[0],
                        Rectangle { height: 10 fill: Color.TRANSPARENT }, menuButtons[1..]]
                    layoutInfo: LayoutInfo { width: 600 height: 400 }
                    nodeHPos: HPos.CENTER spacing: 7 effect: Reflection {
                        fraction: 0.5
                        topOffset: 10
                        topOpacity: 0.1
                        bottomOpacity: 0.0
                    }
                },] }
var inGameScene = Scene {
            stylesheets: "{__DIR__}controlStyle.css"
            fill: Color.web("#333333")

            content: [
                //board,
                //square,
                board_sq,
                //                borders,
                sideMenu,
            ]
        }

var currentScene: Scene = mainMenu;

public function startNewGame(state) {
    // to start a new game and clean the previous game data
    delete  map;
    Sudoko.deleteResources();
    delete  isStrong;
    sq_i = sq_j = x = y = 0;
    if (state == 1) {
        // new generated game (play Sudoku mode)
        Solvermode = false ;
        var temp: Integer[] = Sudoko.generate();
        for (i in [0..80]) {
            map[i] = temp[i];
            delete  board[i].conflict;
            if (map[i] != 0) { isStrong[i] = true; board[i].color = StrongColor;
                board[i].textCol = Color.WHITE } else { isStrong[i] = false;
                board[i].color = bkColor;
                board[i].textCol = Color.BLACK }

        }
    } else if (state == 2) {
        //solver Mode
        Solvermode = true;
        for (i in [0..80]) {
            isStrong[i] = false;
            map[i] = 0;
            delete  board[i].conflict;
            board[i].color = bkColor;
            board[i].textCol = Color.BLACK }

    }

    System.gc();
    currentScene = inGameScene;

    Label { text: "press 'S' to make a cell strong" textFill: Color.WHITE
        translateX: 5 translateY: 450
    }
}

function run() {
    Stage {
        title: "let'Sudoku", width: screenWidth, height: screenHeight
        visible: true
        icons: [Image { url: "{__DIR__}icon.png" }]
        scene: bind currentScene
    }
    board_sq.requestFocus();
    square.requestFocus();
}

public function getN(n): String {
    if (n == 0) then return " " else return "{n}"
}

public function validSubmition() {
    //    check if the submition the player did is valid or not
    for (k in board) {
        if (not k.conflict.isEmpty() or k.tex.equals(" ")) return false;
    }
    return true;
}

public function changeNo(k: KeyCode): Void {
    // this method matches the key pressed with the number then calls,changes the
    // cell in the map calls the cell coloring methods
    var n = -1;
    if (k == KeyCode.VK_0 or k == KeyCode.VK_BACK_SPACE) { n = 0; } else if (k == KeyCode.VK_1) { n = 1; } else if (k == KeyCode.VK_2) { n = 2; } else if (k == KeyCode.VK_3) { n = 3; } else if (k == KeyCode.VK_4) { n = 4; } else if (k == KeyCode.VK_5) { n = 5; } else if (k == KeyCode.VK_6) { n = 6; } else if (k == KeyCode.VK_7) { n = 7; } else if (k == KeyCode.VK_8) { n = 8; } else if (k == KeyCode.VK_9) { n = 9; } else if (k == KeyCode.VK_S and Solvermode) {
        if (board[cell].conflict.isEmpty()) {
            if (board[cell].color == bkColor) {
                board[cell].color = StrongColor;
                board[cell].textCol = Color.WHITE;
            } else {
                board[cell].color = bkColor;
                board[cell].textCol = Color.BLACK;
            }
        } else {
            Stage { scene: Scene { content: CustomAlertMsg {
                        icon: Image { url: "{__DIR__}warning.png" }
                        msg: "Pls remove conflicts" } } }
            square.requestFocus();
        }
        return;
    }
    if (n == -1 or n == map[cell]) return;
    if (n == 0) {
        // wash cell
        map[cell] = n;
        refix(cell);

    } else {
        // add new number
        map[cell] = n;

        refix(cell);
        map[cell] = 0;

        catchConflicts(sq_i, sq_j, n, conflictColor, conflictColor);
        map[cell] = n;

    }

}

public function moveSq(k: KeyCode): Void {
    // this method is responsible for moving the movement square
    var blockX = 0; var blockY = 0;
    var moveX = 0; var moveY = 0;

    if (k == KeyCode.VK_UP and sq_i > 0) { moveY = -1; sq_i -= 1 } else if (k == KeyCode.VK_DOWN and sq_i < 8) { moveY = 1; sq_i += 1 } else if (k == KeyCode.VK_LEFT and sq_j > 0) { moveX = -1; sq_j -= 1 } else if (k == KeyCode.VK_RIGHT and sq_j < 8) { moveX = 1; sq_j += 1 }

    if (moveX > 0 and (sq_j == 3 or sq_j == 6)) { blockX = 1; } else if (moveX < 0 and (sq_j == 2 or sq_j == 5)) { blockX = -1; }
    x += 43 * moveX + blockX * 9;

    if (moveY > 0 and (sq_i == 3 or sq_i == 6)) { blockY = 1; } else if (moveY < 0 and (sq_i == 2 or sq_i == 5)) { blockY = -1; }
    y += 43 * moveY + blockY * 9;
}

public function refix(n: Integer) {
    // this function is responsible for changing cells' colors after any assignment
    if (board[n].conflict.isEmpty()) then return;

    println("conflicts of {n} = {board[n].conflict.toString()}");
    while (not board[n].conflict.isEmpty()) {
        var k = board[n].conflict[0];
        println("conflicts of {k} = {board[k].conflict.toString()}");

        delete n from board[k].conflict;
        if (board[k].conflict.isEmpty()) {
            if (isStrong[k]) { board[k].color = StrongColor; } else { board[k].color = bkColor; }
        }
        delete k from board[n].conflict;
    }
    board[n].color = bkColor;
}

public function catchConflicts(i: Integer, j: Integer, n: Integer, color1: Color, Color2: Color) {
    // this function checks is the new number the player inserted is valid or not
    // and recolor the cell with color1(row or column conflict) or color2(region conflict)
    // this method is used to refix cells' colors after making a cell empty in the
    // mentioned ubove but with n = old number in cell
    for (x in [0..8]) {
        if (map[i * 9 + x] == n) {
            board[i * 9 + x].color = color1;
            insert (i * 9 + j) into board[i * 9 + x].conflict;
            board[i * 9 + j].color = color1;
            insert (i * 9 + x) into board[i * 9 + j].conflict;
        } else if (map[x * 9 + j] == n) {
            board[x * 9 + j].color = color1;
            insert (i * 9 + j) into board[x * 9 + j].conflict;
            board[i * 9 + j].color = color1;
            insert (x * 9 + j) into board[i * 9 + j].conflict;
        }

    }

    var dx: Integer = (j / 3) * 3;
    var dy: Integer = (i / 3) * 3;
    for (k in [0..2]) {
        for (k2 in [0..2]) {
            var temp = (k + dy) * 9 + k2 + dx;
            if (map[temp] == n) {
                board[temp].color = Color2;
                insert temp into board[i * 9 + j].conflict;
                board[i * 9 + j].color = Color2;
                insert i * 9 + j into board[temp].conflict;
            }

        }

    }

}
