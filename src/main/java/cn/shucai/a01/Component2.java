package cn.shucai.a01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Component2 {

    @Autowired
    private ApplicationEventPublisher event;

    private static final Logger log = LoggerFactory.getLogger(Component2.class);

    public void register() {
        log.debug("用户注册");
        event.publishEvent(new UserRegisteredEvent(this));
    }
}
