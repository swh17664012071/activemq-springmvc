package com.etoak.service;

import com.alibaba.fastjson.JSONObject;
import com.etoak.bean.Email;
import com.etoak.bean.User;
import com.etoak.mapper.UserMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    ActiveMQConnectionFactory factory;

    /**
     * 添加用户
     * @param user
     * @return
     */
    @Transactional(isolation = Isolation.DEFAULT,
        propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class)
    public int addUser(User user) throws JMSException {

        String password = DigestUtils.md5Hex("123456");
        user.setPassword(password);
        int result = userMapper.addUser(user);

        Email email = new Email(user.getEmail(),
                "激活邮件",
                "请点击链接：http://localhost:8080/user/active/" + user.getName());

        // 发送消息到ActiveMQ
        // 参数一：队列名称y
        // 参数二：创建消息的接口]
       /* Connection connection = factory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
        Topic topic = session.createTopic("email");*/

        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.send("email", session1 -> {
         return  session1.createTextMessage(JSONObject.toJSONString(email));
        });
        return result;
    }

}
