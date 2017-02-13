package com.blacknebula.scrumpoker.websocket;

import com.blacknebula.scrumpoker.ApplicationTest;
import com.blacknebula.scrumpoker.enums.WsTypes;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author hazem
 */
public class WebSocketSenderTest extends ApplicationTest {

    @Autowired
    private WebSocketSender webSocketSender;

    @Before
    public void setUp() {
        when(webSocketSender.sendNotification(any(), any(), any())).thenCallRealMethod();
    }

    /**
     * @verifies return true after sending a notification
     * @see WebSocketSender#sendNotification(String, WsTypes, Object)
     */
    @Test
    public void sendNotification_shouldReturnTrueAfterSendingANotification() throws Exception {
        // when
        final boolean isNotificationSent = webSocketSender.sendNotification("topic", WsTypes.STORY_ADDED, "test notification");
        // then
        Assertions.assertThat(isNotificationSent).isTrue();
    }

    /**
     * @verifies return false if topic is  null
     * @see WebSocketSender#sendNotification(String, WsTypes, Object)
     */
    @Test
    public void sendNotification_shouldReturnFalseIfTopicIsNull() throws Exception {
        // when
        final boolean isNotificationSent = webSocketSender.sendNotification(null, WsTypes.STORY_ADDED, "test notification");
        // then
        Assertions.assertThat(isNotificationSent).isFalse();
    }

    /**
     * @verifies return false if type is  null
     * @see WebSocketSender#sendNotification(String, WsTypes, Object)
     */
    @Test
    public void sendNotification_shouldReturnFalseIfTypeIsNull() throws Exception {
        // when
        final boolean isNotificationSent = webSocketSender.sendNotification("topic", null, "test notification");
        // then
        Assertions.assertThat(isNotificationSent).isFalse();
    }

    /**
     * @verifies return false if data is  null
     * @see WebSocketSender#sendNotification(String, WsTypes, Object)
     */
    @Test
    public void sendNotification_shouldReturnFalseIfDataIsNull() throws Exception {
        // when
        final boolean isNotificationSent = webSocketSender.sendNotification("topic", WsTypes.STORY_ADDED, null);
        // then
        Assertions.assertThat(isNotificationSent).isFalse();
    }
}
