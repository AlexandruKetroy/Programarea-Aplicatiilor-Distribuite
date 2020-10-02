package utm.pad.lab1;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;


public class SubscriberHandler extends Thread {
    private Socket subscriberSocket;
    private BlockingQueue<String> queue;

    public SubscriberHandler(BlockingQueue<String> queue, Socket subscriberSocket) {
        this.queue = queue;
        this.subscriberSocket = subscriberSocket;
    }

    public void run() {
        try {
            DataInputStream subscriberInStream = new DataInputStream(subscriberSocket.getInputStream());
            DataOutputStream subscriberOutStream = new DataOutputStream(subscriberSocket.getOutputStream());
            String topic = subscriberInStream.readUTF();
            String jsonMessage = "";
            System.out.println("topic:" + topic);
            while (true) {
                String jsonPayload = queue.take();
                System.out.println(jsonPayload);
                ObjectMapper objectMapper = new ObjectMapper();
                Payload payload = objectMapper.readValue(jsonPayload, Payload.class);
                if (true) {
                    byte[] data = jsonPayload.getBytes(StandardCharsets.UTF_8);
                    subscriberOutStream.writeInt(data.length);
                    subscriberOutStream.write(data);
                    subscriberOutStream.flush();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
