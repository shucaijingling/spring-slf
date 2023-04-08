package cn.shucai.a01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Component1 {

    private static final Logger log = LoggerFactory.getLogger(Component1.class);


    /**
     * 监听事件，context中任意位置都可以监听
     */
    @EventListener
    public void aaa(/*入参为监听的事件类型*/UserRegisteredEvent event) {
        log.debug("{}", event);
        log.debug("发送消息");
    }
}
