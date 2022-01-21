package com.closememo.query.infra.messaging.publisher;

import com.closememo.query.infra.messaging.AckEvent;

public interface MessagePublisher {

  void publish(AckEvent ackEvent);
}
