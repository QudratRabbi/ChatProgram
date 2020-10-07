package ChatServer;

import java.io.*;
import java.net.*;

public class Server {

    ServerSocket server;//The socket object for the server

    public Server(int port) {

        new Thread(() -> {
            try {
                server = new ServerSocket(port);
                System.out.println("Server Started!");
                while (true) {
                    try {
                        Socket socket = server.accept();//Accepts clients
                        System.out.println("Connection from " + socket);

                        new Thread(new ServerThread(socket)).start();//Runs the ServerThread with the socket as input
                    } catch (IOException ex) {
                        ex.toString();
                    }
                }
            } catch (IOException ex) {
                ex.toString();
            }
        }).start();

    }

}
