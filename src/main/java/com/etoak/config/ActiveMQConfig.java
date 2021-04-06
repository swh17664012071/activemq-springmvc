package com.etoak.config;

import com.sun.deploy.panel.JHighDPITable;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Session;

@Configuration
@PropertySource(value = "classpath:mq.properties")
public class ActiveMQConfig {

    @Value("${mq.username}")
    private String username;

    @Value("${mq.password}")
    private String password;

    @Value("${mq.brokerURL}")
    private String url;

    /** ActiveMQConnectionFactory */
    @Bean
    public ActiveMQConnectionFactory mqConnectionFactory() {
        return new ActiveMQConnectionFactory(username,
                password, url);
    }

    /** CachingConnectionFactory */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setTargetConnectionFactory(this.mqConnectionFactory());
        factory.setSessionCacheSize(30);
        return factory;
    }

    /** JmsTemplate: 用于发送JMS消息 */
    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(this.connectionFactory());
        // 开启服务质量控制 QOS values (deliveryMode, priority, timeToLive)
        jmsTemplate.setExplicitQosEnabled(true);
        // 持久化
        jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
        // 客户端签收消息
        jmsTemplate.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return jmsTemplate;
    }

}