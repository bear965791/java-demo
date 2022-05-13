import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

// keep the consumer running to listen for messages and print them out
public class Recv {
    //declare the queue from which we're going to consume.
    // Note this matches up with the queue that send publishes to.
    private final static String QUEUE_NAME = "FIRST_QUEUE";

    public static void main(String[] argv) throws Exception {
        //接收方不需要try-with-resource自動關閉，他必須維持alive，
        //因為the consumer is listening asynchronously for messages to arrive
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //declare the queue here, as well. Because we might start the consumer before the publisher,
        // we want to make sure the queue exists before we try to consume messages from it
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        //Since it will push us messages asynchronously, we provide a callback in the form of an object that will buffer the messages until we're ready to use them.
        // That is what a DeliverCallback subclass does.
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        /*
         * 監聽佇列
         * 引數1:佇列名稱
         * 引數2：是否傳送ack包，不傳送ack訊息會持續在服務端儲存，直到收到ack。  可以通過channel.basicAck手動回覆ack
         * 引數3：消費者
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}