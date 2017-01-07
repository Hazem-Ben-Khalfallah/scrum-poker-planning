package com.influans.sp.websocket;

import com.influans.sp.dto.WsRequest;
import com.influans.sp.enums.WsTypes;
import com.influans.sp.utils.JsonSerializer;
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
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WebSocketSender.class);

    @Autowired
    private WebSocketConfig config;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Send a message to listeners (browsers) which are subscribers on suitable
     * topic. If brandCode is missing, no message is sent
     *
     * @param topic : websocket topic@Autowired
     *              private WebSocketSender webSocketSender;
     * @param type request type
     * @param data  : message data
     * @return true if something has been sent through SimpMessagingTemplate ;
     * false otherwise
     * @throws MessagingException if a problem occurs during sending message
     */
    public boolean sendNotification(String topic, WsTypes type, Object data) {
        try {
            if (topic != null && data != null) {
                final String topicName = "/topic/" + topic;
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
