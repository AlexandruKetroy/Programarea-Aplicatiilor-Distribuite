package utm.pad.lab1;

public class Subscriber {
    public static void main(String[]args) {
        SubscriberSocket subscriberSocket = new SubscriberSocket();
        subscriberSocket.connect("127.0.0.1",8888);
        subscriberSocket.send();
        subscriberSocket.close();
    }
}
