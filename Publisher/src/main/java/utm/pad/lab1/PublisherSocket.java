package utm.pad.lab1;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PublisherSocket {
    private Socket socket;
    private DataInputStream inStream;
    private DataOutputStream outStream;

    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Publisher could not connect to broker");
        }
    }

    public void send() {
        if(socket == null) {
            return;
        }
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                Payload payload = new Payload();
                System.out.println("Enter topic :");
                String topic = scanner.nextLine();
                payload.setTopic(topic);

                System.out.println("Enter message");
                String message = scanner.nextLine();
                payload.setMessage(message);

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonPayload = objectMapper.writeValueAsString(payload);

                byte[] data = jsonPayload.getBytes(StandardCharsets.UTF_8);
                System.out.println(data.length);
                outStream.writeInt(data.length);
                outStream.write(data);
                outStream.flush();
            } catch (IOException e) {
                System.out.println("Publisher cannot send the data");
            }
        }
    }

    public void close() {
        try {
            outStream.close();
            outStream.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}