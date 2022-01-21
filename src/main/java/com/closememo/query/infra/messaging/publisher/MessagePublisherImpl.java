package com.closememo.query.infra.messaging.publisher;

import com.closememo.query.infra.helper.JsonUtils;
import com.closememo.query.infra.messaging.AckEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessagePublisherImpl implements MessagePublisher {

  private final ApplicationContext context;

  public MessagePublisherImpl(ApplicationContext context) {
    this.context = context;
  }

  @Override
  public void publish(AckEvent ackEvent) {
    String channelName = ackEvent.getClass().getSimpleName();
    send(channelName, new GenericMessage<>(ackEvent));
  }

  private void send(String channelName, Message<?> message) {
    log.debug("message send: {} : {}", channelName, JsonUtils.toJson(message));
    MessageChannel messageChannel = (MessageChannel) context.getBean(channelName);
    messageChannel.send(message);
  }
}
