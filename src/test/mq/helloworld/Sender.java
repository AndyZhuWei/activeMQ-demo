package test.mq.helloworld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;

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
        //第三步，通过Connection对象创建Session会话（上下文环境对象）




























    }
}
