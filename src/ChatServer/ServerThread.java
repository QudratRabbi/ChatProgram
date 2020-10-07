package ChatServer;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerThread implements Runnable {

    Socket socket;
    ObjectInputStream inputFromClient;//Input from the client(s)
    ObjectOutputStream outToClient;//Output for the client(s)
    static ArrayList<String> chatLog = new ArrayList<String>();//Chat log
    static ArrayList<ObjectOutputStream> clients = new ArrayList<ObjectOutputStream>();//Clients

    public ServerThread(Socket socket) {
        try {
            this.socket = socket;
            inputFromClient = new ObjectInputStream(socket.getInputStream());
            outToClient = new ObjectOutputStream(socket.getOutputStream());
            clients.add(outToClient);

        } catch (IOException ex) {
            ex.toString();
        }

    }

    public void sendMessage(String message) {//Sends messages to the client(s)
        try {
            for (ObjectOutputStream s : clients) {
                s.writeObject(message);
                s.flush();
                outToClient.flush();
            }

        } catch (IOException ex) {
            ex.toString();
        }
    }

    @Override
    public void run() {
        System.out.println("ServerThread Run");
        while (true) {
            try {
                int command = (int) inputFromClient.readObject();
                String message;

                switch (command) {
                    case 1:
                        //get size of chatLog
                        outToClient.writeObject(Integer.toString(chatLog.size()));
                        outToClient.flush();
                        break;
                    case 2:
                        //recieve message
                        message = inputFromClient.readObject().toString();
                        chatLog.add(message);
                        System.out.println(message);
                        break;
                    case 3:
                        //message access request
                        int location = (int) inputFromClient.readObject();
                        sendMessage(chatLog.get(location - 1));
                        break;
                }

            } catch (IOException ex) {
                ex.toString();
            } catch (ClassNotFoundException ex) {
                ex.toString();
            }

        }

    }
}
