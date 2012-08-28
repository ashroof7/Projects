package findthebomb;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.HPos;
import findthebomb.Configuration;
import javafx.scene.media.*;
import javafx.geometry.VPos;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.util.Math;
import com.javafx.preview.control.PopupMenu;
import com.javafx.preview.control.MenuItem;
import javafx.scene.control.Separator;
import findthebomb.stackalgo;
import findthebomb.Queuealgo;
import java.lang.Exception;
import javafx.scene.Cursor;
import java.lang.System;

var screenW: Integer = 1024;
var screenH: Integer = 700;
var n: Integer;
var music = Media {
            source: "d://some.mp3"
        }
var myplayer = MediaPlayer { media: music
            repeatCount: MediaPlayer.REPEAT_FOREVER
            autoPlay: true
        }
def victim: Image = Image { url: "{__DIR__}victimOnTile3.png" };
def key: Image = Image { url: "{__DIR__}key.png" };
def keyPressed: Image = Image { url: "{__DIR__}keyPressed.png" };
def tile3: Image = Image { url: "{__DIR__}tile3.png" };
def yellow: Image = Image { url: "{__DIR__}yellow.png" };
def sand: Image = Image { url: "{__DIR__}sand.png" };
def grass: Image = Image { url: "{__DIR__}grass.png" };
def wood: Image = Image { url: "{__DIR__}wood.png" };
def background: Image = Image { url: "{__DIR__}dabomb.jpg" };
def pink: Image = Image { url: "{__DIR__}pink.png" };
def start: Image = Image { url: "{__DIR__}circle.png" };
def targets = for (i in [1..3]) Image { url: "{__DIR__}target{i}.png" };
var bombIsFound: Boolean = false;
var levelMap;
var current: Image;
var levelNom: Integer = Configuration.bvs[2][0];
var prev: KeyCode;
var helpOnGame: Boolean;
var BBoxs: Rectangle[];
var sandBoxs: Rectangle2D[];
var step1 = 5;
var x = 0;
var y = 0;
var bomb: Rectangle2D;
var vict: Rectangle2D;
var victimIsFound: Boolean;
var t = 200;
var stack: Stack;
var solveTimeLine: Timeline = Timeline {
            keyFrames: KeyFrame {
                time: 0.3s
                action: function() {
                    var t: point = stack.peek() as point;
                    draw(t);
                    t = stack.pop() as point;
                }
            } }
var mMove = Rectangle { width: 19 height: 12 x: bind x + 7 y: bind y + 18 }
var autoSolveMenu: PopupMenu = PopupMenu {
            items: [
                MenuItem { font:Font {name : "Comic Sans MS" size : 20}
               cursor:Cursor.HAND  text: "Any Path"
                    action: function() {
                        startGame();
                        var temp: stackalgo =
                                new stackalgo(Configuration.map, Configuration.levelIsFound,
                                Configuration.levelMatch[Configuration.bvs[2][0]],
                                Configuration.bvs[2][1], Configuration.bvs[2][2]);
                        temp.algo();
                        var backSteps = stackalgo.final1;
                        stack = backSteps;
                        solveTimeLine.play();
                        solveTimeLine.repeatCount = stack.size();
                        Configuration.configure();
                        Configuration.checkStairs();
                    }
                },
                Separator {},
                MenuItem { font:Font {name : "Comic Sans MS" size : 20}
               cursor:Cursor.HAND text: "Shortest path"
                    action: function() {
                        startGame();
                        var temp: Queuealgo =
                                new Queuealgo(Configuration.map, Configuration.levelIsFound,
                                Configuration.levelMatch[Configuration.bvs[2][0]],
                                Configuration.bvs[2][1], Configuration.bvs[2][2]);
                        temp.algorithm();
                        var backSteps = Queuealgo.final1;;
                        stack = backSteps;
                        solveTimeLine.play();
                        solveTimeLine.repeatCount = stack.size();
                        Configuration.configure();
                        Configuration.checkStairs();
                    } }
            ] }
