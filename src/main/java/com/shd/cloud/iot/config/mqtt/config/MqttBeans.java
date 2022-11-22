package com.shd.cloud.iot.config.mqtt.config;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.shd.cloud.iot.models.SensorHistory;
import com.shd.cloud.iot.payload.request.SensorHistoryRequest;
import com.shd.cloud.iot.sevices.OperatorService;
import com.shd.cloud.iot.sevices.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MqttBeans {
    private final SensorService sensorService;
    private final OperatorService operatorService;

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
        return message -> {
            String topic = Objects.requireNonNull(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC)).toString();
            String payload = message.getPayload().toString();
            if (topic.startsWith("sensor/")) {
                try {
                    String[] t = topic.split("/");
                    List<SensorHistory> sh = sensorService.searchHistory(Long.valueOf(t[1]));
                    if (sh.size() > 0) {
                        SensorHistory lastHistory = sh.get(sh.size() -1);
                        String data = lastHistory.getData();
                        log.info("data   --> {}", data);
                        if (!data.equals(payload)) {
                            SensorHistoryRequest sr = new SensorHistoryRequest(payload, t[2]);
                            sensorService.saveSensorHistory(Long.valueOf(t[1]), sr);
                        }else {
                            sensorService.changeLastUpdateHistory(lastHistory.getId());
                        }
                    } else {
                        SensorHistoryRequest sr = new SensorHistoryRequest(payload, t[2]);
                        sensorService.saveSensorHistory(Long.valueOf(t[1]), sr);
                    }
                } catch (Exception e) {
                   log.error(e.getMessage());
                }
            }
            if (topic.startsWith("healthCheck/")){
                try {
                    String[] t = topic.split("/");
                    operatorService.setHealthCheckDate(Long.valueOf(t[1]),t[2], payload);
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
            log.info("message: {}",message.getPayload());
            log.info("message header: {}",message.getHeaders());
            log.info("message topic: {}", topic);
        };
    }

}
