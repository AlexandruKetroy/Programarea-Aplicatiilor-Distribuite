package utm.pad.lab1;

public class Broker {
    public static void main(String[]args) {
        BrokerSocket brokerSocket = new BrokerSocket();
        brokerSocket.accept(9000,8888);
    }
}
