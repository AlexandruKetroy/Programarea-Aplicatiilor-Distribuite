package utm.pad.lab1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BrokerSocket {

    private ServerSocket publisherBrokerSocket;
//    private ServerSocket subscriberBrokerSocket;
    private BlockingQueue<String> queue = new LinkedBlockingQueue<>();
//    private MpmcArrayQueue<String> queue = new MpmcArrayQueue<>(10);

    public void accept(int publisherPort, int subscriberPort) {
        try {
            publisherBrokerSocket = new ServerSocket(publisherPort);
//            subscriberBrokerSocket = new ServerSocket(subscriberPort);
            int counter = 0;
//            BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
            while (true) {
                counter++;
                Socket publisherBroker = publisherBrokerSocket.accept();
                System.out.println(" >> " + "Publisher No:" + counter + " started!");
//                Socket subscriberBroker = subscriberBrokerSocket.accept();
//                System.out.println(" >> " + "Subscriber No:" + counter + " started!");
                PublisherHandler publisherHandler = new PublisherHandler(queue, publisherBroker);
                publisherHandler.start();
//                SubscriberHandler subscriberHandler = new SubscriberHandler(queue, subscriberBroker);
//                subscriberHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}