package utm.pad.lab1;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PublisherSocket {
    private Socket socket;
    private DataOutputStream outStream;

    public void connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            outStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Connection refused: Publisher could not connect to broker");
        }
    }

    public void send() {
        if (socket == null) {
            return;
        }
        try {
            while (true) {
                Scanner scanner = new Scanner(System.in);
                Payload payload = new Payload();
                System.out.println("Enter topic :");
                String topic = scanner.nextLine();
                payload.setTopic(topic);

                System.out.println("Enter message");
                String message = scanner.nextLine();
                payload.setMessage(message);
                payload.setType("publisher");

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonPayload = objectMapper.writeValueAsString(payload);

                byte[] data = jsonPayload.getBytes(StandardCharsets.UTF_8);
                outStream.writeInt(data.length);
                outStream.write(data);
                outStream.flush();
            }
        } catch (IOException e) {
            System.out.println("Publisher could not send the data");
        }
    }

    public void close() {
        try {
            outStream.close();
            outStream.close();
            socket.close();
        } catch (Exception e) {
        }
    }
}