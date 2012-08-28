package queryProcessor;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

import com.sun.media.jfxmedia.events.NewFrameEvent;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Workspace extends Application {

    VBox vb = new VBox(13);
    HBox hb1 = new HBox(7);
    HBox hb2 = new HBox(7);
    Button br = new Button("Browse");
    Button ok = new Button("OK");
    Button cancel = new Button("Cancel");
    TextField tex = new TextField();
    ListView<String> files = new ListView<String>();
    Label title = new Label();
    static String[] arg;

    public static void main(String[] args) {
        arg = args;
        launch(args);
        File f = new File("C:\\Users\\Saleh\\Documents\\NetBeansProjects\\Jdbc final\\");
        String files[] = f.list();
        ObservableList<ArrayList<String>> data = FXCollections.observableArrayList();
    }

    private void setNodes() {
        vb.setAlignment(Pos.CENTER);
        hb1.getChildren().addAll(tex, br);
        tex.setPrefWidth(250);
        hb1.setPrefWidth(300);
        hb2.setPrefWidth(300);
        hb1.setAlignment(Pos.CENTER);
        hb2.setAlignment(Pos.CENTER);
        br.setPrefSize(100, 30);
        ok.setPrefSize(70, 30);
        cancel.setPrefSize(70, 30);
        title.setText("please selcet your Database Location");
        title.setFont(new Font("Ubuntu", 15));
        hb2.getChildren().addAll(ok, cancel);
        vb.getChildren().addAll(title, hb1, hb2);
        vb.setTranslateX(15);
        vb.setTranslateY(15);

        cancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        br.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                FileDialog fd = new FileDialog(new Frame(), " Open File");
                fd.show();
                String url = fd.getFile();
            }
        });

    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        setNodes();
        primaryStage.setTitle("My Database");
        Group root = new Group(vb);
        Scene scene = new Scene(root, 340, 150);
        ok.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                Login.scene = new Scene(Login.root, 250, 150);
                Login.stage.setScene(Login.scene);

            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}