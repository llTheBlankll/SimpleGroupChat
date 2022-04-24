package per.nytri.groupchat;

import java.io.IOException;
import java.net.ServerSocket;

public class socketTest {

    private volatile boolean run = true;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(4444);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
