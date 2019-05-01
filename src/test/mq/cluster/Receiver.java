package test.mq.cluster;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Receiver {
    public static void main(String[] args) throws Exception {
        //第一步:建立ConnetionFactory工厂对象，需要输入用户名，密码以及要连接的地址。
        //均使用默认即可。默认端口号为：tcp://localhost:61616
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "failover:(tcp://192.168.7.124:51511,tcp://192.168.7.124:51512,tcp://192.168.7.124:51513)?Randomize=false\"");
        //第二步：通过ConnectionFactory工厂对象我们创建一个Connection连接，并且调用Connection的
        //start方法开启连接，Connection默认是关闭的
        Connection connection = connectionFactory.createConnection();
        connection.start();
        //第三步，通过Connection对象创建Session会话（上下文环境对象）,用于接收消息
        //参数1为是否启用事务，参数2位签收模式，一般我们设置自动签收
       // Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

        Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);

        //第四步，通过Session创建Destination对象，指的是一个客户端用来指定生产消息目标
        //和消费消息来源的对象，在PTP模式中，Destination被称为Queue,在发布订阅模式中称为Topic
        Destination destination = session.createQueue("first");
        //第五步：我们需要通过Session对象创建消息的发送和接收对象（生产者和接收者）MessageProducer/MessageConsumer
        MessageConsumer messageConsumer = session.createConsumer(destination);
        while(true) {
            TextMessage textMessage = (TextMessage)messageConsumer.receive();
            //手工去签收消息，另起一个线程（TCP）去通知我们的MQ服务确认签收
            textMessage.acknowledge();
            if(textMessage == null) {
                break;
            }
            System.out.println("收到的内容："+textMessage.getText());
        }

        if(connection!=null) {
            connection.close();
        }

    }
}
