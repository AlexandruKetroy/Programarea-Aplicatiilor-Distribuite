package utm.pad.lab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class PublisherHandler extends Thread {
    private Socket publisherSocket;
    private BlockingQueue<String> queue;

    public PublisherHandler(BlockingQueue<String> blockingQueue, Socket publisherSocket) {
        this.queue = blockingQueue;
        this.publisherSocket = publisherSocket;
    }

    public void run() {
        while (true) {
            try {
                DataInputStream publisherInStream = new DataInputStream(publisherSocket.getInputStream());
                DataOutputStream publisherOutStream = new DataOutputStream(publisherSocket.getOutputStream());
                String jsonMessage = "";
                while (!jsonMessage.equals("bye")) {
                    jsonMessage = receive(publisherInStream);
                    queue.put(jsonMessage);
                    System.out.println("----------------------------------------------" + jsonMessage);
                }
//                publisherInStream.close();
//                publisherOutStream.close();
//                publisherSocket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String receive(DataInputStream in) {
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
            e.getMessage();
        }
        return dataString.toString();
    }
}
