package per.nytri.groupchat;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class socketSubTest implements Runnable {

    char dataType;
    int length;
    Socket socket;
    DataInputStream inputStream;

    public socketSubTest(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.dataType = this.inputStream.readChar();
        this.length = this.inputStream.readInt();
    }

    @Override
    public void run() {
        System.out.println("Reading Started");
        try {
            byte[] messageByte = new byte[this.length];
            int data = this.inputStream.read();
            StringBuilder stringBuilder = new StringBuilder(this.length);

            while (data != 1) {
                while (true) {
                    stringBuilder.append(new String(messageByte, 0, this.inputStream.read(messageByte), StandardCharsets.UTF_8));

                    if (stringBuilder.length() >= this.length) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
