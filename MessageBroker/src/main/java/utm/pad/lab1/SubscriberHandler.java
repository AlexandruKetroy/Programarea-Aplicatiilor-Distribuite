package utm.pad.lab1;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;


public class SubscriberHandler extends Thread {
    private Socket subscriberSocket;
    private BlockingQueue<String> queue;

    public SubscriberHandler(BlockingQueue<String> queue, Socket subscriberSocket) {
        this.queue = queue;
        this.subscriberSocket = subscriberSocket;
    }

    public void run() {
        while (true) {
            try {
                DataInputStream subscriberInStream = new DataInputStream(subscriberSocket.getInputStream());
                DataOutputStream subscriberOutStream = new DataOutputStream(subscriberSocket.getOutputStream());
                String jsonMessage = "";
                String topic = subscriberInStream.readUTF();
                while (!jsonMessage.equals("bye")) {
                    String jsonPayload = queue.take();
                    ObjectMapper objectMapper = new ObjectMapper();
                    Payload payload = objectMapper.readValue(jsonPayload, Payload.class);
                    if (true) {
                        subscriberOutStream.writeUTF(jsonPayload);
                        subscriberOutStream.flush();
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
