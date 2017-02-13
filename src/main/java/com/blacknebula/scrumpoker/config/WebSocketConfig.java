package com.blacknebula.scrumpoker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Value("${websocket.topics.prefix}")
    private String topicPrefix;

    @Value("${websocket.transport.endpoint}")
    private String endPoint;

    @Value("${websocket.application.destinationPrefix}")
    private String destinationPrefix;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(endPoint).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(topicPrefix);
        config.setApplicationDestinationPrefixes(destinationPrefix);
    }

    public String getTopicPrefix() {
        return topicPrefix;
    }
}