var newGameMenu: PopupMenu = PopupMenu {
            items: [
                MenuItem {
                cursor:Cursor.HAND font:Font {name : "Comic Sans MS" size : 20}
                text: "Default Map"
                    action: function() {
                        Configuration.fileName = "map.config";
                        startGame();
                        clockTimeLine.play();
                        man.requestFocus();
                    } },
                Separator {},
                MenuItem {font:Font {name : "Comic Sans MS" size : 20}
                    text: "Load Map"
                    cursor:Cursor.HAND
                    action: function() {
                        Configuration.browse();
                        try { startGame(); } catch (ex: Exception) { Configuration.fileName = "map.config";
                            startGame();
                        }
                        clockTimeLine.play();
                        man.requestFocus();
                    }
                }] };
var help: VBox = VBox {
            translateX: 50
            translateY: 70
            onKeyPressed: function(e: KeyEvent): Void {
                if (e.code == KeyCode.VK_BACK_SPACE or e.code == KeyCode.VK_ESCAPE) {
                    delete help from tempScene.content;
                    if (helpOnGame) {
                        insert levelGroup into tempScene.content;
                        clockTimeLine.play();
                        insert man into tempScene.content;
                        insert onPause into tempScene.content;
                    } else {
                        insert menuButtons into tempScene.content;
                    } }
            }
            content: [
                Text { content: "Story" font: Font { name: "Comic Sans MS" size: 30 } fill: Color.web("#0c97e2"); },
                Text { content: "You play Leo, your enemies have kidnapped your girlfriend Mia,"
                    " imprisoned her in an old building and set a bomb to blow up the whole building"
                    wrappingWidth: 420 translateX: 30 fill: Color.web("#0c97e2");
                    font: Font { name: "Comic Sans MS" size: 20 } }
                Text { content: "Your Mission Is" font: Font { name: "Comic Sans MS" size: 30 } fill: Color.web("#0c97e2"); }
                Text { wrappingWidth: 420 translateX: 30 fill: Color.web("#0c97e2");
                    content: "is either to save Mia directly or defuse the bomb first then save Mia. \n \n "
                    font: Font { name: "Comic Sans MS" size: 20 } }
                Text { content: "Controls" font: Font { name: "Comic Sans MS" size: 30 } fill: Color.web("#0c97e2"); }
                Text { wrappingWidth: 500 translateX: 30 fill: Color.web("#0c97e2");
                    content: "Use the navigation keys to move Leo, Enter to use. You can pause the game at any time by pressing Esc."
                    font: Font { name: "Comic Sans MS" size: 20 } }
            ] }
var about: VBox = VBox { translateY: 100
            content: [
                Text { font: Font { name: "Comic Sans MS" size: 48 } content: "Find The Bomb" translateX: 150 fill: Color.web("#0c97e2") }
                Text { font: Font { name: "Comic Sans MS" size: 25 } content: " V. 1.01 \n Copyright 2011 "
                    "All Rights Reserved \n     Ashraf Saleh Mohamed Aly \n         leo_aly7@ymail.com \n "
                    "   Asser Mohamed Hassan \n        asser.mohamed.hassan@gmail.com" translateX: 40 fill: Color.web("#0c97e2") }]
            onKeyPressed: function(e: KeyEvent): Void {
                if (e.code == KeyCode.VK_BACK_SPACE or e.code == KeyCode.VK_ESCAPE) {
                    delete about from tempScene.content;
                    insert menuButtons into tempScene.content;
                }
            }
        };
var manImg = [
            Image { url: "{__DIR__}up1.png" }, Image { url: "{__DIR__}up2.png" }, Image { url: "{__DIR__}up3.png" },
            Image { url: "{__DIR__}right1.png" }, Image { url: "{__DIR__}right2.png" }, Image { url: "{__DIR__}right3.png" },
            Image { url: "{__DIR__}down1.png" }, Image { url: "{__DIR__}down2.png" }, Image { url: "{__DIR__}down3.png" },
            Image { url: "{__DIR__}left1.png" }, Image { url: "{__DIR__}left2.png" }, Image { url: "{__DIR__}left3.png" },];
var directions: Boolean[] = [false, false, false, false];
var clock = Text {
            font: Font { name: "Comic Sans MS" size: 27 }
            fill: Color.web("#0c97e2")
            translateX: 810
            translateY: 555
            content: "300" }
