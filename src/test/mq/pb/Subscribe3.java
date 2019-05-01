package test.mq.pb;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Subscribe3 {
    //1.连接工厂
    private ConnectionFactory connectionFactory;

    //2.连接对象
    private Connection connection;

    //3.Session对象
    private Session session;

    //4.消费者
    private MessageConsumer messageConsumer;

    //5目标地址
    private Destination destination;

    public Subscribe3() {
        try {
            this.connectionFactory = new ActiveMQConnectionFactory(
                    ActiveMQConnectionFactory.DEFAULT_USER,
                    ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                    "tcp://localhost:61616");
            this.connection = this.connectionFactory.createConnection();
            this.connection.start();
            this.session = this.connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
            this.destination=this.session.createTopic("topic1");
            //创建消费者的时候发生了变化
            this.messageConsumer=this.session.createConsumer(this.destination);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void receiver() {
        try {
            this.messageConsumer.setMessageListener(new Listener());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    class Listener implements MessageListener {
        @Override
        public void onMessage(Message message) {
            try {
                if(message instanceof TextMessage) {
                    System.out.println("subscribe3接收到的消息："+((TextMessage) message).getText());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Subscribe3 c = new Subscribe3();
        c.receiver();
    }
}
