package com.blacknebula.scrumpoker.websocket;

import com.blacknebula.scrumpoker.config.WebSocketConfig;
import com.blacknebula.scrumpoker.dto.WsRequest;
import com.blacknebula.scrumpoker.enums.WsTypes;
import com.blacknebula.scrumpoker.utils.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * WebSocket push service when modifications occur on Onboarding counters for a
 * given brand
 */
@Component
@Profile("!test")
public class WebSocketSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketSender.class);

    @Autowired
    private WebSocketConfig config;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Send a message to listeners (browsers) which are subscribers on suitable
     * topic. If brandCode is missing, no message is sent
     *
     * @param topic : websocket topic
     * @param type  request type
     * @param data  : message data
     * @return true if something has been sent through SimpMessagingTemplate ;
     * false otherwise
     * @throws MessagingException if a problem occurs during sending message
     * @should return true after sending a notification
     * @should return false if topic is  null
     * @should return false if type is  null
     * @should return false if data is  null
     */
    public boolean sendNotification(String topic, WsTypes type, Object data) {
        try {
            if (topic != null && type != null && data != null) {
                final String topicName = config.getTopicPrefix() + "/" + topic;
                final WsRequest request = new WsRequest(type, data);
                messagingTemplate.convertAndSend(topicName, request);
                LOGGER.info("[WS] [topic: {}] sent data: {}", topicName, JsonSerializer.serialize(request));
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Error while sending WS notification ", e);
        }
        return false;
    }
}