var level = Text {
            content: "level: {levelNom}"
            font: Font { name: "Comic Sans MS" size: 27 }
            fill: Color.web("#0c97e2")
            translateX: 780
            translateY: 590
        }
var winOrLose: Text = Text { content: "Game Over"
            textAlignment: TextAlignment.CENTER
            fill: Color.web("#0c97e2");
            stroke: Color.WHITE
            font: Font { name: "Comic Sans MS" size: 36 }
        }
var onLose: Group = Group {
            content: [Rectangle { fill: Color.GRAY
                    width: bind screenW
                    height: bind screenH
                    opacity: 0.5
                },
                VBox {
                    width: bind screenW
                    height: bind screenH
                    fillWidth: true
                    hpos: HPos.CENTER
                    vpos: VPos.CENTER
                    nodeHPos: HPos.CENTER
                    nodeVPos: VPos.CENTER
                    spacing: 10
                    content: [
                        winOrLose,
                        Button {
                            layoutInfo: LayoutInfo { width: 130 }
                            text: "Main Menu"
                            action: function() {
                                tempScene = menu;
                                delete onLose from gameScene.content;
                            } },
                        Button {
                            layoutInfo: LayoutInfo { width: 130 }
                            text: "Exit"
                            action: function() {
                                main.close();
                            } }
                    ] }] }
var clockTimeLine: Timeline = Timeline {
            repeatCount: Timeline.INDEFINITE
            keyFrames: KeyFrame {
                time: 1s
                action: function() {
                    if (t == 0) {
                        winOrLose.content = "Game Over \n Disappointing";
                        insert onLose into tempScene.content;
                        onLose.requestFocus();
                        clockTimeLine.pause();
                        onLose.requestFocus();
                    }
                    clock.content = "{t}";
                    t--;
                }
            }
        }
var currentMan: Image = manImg[7];
var man: ImageView = ImageView {
            image: bind currentMan
            translateX: bind x
            translateY: bind y
            onKeyPressed: function(e: KeyEvent): Void {
                if (e.code == KeyCode.VK_ENTER) {
                    stairAction();
                    return;
                } else if (e.code == KeyCode.VK_ESCAPE) {
                    insert onPause into tempScene.content;
                    onPause.requestFocus();
                    solveTimeLine.pause();
                    clockTimeLine.pause();
                    return;
                }
                move(e.code);
            }
            onKeyReleased: function(e: KeyEvent): Void {
                var k = e.code;
                var l = n * 32;
                if (mMove.x < 0 or mMove.y < 0 or (mMove.x + mMove.width) > l or (mMove.y + mMove.height) > l) {
                    if (k == KeyCode.VK_UP) {
                        y += step1;
                    } else if (k == KeyCode.VK_RIGHT) {
                        x -= step1;
                    } else if (k == KeyCode.VK_DOWN) {
                        y -= step1;
                    } else if (k == KeyCode.VK_LEFT) {
                        x += step1;
                    }
                    man.onKeyReleased;
                    return;
                }
                var manMove = Rectangle2D { width: 19 height: 12 minX: x + 7 minY: y + 18 }
                if (levelNom == Configuration.bvs[0][0] and bomb.contains(manMove)) {
                    println("bomb found");
                    bombIsFound = true;
                    clockTimeLine.pause();
                    insert ImageView { image: keyPressed
                        x: bomb.minX y: bomb.minY } into levelGroup.content
                } else if (levelNom == Configuration.bvs[1][0] and vict.intersects(manMove)) {
                    println("victim found");
                    victimIsFound = true;
                    clockTimeLine.pause();
                    winOrLose.content = "You Are The One";
                    insert onLose into tempScene.content;
                    onLose.requestFocus();
                }

                for (box in BBoxs) {
                    if (mMove.boundsInLocal.intersects(box.x, box.y, box.width, box.height)) {
                        if (k == KeyCode.VK_UP) {
                            y += step1;
                        } if (k == KeyCode.VK_RIGHT) {
                            x -= step1;
                        } if (k == KeyCode.VK_DOWN) {
                            y -= step1;
                        } if (k == KeyCode.VK_LEFT) {
                            x += step1;
                        }
                    }
                }
            }
        }
