package com.shd.cloud.iot.mqtt.config;

import java.util.List;

import com.shd.cloud.iot.models.SensorHistory;
import com.shd.cloud.iot.payload.request.SensorHistoryRequest;
import com.shd.cloud.iot.sevices.SensorService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
public class MqttBeans {
    @Autowired
    private SensorService sensorService;

    @Value("${message.broker.host}")
    private String host;
    @Value("${message.broker.port}")
    private String port;

    private static final String MQTT_PUBLISHER_ID = "spring-server";

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[] { "tcp://" + host + ":" + port });
        // options.setUserName("admin");
        // String pass = "12345678";
        // options.setPassword(pass.toCharArray());
        options.setCleanSession(true);

        factory.setConnectionOptions(options);

        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT Information channels （ producer ）
     **/
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
 
    /**
     * MQTT Message handler （ producer ）
     **/
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MQTT_PUBLISHER_ID, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("defaultTopic");
        return messageHandler;
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn",
                mqttClientFactory(), "#");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                String payload = message.getPayload().toString();
                if (topic.startsWith("sensor/")) {
                    try {
                        String t[] = topic.split("/");
                        System.out.println("This is the topic");
                        List<SensorHistory> sh = sensorService.searchHistory(Long.valueOf(t[1]), null);
                        if (sh.size() > 0) {
                            String data = sh.get(sh.size() - 1).getData();
                            System.out.println("data   -->" + data);
                            if (!data.equals(payload)) {
                                System.out.println("in if");
                                SensorHistoryRequest sr = new SensorHistoryRequest(payload, t[2]);
                                sensorService.saveSensorHistory(Long.valueOf(t[1]), sr);
                            }
                        } else {
                            SensorHistoryRequest sr = new SensorHistoryRequest(payload, t[2]);
                            sensorService.saveSensorHistory(Long.valueOf(t[1]), sr);
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
                 System.out.println("message: " + message.getPayload());
                 System.out.println("message header: " + message.getHeaders());
                 System.out.println("message topic: " + topic);

            }

        };
    }

}
