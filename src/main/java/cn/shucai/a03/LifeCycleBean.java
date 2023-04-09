package cn.shucai.a03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class LifeCycleBean {
    public static final Logger log = LoggerFactory.getLogger(LifeCycleBean.class);



    public LifeCycleBean() {
        log.debug("construct");
    }

    @Autowired
    public void autowire(@Value("${env.val}") String name) {
        log.debug("依赖注入：{}", name);
    }

    @PostConstruct
    public void init() {
        log.debug("初始化");
    }

    @PreDestroy
    public void destroy() {
        log.debug("销毁");
    }
}
