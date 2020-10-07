/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatClient;

import java.io.*;
import java.net.*;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class ClientHandler implements Runnable {

    private ObjectOutputStream outToServer;//Outputs data to server
    private ObjectInputStream inputFromServer;//Takes input from server
    private TextArea textArea;//textArex input from controller
    private int count;//counts number of messages
    private boolean wasSent = false;//tests to see if message was sent with boolean

    public ClientHandler(TextArea textArea, Socket socket) {
        this.textArea = textArea;
        count = 0;
        try {
            outToServer = new ObjectOutputStream(socket.getOutputStream());

            inputFromServer = new ObjectInputStream(socket.getInputStream());

        } catch (IOException ex) {
            Platform.runLater(() -> textArea.appendText("Exception in ClientHandler constructor: " + ex.toString() + "\n"));//error handling
        }
    }

    public void sendMessage(String message) {//Sends message to server
        try {
            count++;
            wasSent = true;
            outToServer.writeObject(2);
            outToServer.flush();
            outToServer.writeObject(message);
            outToServer.flush();
            textArea.appendText("You: " + message + "\n");
        } catch (IOException ex) {
            ex.toString();
        }

    }

    public int getSize() {//Gets the size of chat log
        try {
            outToServer.writeObject(1);
            outToServer.flush();
        } catch (IOException ex) {
            ex.toString();
        }
        int count = 0;
        try {
            count = Integer.parseInt((String) inputFromServer.readObject());
        } catch (IOException ex) {
            Platform.runLater(() -> textArea.appendText("Error in getSize: " + ex.toString() + "\n"));
        } catch (ClassNotFoundException ex) {
            ex.toString();
        }
        return count;
    }

    public String getMessage(int location) {//Gets message to server
        String m = "";

        try {
            outToServer.writeObject(3);
            outToServer.flush();
            outToServer.writeObject(location);
            outToServer.flush();

            m = inputFromServer.readObject().toString();
            System.out.println("End of getMessage try");
        } catch (IOException ex) {
            ex.toString();
        } catch (ClassNotFoundException ex) {
            ex.toString();
        }

        return m;
    }

    @Override
    public void run() {
        String read = "";
        while (true) {//Inf loop so the client cant continously get messages
            if (wasSent) {
                try {
                    read = (String) inputFromServer.readObject();
                    wasSent = false;
                } catch (IOException ex) {
                    ex.toString();
                } catch (ClassNotFoundException ex) {
                    ex.toString();
                }
            }

            int chatLogSize = getSize();
            if (chatLogSize > count) {

                read = getMessage(chatLogSize);

                textArea.appendText("Friend: " + read + "\n");
                count++;
            } else {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    ex.toString();
                }
            }

        }

    }

}
