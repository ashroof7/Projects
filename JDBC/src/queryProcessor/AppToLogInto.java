package queryProcessor;

import javax.security.auth.callback.LanguageCallback;

import queryProcessor.LoginPreloader.CredentialsConsumer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AppToLogInto extends Application implements CredentialsConsumer {
    String user = null;
    Label l = new Label("");
    Stage stage = null;
    public static void main(String[] args) {
    	launch(args);
    }
    
    private void mayBeShow() {
        // Show the application if it has credentials and 
        // the application stage is ready
        if (user != null && stage != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    stage.show();
                }                
            });
        }
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setScene(new Scene(l, 400, 400));
        mayBeShow();
    }
 
    public void setCredential(String user, String password) {
        this.user = user;
        l.setText("Hello "+user+"!");
        mayBeShow();
    }
}