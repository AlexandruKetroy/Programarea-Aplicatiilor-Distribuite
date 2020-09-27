package utm.pad.lab1;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SubscriberSocket {
    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;

    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Connection refused: Subscriber could not connect to broker");
        }
    }

    public void send() {
        if(socket == null) {
            return;
        }
        while (true) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter topic :");
                String topic = br.readLine();
                topic = "subscribe" + topic;
                byte[] data = topic.getBytes(StandardCharsets.UTF_8);
                System.out.println(data.length);
                outStream.writeInt(data.length);
                outStream.write(data);
                outStream.flush();
            } catch (IOException e) {
                System.out.println("Subscriber cannot send the data");
            }
        }
    }

    public void close() {
        try {
            outStream.close();
            outStream.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Connection closed");
        }
    }
}
