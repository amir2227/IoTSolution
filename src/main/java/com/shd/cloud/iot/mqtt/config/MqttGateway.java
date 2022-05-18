package com.shd.cloud.iot.mqtt.config;


import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * mqtt Send a message 
 * （defaultRequestChannel = "mqttOutboundChannel"  Corresponding config To configure ）
 * **/
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {
    
    /**
     *  Send a message to MQTT The server 
     *
     * @param data  Text sent 
     */
    void sendToMqtt(String data);

    /**
     *  Send a message to MQTT The server 
     *
     * @param topic  The theme 
     * @param payload  Message body 
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic,
                    String payload);

    /**
     *  Send a message to MQTT The server 
     *
     * @param topic  The theme 
     * @param qos  Several mechanisms for message processing .
     * 0  It means that the subscriber does not receive the message and will not send it again , The news will be lost .
     * 1  It means that it will try to retry , Until you receive the message , However, this situation may cause subscribers to receive multiple repeated messages .
     * 2  One more weight removal action , Ensure that the subscriber receives a message once .
     * @param payload  Message body 
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic,
                    @Header(MqttHeaders.QOS) int qos,
                    String payload);

}