var levelGroup: Group = Group { content: [] }
var onPause: Group = Group {
            content: [
                Rectangle { fill: Color.GRAY
                    width: bind screenW
                    height: bind screenH
                    opacity: 0.5
                },
                VBox {
                    width: bind screenW
                    height: bind screenH
                    fillWidth: true
                    hpos: HPos.CENTER
                    vpos: VPos.CENTER
                    nodeHPos: HPos.CENTER
                    nodeVPos: VPos.CENTER
                    spacing: 10
                    content: [

                        Button {
                            layoutInfo: LayoutInfo { width: 130 }
                            text: "Resume"
                            font: Font { size: 17 name: "Comic Sans MS" }
                            action: function() {
                                delete onPause from tempScene.content;
                                man.requestFocus();
                                clockTimeLine.play();
                                solveTimeLine.play();
                            },
                        }
                        Button {
                            layoutInfo: LayoutInfo { width: 130 }
                            text: bind if (not myplayer.paused) "Sound Off" else "Sound On"
                            action: function() {
                                if (myplayer.paused) {
                                    myplayer.play();
                                } else {
                                    myplayer.pause();
                                }
                            } },
                        Button {
                            layoutInfo: LayoutInfo { width: 130 }
                            text: "Help"
                            action: function() {
                                delete levelGroup from tempScene.content;
                                delete man from tempScene.content;
                                delete onPause from tempScene.content;
                                insert help into tempScene.content;
                                helpOnGame = true;
                                help.requestFocus();
                            } },
                        Button {
                            layoutInfo: LayoutInfo { width: 130 }
                            text: "Main Menu"
                            action: function() {
                                tempScene = menu;
                                delete onPause from gameScene.content;

                            }
                        },
                        Button {
                            layoutInfo: LayoutInfo { width: 130 }
                            text: "Exit"
                            action: function() {
                                main.close();
                            }
                        }
                    ]
                }] }
var menuButtons: VBox = VBox {
            height: bind screenH
            width: bind screenW
            fillWidth: true
            translateX: 50
            vpos: VPos.CENTER
            nodeVPos: VPos.CENTER
            spacing: 10
            content: [
                Button {
                    layoutInfo: LayoutInfo { width: 230 height: 70 }
                    font: Font { name: "Comic Sans MS" size: 36 }
                    text: "New Game"
                    action: function() {
                        insert newGameMenu into tempScene.content;
                        newGameMenu.show(menuButtons.content[0], HPos.RIGHT, VPos.CENTER, 0, 10);
                    }
                },
                Button {
                    layoutInfo: LayoutInfo { width: 230 height: 70 }
                    translateX: 40
                    font: Font { name: "Comic Sans MS" size: 32 }
                    text: bind if (not myplayer.paused) "Sound Off" else "Sound On"
                    action: function() {
                        if (myplayer.paused) {
                            myplayer.play();
                        } else {
                            myplayer.pause();
                        }
                    }
                },
                Button {
                    layoutInfo: LayoutInfo { width: 230 height: 70 }
                    translateX: 80
                    font: Font { name: "Comic Sans MS" size: 32 }
                    text: "Help"
                    action: function() {
                        delete menuButtons from tempScene.content;
                        insert help into tempScene.content;
                        help.requestFocus();
                    }
                },
                Button {
                    translateX: 120
                    layoutInfo: LayoutInfo { width: 230 height: 70 }
                    font: Font { name: "Comic Sans MS" size: 32 }
                    text: "Auto Solve"
                    id: "autoSolve"
                    action: function() {
                        insert autoSolveMenu into tempScene.content;
                        autoSolveMenu.show(menuButtons.content[3], HPos.RIGHT, VPos.CENTER, 0, 10);
                    }
                },
                Button {
                    translateX: 160
                    layoutInfo: LayoutInfo { width: 230 height: 70 }
                    font: Font { name: "Comic Sans MS" size: 32 }
                    text: "About"
                    action: function() {
                        delete menuButtons from tempScene.content;
                        insert about into tempScene.content;
                        about.requestFocus();
                    }
                },
                Button {
                    translateX: 200
                    layoutInfo: LayoutInfo { width: 230 height: 70 }
                    font: Font { name: "Comic Sans MS" size: 32 }
                    text: "Exit"
                    action: function() {
                        main.close();
                    }
                }
            ]
        }
