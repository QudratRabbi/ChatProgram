/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatClient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.*;
import java.net.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author Connor M
 * @author Keion P
 * @author Qudrat R
 */
public class FXMLChatClientController implements Initializable {

    private Client client;
    @FXML
    private TextField textField;//Text bar used for inputing text
    @FXML
    private TextArea messageArea;//Has a log of the text

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int port = 8000;//Port that the client is using
        client = new Client(port, messageArea);
        messageArea.setEditable(false);

    }

    @FXML
    private void sendButton(ActionEvent event) {
        updateText();

    }

    @FXML
    private void sendEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            updateText();
        }

    }

    public void updateText() {//Updates the text to the GUI
        if (!textField.getText().trim().isEmpty()) {
            client.sendMessage(textField.getText());
            textField.setText("");
        }

    }

    class Client {//Used for ClientHandler

        TextArea text;
        private ClientHandler client;
        Socket socket;

        public Client(int port, TextArea text) {

            this.text = text;
            new Thread(() -> {
                try {
                    socket = new Socket("localhost", port);
                } catch (IOException ex) {
                    ex.toString();
                }
                client = new ClientHandler(this.text, socket);
                new Thread(client).start();
            }).start();

        }

        public void sendMessage(String message) {//Sends message to client object which sends it to the ClientHandler class
            client.sendMessage(message);
        }

    }

}
