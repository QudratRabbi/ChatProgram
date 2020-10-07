package ChatClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Runner extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLChatClient.fxml"));
        stage.setTitle("Chat Client");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