var pauseMenu: Scene = Scene {
            stylesheets: "{__DIR__}buttons.css"
            width: screenW
            height: screenH
            content: [] }
var menu: Scene = Scene {
            stylesheets: "{__DIR__}controlStyle.css"
            width: 800
            height: 600
            fill: Color.GREY
            content: [ImageView { image: background }
                menuButtons] }
var gameScene: Scene = bind Scene {
            width: screenW
            stylesheets: "{__DIR__}controlStyle.css"
            height: screenH
            impl_focusOwner: man
            content: [ImageView { image: background }, levelGroup, man, clock, level]
        }
var tempScene: Scene;
var main: Stage = Stage {
            title: "Find The Bomb"
            width: screenW + 17
            height: screenH + 39
            icons: manImg[6]
            isPopupStage: false
            scene: bind tempScene
        }

function draw(t: point) {
    var ys = t.index1;
    var xs = t.index2;
    if (t.getlevel() == Configuration.levelMatch[levelNom]) {
        insert Rectangle { height: 32 width: 32 translateX: xs * 32 translateY: ys * 32
            fill: Color.web("#ff0ddb") opacity: 0.5 } into levelGroup.content;
    } else {
        var allStairs = Configuration.stairs;
        var targetLevel; var m;
        for (i in [0..<Configuration.stairs.length]) {
            if (Configuration.levelMatch[Configuration.stairs[i][0]] == levelNom and Configuration.stairs[i][1] == ys and Configuration.stairs[i][2] == xs) {
                targetLevel = Configuration.stairs[i][3];
                m = Configuration.stairs[i][0];
                break;
            } }
        levelMap = Configuration.map[targetLevel];
        n = levelMap.length;
        var xy = Configuration.stairCoord(m, targetLevel);
        levelNom = m;
        level.content = "level = {levelNom}";
        levelbuild(m);
        y = xy[0] * 32 - 2 * step1;
        x = xy[1] * 32;
        tempScene = gameScene; } }

function tile(temp: Character) {
    var cuurent: Image;
    if (temp == 120) {//x
        if (Math.random() * 2 > 1) {
            current = yellow; } else { current = pink; }
    } else if (temp == 32) {// space
        current = tile3;
    } else if (temp == 114) {//r
        current = grass;
    } else if (temp >= 48 and temp <= 58) {
        current = wood;
    } else if (temp == 83) {//S
        current = start;
    } else if (temp == 66) {//B
        if (bombIsFound) {
            current = keyPressed;
        } else {
            current = key; }
    } else if (temp == 86) {//V
        current = victim;
    }
    return current;
}

function startGame(): Void {
    Configuration.configure();
    Configuration.checkStairs();
    System.gc();
    levelNom = Configuration.bvs[2][0];
    levelMap = Configuration.map[Configuration.levelMatch[levelNom]];
    n = levelMap.length;
//if(n > 25) then throw new Exception("map error");
level.content = "level = {levelNom}";
    delete  levelGroup.content;
    bombIsFound = false;
    victimIsFound = false;
    tempScene = Scene {};
    levelbuild(levelNom);
    y = Configuration.bvs[2][1] * 32 - 2 * step1;
    x = Configuration.bvs[2][2] * 32;
    t = 100;
    tempScene = gameScene;
    bomb = Rectangle2D { minX: Configuration.bvs[0][2] * 32
                minY: Configuration.bvs[0][1] * 32
                width: 32 height: 32
            }
    vict = Rectangle2D { minX: Configuration.bvs[1][2] * 32
                minY: Configuration.bvs[1][1] * 32
                width: 32 height: 32
            }
    man.requestFocus();
}

