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
            /*
             * 宣告（建立）佇列
             * 引數1：佇列名稱
             * 引數2：為true時server重啟佇列不會消失
             * 引數3：佇列是否是獨佔的，如果為true只能被一個connection使用，其他連線建立時會丟擲異常
             * 引數4：佇列不再使用時是否自動刪除（沒有連線，並且沒有未處理的訊息)
             * 引數5：建立佇列時的其他引數
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //we can publish a message to the queue
            String message = "Hello World!";
            //Declaring a queue is idempotent,it will only be created if it doesn't exist already.
            /* HTTP Idempotent Methods (GET，HEAD，OPTIONS，TRACE及PUT, DELETE)
               若client發出一次或多次同樣的請求對server的影響結果不變，則此請求方法則為Idempotent Methods。
               也就是說同樣的請求即使不小心送了兩次，也不用擔心對server端造成不同的影響。
             */
            /*
             * 向server釋出一條訊息
             * 引數1：exchange名字，若為空則使用預設的exchange
             * 引數2：routing key
             * 引數3：其他的屬性
             * 引數4：訊息體
             * RabbitMQ預設有一個exchange，叫default exchange，它用一個空字串表示，它是direct exchange型別，
             * 任何發往這個exchange的訊息都會被路由到routing key的名字對應的佇列上，如果沒有對應的佇列，則訊息會被丟棄
             */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}