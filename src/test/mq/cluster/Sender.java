package test.mq.cluster;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {

    public static void main(String[] args) {
        try {
            //第一步：建立ConnectionFactory工厂对象，需要填入用户名，密码，以及要连接的地址，均使用默认即可。
            //默认端口为tcp://localhost:61616
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    ActiveMQConnectionFactory.DEFAULT_USER,
                    ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                    "failover:(tcp://192.168.7.124:51511,tcp://192.168.7.124:51512,tcp://192.168.7.124:51513)?Randomize=false");
            //第二步：通过ConnectionFactory工厂对象我们创建一个Connection连接，并且调用Connection的start方法开启
            //连接，Connection默认是关闭的。
            Connection connection = connectionFactory.createConnection();
            connection.start();

            //第三步：通过Connection对象创建Session会话（上下文环境对象），用于接收消息，参数配置1位是否启用事务，参数配置2位签收模式，
            //一般我们设置自动签收
            Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);


            //第四步：通过Session创建Destination对象，指的是一个客户端用来指定生产消息目标和消费消息来源的对象，在PTP模式中，
            //Destination被称作Queue即队列：
            Destination destination = session.createQueue("first");

            //第五步：我们需要通过Session对象创建消息的发送和接收对象（生产者和消费者）MessageProducer/MessageConsumer
            MessageProducer producer = session.createProducer(null);

            //第六步：我们可以使用MessageProducer的setDeliveryMode方法为其设置持久化特性和非持久化特性
            //producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            //第七步：最后我们使用JMS规范的TextMessage形式创建数据，（通过Session对象），并用MessageProducer
            //的send的方法发送数据，同理客户端使用reveiver方法接收数据
            for(int i=1;i<=500000;i++){
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText("我是消息内容,id为："+i);
                //messageProducer.send(textMessage);

                //第一个参数：目的地
                //第二个参数：消息
                //第三个参数：是否持久化
                //第四个参数：优先级（0~9 0~4表示普通，5~9表示加急，默认为4）
                //第五个参数；消息在MQ上存放的有效期
                producer.send(destination,textMessage,DeliveryMode.NON_PERSISTENT,0,1000*60);
                System.out.println("生产者："+textMessage.getText());
                Thread.sleep(1000);
            }

            if(connection!=null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
