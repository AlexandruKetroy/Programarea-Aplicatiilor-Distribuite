package utm.pad.lab1;

public class Publisher {
    public static void main(String[]args) {
        PublisherSocket publisherSocket = new PublisherSocket();
        publisherSocket.connect("127.0.0.1",9000);
        publisherSocket.send();
        publisherSocket.close();
    }
}