function levelbuild(level: Integer) {
    var target: Integer = Configuration.levelMatch[level];
    levelGroup.content = null;
    BBoxs = null;
    sandBoxs = null;
    for (i in [0..<n]) {
        for (j in [0..<n]) {
            insert ImageView {
                image: tile(Configuration.map[target][i][j])
                x: 32 * j
                y: 32 * i
            } into levelGroup.content;
            if (levelMap[i][j] == 120) {
                insert Rectangle {
                    width: 32
                    height: 32
                    x: 32 * j
                    stroke: Color.RED
                    y: 32 * i
                } into BBoxs; } else if (levelMap[i][j] == 114) {
                insert Rectangle2D {
                    width: 32
                    height: 32
                    minX: 32 * j
                    minY: 32 * i
                } into sandBoxs; }

        } }
    var st = Configuration.inlevelStairs(levelNom);
    for (i in [0..<st.length]) {
        insert Label {
            text: "  {st[i][2]}"
            width: 32 height: 32
            textFill: Color.WHITESMOKE
            font: Font { size: 20 name: "Comic Sans MS" }
            translateX: st[i][1] * 32
            translateY: st[i][0] * 32
        } into levelGroup.content
    }
}

function stairAction(): Void {
    var st = Configuration.inlevelStairs(levelNom);
    var manMove = Rectangle2D { width: 19 height: 12 minX: x + 7 minY: y + 18 }
    for (i in [0..<st.length]) {
        var sTile = Rectangle2D { minX: st[i][1] * 32 minY: st[i][0] * 32
                    width: 32 height: 32 }
        if (sTile.contains(manMove)) {
            levelMap = Configuration.map[Configuration.levelMatch[st[i][2]]];
            n = levelMap.length;
            var xy = Configuration.stairCoord(st[i][2], levelNom);
            levelNom = st[i][2];
            level.content = "level = {levelNom}";
            levelbuild(st[i][2]);
            y = xy[0] * 32 - 2 * step1;
            x = xy[1] * 32;
            tempScene = gameScene;
            man.requestFocus();
        }
    }
}

function move(k: KeyCode): Void {
    var step2 = step1;
    var flag = false;
    var l = n * 32;
    if (mMove.x < 0 or mMove.y < 0 or (mMove.x + mMove.width) > l or (mMove.y + mMove.height) > l) then return;
    for (box in BBoxs) {
        if (mMove.boundsInLocal.intersects(box.x, box.y, box.width, box.height)) {
            flag = true;
            break;
        }
    }
    var manMove = Rectangle2D { width: 19 height: 12 minX: x + 7 minY: y + 18 };
    for (box in sandBoxs) {
        if (box.intersects(manMove)) {
            step1 = step1 / 3;
            break;
        }
    }
    if (not flag) {
        if (k == KeyCode.VK_UP) {
            if (prev == KeyCode.VK_UP) {
                if (directions[0]) {
                    currentMan = manImg[1];
                } else { currentMan = manImg[2]; }
                directions[0] = not directions[0];
                y -= step1;
            } else { currentMan = manImg[0] }
            prev = KeyCode.VK_UP;

        } else if (k == KeyCode.VK_RIGHT) {
            if (prev == KeyCode.VK_RIGHT) {
                {
                    if (directions[1]) {
                        currentMan = manImg[4];
                    } else { currentMan = manImg[5]; }
                    directions[1] = not directions[1];
                    x += step1;
                }
            } else { currentMan = manImg[3] }
            prev = KeyCode.VK_RIGHT;

        } else if (k == KeyCode.VK_DOWN) {
            if (prev == KeyCode.VK_DOWN) {
                {
                    if (directions[2]) {
                        currentMan = manImg[7];
                    } else { currentMan = manImg[8]; }
                    directions[2] = not directions[2];
                    y += step1;
                }
            } else { currentMan = manImg[6] }
            prev = KeyCode.VK_DOWN;

        } else if (k == KeyCode.VK_LEFT) {
            if (prev == KeyCode.VK_LEFT) {
                {
                    if (directions[3]) {
                        currentMan = manImg[10];
                    } else { currentMan = manImg[11]; }
                    directions[3] = not directions[3];
                    x -= step1;
                }
            } else { currentMan = manImg[9] }
            prev = KeyCode.VK_LEFT;
        } }
    step1 = step2;
}

function run() {
    tempScene = menu;
}
