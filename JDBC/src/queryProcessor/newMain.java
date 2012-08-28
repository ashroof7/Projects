package queryProcessor;

import DBSystem.MyDatabase;
import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;
import JDBC.MyDriver;
import java.awt.Frame;
import javax.swing.JOptionPane;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;

public class newMain extends Application {

    private static Stage stage;
    private static String url = "jdbc:odbc:Mydriver";
    private static Connection con = null;
    private static Statement st = null;
    private TextField name = new TextField();
    private PasswordField password = new PasswordField();
    private Label exception = new Label("Invalid user name or  password");
    public static Properties info = new Properties();
    private ResultSet rs = null;
    private ResultSetMetaData rm = null;
    private ObservableList<ArrayList<String>> data = FXCollections.observableArrayList();
    private ArrayList<String> tempRow = null;
    ArrayList<String> row1 = new ArrayList<String>();
    ArrayList<String> row2 = new ArrayList<String>();
    ArrayList<String> colsName = new ArrayList<String>();
    String userName = "";
    String passWord = "";
    String path = "";

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("JDBC.MyDriver");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new Frame(), "No Driver Found");
            return;
        }
        launch(args);
    }
    TextArea commandField = new TextArea();
    ListView<String> prevCommands = new ListView<String>();
    ObservableList<String> prevComs = FXCollections.observableArrayList();
    // VBox vb = new VBox(7);
    SplitPane spVer = new SplitPane();
    SplitPane spHor = new SplitPane();
    boolean inExecution = false;
    TableView table = new TableView();

    public Scene getWorkspace(final Stage stage) {

        VBox vb = new VBox(13);
        HBox hb1 = new HBox(7);
        HBox hb2 = new HBox(7);
        Button br = new Button("Browse");
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        final TextField tex = new TextField();
        Label title = new Label();

        HBox hb11 = new HBox(7);
        HBox hb22 = new HBox(7);
        Font font = new Font("Ubuntu", 15);

        /// setting nodes 
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
        title.setText("Select your Database Location");
        title.setFont(font);


        int width = 250;
        name.setPrefWidth(width - 100);
        password.setPrefWidth(width - 100);
        title.setFont(font);
        vb.setAlignment(Pos.CENTER);

        Label nameText = new Label("Name");
        nameText.setFont(font);
        Label passwordText = new Label("Password");
        passwordText.setFont(font);
        nameText.setPrefWidth(75);
        passwordText.setPrefWidth(75);
        exception.setFont(font);
        exception.setTextFill(Color.RED);
        exception.setVisible(false);


        hb11.getChildren().addAll(nameText, name);
        hb22.getChildren().addAll(passwordText, password);

        hb2.getChildren().addAll(ok, cancel);
        vb.getChildren().addAll(hb11, hb22, exception, title, hb1, hb2);
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
                // TODO Auto-generated method stu
                DirectoryDialog ddr = new DirectoryDialog(new Shell());
                path = ddr.open();
                if(path.length()==0)return;
                MyDatabase.path = path + "\\";
                tex.setText(path);
            }
        });

        /// old main 
        Group group = new Group(vb);

        Scene scene = new Scene(group, 150, 150);

        stage.setWidth(350);
        stage.setHeight(310);
        stage.setScene(scene);

        ok.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                info.put("username", name.getText());
                info.put("password", password.getText());
                info.put("url", url);
                Driver dv = new MyDriver();
                if (path.length() == 0) {
                    exception.setVisible(true);
                    JOptionPane.showMessageDialog(new Frame(), "Invalid Directory Path");
                    return;
                }
                try {
                    con = dv.connect(url, info);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(new Frame(), "Connection Error");
                }
                if (con == null) {
                    exception.setVisible(true);
                    return;
                }

                Group root = new Group(spVer);
                table.setItems(data);
                Scene scene2 = new Scene(root, 300, 335);
                setNodes();
                spVer.prefWidthProperty().bind(scene2.widthProperty());
                spVer.prefHeightProperty().bind(scene2.heightProperty());
                stage.setScene(scene2);
            }
        });
        return scene;
    }

    private void myExecute(String input) throws Exception {
        table.getColumns().clear();
        data.clear();

        st = con.createStatement();
        String query = "";
        query = input;
        rs = null;
        if (st.execute(query)) {
            rs = st.executeQuery(query);
            tempRow = new ArrayList<String>();
            rm = rs.getMetaData();
            int colNums = rm.getColumnCount();
            for (int i = 0; i < colNums; i++) {
                tempRow.add(rm.getColumnName(i + 1));
            }
            colsName = tempRow;
        }
        st.close();
    }

    private void setTable() throws SQLException {

        int nCols = 0;
        if (rs != null) {
            nCols = rm.getColumnCount();
            while (rs.next()) {
                tempRow = new ArrayList<>();
                for (int i = 0; i < nCols; i++) {
                    tempRow.add(rs.getObject(i + 1) + "");
                }
                data.addAll(tempRow);
            }
        }

        for (int i = 0; i < nCols; i++) {

            TableColumn temp = new TableColumn(colsName.get(i));
            final int k = i;
            temp.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ArrayList<String>, String>, ObservableValue<String>>() {

                public ObservableValue<String> call(TableColumn.CellDataFeatures<ArrayList<String>, String> p) {
                    return new ReadOnlyObjectWrapper(p.getValue().get(k));
                }
            });
            table.getColumns().add(temp);
        }

    }

    private void setNodes() {
        // commandField.setMinHeight(75);
        commandField.prefWidthProperty().bind(spVer.widthProperty());
        commandField.setManaged(true);
        commandField.setStyle("-fx-font: 15pt \"Ubuntu \"; ");
        prevCommands.setMinHeight(100);
        prevCommands.prefWidthProperty().bind(spVer.widthProperty());
        prevCommands.setItems(prevComs);
        spVer.setDividerPosition(0, 0.2);
        table.setTableMenuButtonVisible(true);
        spVer.setOrientation(Orientation.VERTICAL);
        spVer.getItems().addAll(prevCommands, commandField, table);
        spHor.getItems().addAll(spVer);

        commandField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.isShiftDown() && event.getCode() == KeyCode.ENTER
                        && !inExecution) {
                    inExecution = true;
                    try {
                        myExecute(commandField.getText());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(new Frame(), e.getMessage());
                        return;
                    }
                    prevComs.add(commandField.getText());
                    commandField.setText("");
                    prevCommands.scrollTo(prevComs.size());
                    try {
                        setTable();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(new Frame(), "Database data Error");
                        return;
                    }

                }
            }
        });
        prevCommands.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    commandField.setText(prevCommands.getSelectionModel().getSelectedItem());
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(new Frame(), e.getMessage());
                }
                ;
            }
        });
        commandField.setOnKeyReleased(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                inExecution = false;
            }
        });

        javafx.embed.swing.JFXPanel t = new JFXPanel();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("My Database");
        primaryStage.show();
        Scene temp = getWorkspace(primaryStage);
    }
}
