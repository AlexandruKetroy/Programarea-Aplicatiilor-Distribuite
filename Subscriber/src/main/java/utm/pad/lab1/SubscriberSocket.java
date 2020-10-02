package utm.pad.lab1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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
        if (socket == null) {
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter topic :");
            String topic = br.readLine();

            Payload payload = new Payload();
            payload.setTopic(topic);
            payload.setType("subscriber");
            String jsonPayload = objectMapper.writeValueAsString(payload);

            byte[] data = jsonPayload.getBytes(StandardCharsets.UTF_8);
            outStream.writeInt(data.length);
            outStream.write(data);
            outStream.flush();
        } catch (IOException e) {
            System.out.println("Subscriber could not send the data");
        }
        try {
            while (true) {
                String jsonPayload = receive(inStream).orElseThrow(() -> new ClosedConnectionException("Closed connection"));
                Payload payload = objectMapper.readValue(jsonPayload, Payload.class);
                String msg = payload.getMessage();
                System.out.println("Message received: " + msg);
            }
        } catch (ClosedConnectionException e) {
            System.out.println(e.getMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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

    private Optional<String> receive(DataInputStream in) {
        StringBuilder dataString = null;
        try {
            int length = in.readInt();
            byte[] messageByte = new byte[length];
            dataString = new StringBuilder(length);
            boolean end = false;
            int totalBytesRead = 0;
            while (!end) {
                int currentBytesRead = in.read(messageByte);
                totalBytesRead = currentBytesRead + totalBytesRead;
                if (totalBytesRead <= length) {
                    dataString
                            .append(new String(messageByte, 0, currentBytesRead, StandardCharsets.UTF_8));
                } else {
                    dataString
                            .append(new String(messageByte, 0, length - totalBytesRead + currentBytesRead,
                                    StandardCharsets.UTF_8));
                }
                if (dataString.length() >= length) {
                    end = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Subscriber could not receive the data");
        }
        if (dataString == null) {
            return Optional.empty();
        }
        return Optional.of(dataString.toString());
    }
}
