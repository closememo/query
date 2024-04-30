package com.closememo.query.config.messaging.integration;

import static org.reflections.scanners.Scanners.SubTypes;

import com.closememo.query.config.messaging.kafka.KafkaMessageConverter;
import com.closememo.query.infra.messaging.AckEvent;
import com.closememo.query.infra.messaging.DomainEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

@Slf4j
@Configuration
public class IntegrationConfig {

  @Bean
  public KafkaMessageDrivenChannelAdapter<?, ?> kafkaMessageDrivenAdapter(
      @Qualifier("kafkaListenerContainerFactory") KafkaListenerContainerFactory<?> kafkaListenerContainerFactory,
      @Qualifier("kafkaMessageConverter") KafkaMessageConverter kafkaMessageConverter) {

    Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.forClass(DomainEvent.class))
        .setScanners(SubTypes));

    String[] topics = reflections.getSubTypesOf(DomainEvent.class).stream()
        .map(Class::getSimpleName)
        .toArray(String[]::new);
    ConcurrentMessageListenerContainer<?, ?> container
        = (ConcurrentMessageListenerContainer<?, ?>) kafkaListenerContainerFactory.createContainer(topics);
    KafkaMessageDrivenChannelAdapter<?, ?> kafkaMessageDrivenChannelAdapter
        = new KafkaMessageDrivenChannelAdapter<>(container);
    kafkaMessageDrivenChannelAdapter.setMessageConverter(kafkaMessageConverter);
    kafkaMessageDrivenChannelAdapter.setOutputChannel(inboundKafkaMessageChannel());

    return kafkaMessageDrivenChannelAdapter;
  }

  @Bean
  public MessageHandler ackEventHandler(
      @Qualifier("kafkaObjectMapper") ObjectMapper kafkaObjectMapper,
      KafkaTemplate<String, String> kafkaTemplate) {

    return message -> {
      AckEvent payload = (AckEvent) message.getPayload();
      try {
        Message<String> kafkaMessage = MessageBuilder
            .withPayload(kafkaObjectMapper.writeValueAsString(payload))
            .setHeader(KafkaHeaders.TOPIC, payload.getClass().getSimpleName())
            .setHeader(KafkaHeaders.MESSAGE_KEY, payload.getAggregateId())
            .build();
        kafkaTemplate.send(kafkaMessage);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new IllegalArgumentException();
      }
    };
  }

  @Bean
  public MessageChannel inboundKafkaMessageChannel() {
    return new DirectChannel();
  }

  @Bean
  public IntegrationFlow routeKafkaInboundMessage() {
    return IntegrationFlows.from(inboundKafkaMessageChannel())
        .route(router())
        .get();
  }

  private HeaderValueRouter router() {
    return new HeaderValueRouter(KafkaHeaders.RECEIVED_TOPIC);
  }

  @Component
  public static class Initializer {

    private final GenericWebApplicationContext context;
    private final ThreadPoolTaskExecutor messageTaskExecutor;
    private final MessageHandler ackEventHandler;

    public Initializer(GenericWebApplicationContext context,
        @Qualifier("messageTaskExecutor") ThreadPoolTaskExecutor messageTaskExecutor,
        @Qualifier("ackEventHandler") MessageHandler ackEventHandler) {
      this.context = context;
      this.messageTaskExecutor = messageTaskExecutor;
      this.ackEventHandler = ackEventHandler;
    }

    @PostConstruct
    public void init() {
      registerDomainEventChannels(context, messageTaskExecutor);
      registerAckEventChannel(context, messageTaskExecutor, ackEventHandler);
    }

    private void registerDomainEventChannels(GenericWebApplicationContext context,
        ThreadPoolTaskExecutor messageTaskExecutor) {

      Reflections reflections = new Reflections(new ConfigurationBuilder()
          .setUrls(ClasspathHelper.forClass(DomainEvent.class))
          .setScanners(SubTypes));

      reflections.getSubTypesOf(DomainEvent.class)
          .forEach(aClass ->
              context.registerBean(aClass.getSimpleName(), PublishSubscribeChannel.class,
                  () -> new PublishSubscribeChannel(messageTaskExecutor)));
    }

    private void registerAckEventChannel(GenericWebApplicationContext context,
        ThreadPoolTaskExecutor messageTaskExecutor, MessageHandler ackMessageHandler) {

      context.registerBean("AckEvent", PublishSubscribeChannel.class,
          () -> new PublishSubscribeChannel(messageTaskExecutor));
      PublishSubscribeChannel eventChannel = context
          .getBean("AckEvent", PublishSubscribeChannel.class);
      context.registerBean("Broadcast-AckEvent", EventDrivenConsumer.class,
          eventChannel, ackMessageHandler);
    }
  }
}
