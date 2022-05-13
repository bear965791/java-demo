import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
    private final static String QUEUE_NAME = "FIRST_QUEUE";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost"); // hostname or IP address
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) { //要使用的API都在這
            //declare a queue for us to send to
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //we can publish a message to the queue
            String message = "Hello World!";
            //Declaring a queue is idempotent,it will only be created if it doesn't exist already.
            /* HTTP Idempotent Methods (GET，HEAD，OPTIONS，TRACE及PUT, DELETE)
               若client發出一次或多次同樣的請求對server的影響結果不變，則此請求方法則為Idempotent Methods。
               也就是說同樣的請求即使不小心送了兩次，也不用擔心對server端造成不同的影響。
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}