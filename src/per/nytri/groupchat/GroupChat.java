package per.nytri.groupchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GroupChat {

    public static String name = "";
    public static volatile boolean isFinished = false;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            InetAddress group = InetAddress.getByName("239.32.1.52");
            MulticastSocket socket = new MulticastSocket(1234);
            socket.setTimeToLive(0);
            socket.joinGroup(group);
            System.out.print("Enter your name: ");
            name = scanner.nextLine();

            Thread readMessages = new Thread(new ReadMessages(socket, group));
            readMessages.start();

            System.out.println("Start typing message.");
            while (true) {
                String message;
                System.out.print("Message: ");
                message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    scanner.close();
                    socket.leaveGroup(group);
                    socket.close();
                    isFinished = true;
                    break;
                }
                message = name + ": " + message;
                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 1234);
                socket.send(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ReadMessages implements Runnable {

        public MulticastSocket socket;
        public InetAddress group;

        public ReadMessages(MulticastSocket socket, InetAddress group) {
            this.socket = socket;
            this.group = group;
        }

        @Override
        public void run() {
            while (!GroupChat.isFinished) {
                byte[] buffer = new byte[10000];

                DatagramPacket packet = new DatagramPacket(buffer, 10000, group, 1234);
                String message;
                try {
                    socket.receive(packet);

                    message = new String(buffer, 0, packet.getLength(), StandardCharsets.UTF_8);
                    if (!message.startsWith(GroupChat.name)) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
