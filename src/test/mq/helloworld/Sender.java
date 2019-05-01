package test.mq.helloworld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {

    public static void main(String[] args) throws Exception {
        //第一步:建立ConnetionFactory工厂对象，需要输入用户名，密码以及要连接的地址。
        //均使用默认即可。默认端口号为：tcp://localhost:61616
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                "tcp://localhost:61616");
        //第二步：通过ConnectionFactory工厂对象我们创建一个Connection连接，并且调用Connection的
        //start方法开启连接，Connection默认是关闭的
        Connection connection = connectionFactory.createConnection();
        connection.start();
        //第三步，通过Connection对象创建Session会话（上下文环境对象）,用于接收消息
        //参数1为是否启用事务，参数2位签收模式，一般我们设置自动签收
        //Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

        //使用事务进行测试
        //Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

        //使用Client签收的方式
        Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);


        //第四步，通过Session创建Destination对象，指的是一个客户端用来指定生产消息目标
        //和消费消息来源的对象，在PTP模式中，Destination被称为Queue,在发布订阅模式中称为Topic
        Destination destination = session.createQueue("queue1");
        //第五步：我们需要通过Session对象创建消息的发送和接收对象（生产者和接收者）MessageProducer/MessageConsumer
       // MessageProducer messageProducer = session.createProducer(destination);

        MessageProducer messageProducer = session.createProducer(null);
        //第六步：我们可以使用MessageProducer的setDeliveryMode方法为其设置持久化特性
       // messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        //第七步：最后我们使用JMS规范中的TextMessage形式创建数据（通过Session对象），并使用
        //MessageProducer的send方法发送数据，同理客户端使用receive方法进行接收
        for(int i=1;i<=5;i++){
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("我是消息内容,id为："+i);
            //messageProducer.send(textMessage);

            //第一个参数：目的地
            //第二个参数：消息
            //第三个参数：是否持久化
            //第四个参数：优先级（0~9 0~4表示普通，5~9表示加急，默认为4）
            //第五个参数；消息在MQ上存放的有效期
            messageProducer.send(destination,textMessage,DeliveryMode.NON_PERSISTENT,i,1000*60*2);
            System.out.println("生产者："+textMessage.getText());
        }

        //使用事务进行测试
        session.commit();

        if(connection!=null) {
            connection.close();
        }

    }
}
