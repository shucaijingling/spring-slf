package cn.shucai.a01;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        /*
          1.什么是BeanFactory
               - ApplicationContext的父接口
               - spring的核心容器，主要的ApplicationContext 实现都【组合】了它的功能
         */
        System.out.println(context);

        /*
          2.BeanFactory能干什么
               - 表面上只有getBean
               - 实际上控制反转，基本的依赖注入，直至Bean的生命周到的各个功能，都由它的实现类提供
         */
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        map.entrySet().stream().filter(e -> e.getKey().startsWith("component"))
                .forEach(e -> {
                    System.out.println(e.getKey() + ":" + e.getValue());
                });
        /**
         * messagesource 国际化
         */
        System.out.println(context.getMessage("hi", null, Locale.CHINA));
        System.out.println(context.getMessage("hi", null, Locale.ENGLISH));
        System.out.println(context.getMessage("hi", null, Locale.JAPANESE));

        /**
         * resources
         */
        //单个resource
        Resource[] resources = context.getResources("classpath:application.properties");
        for (Resource resource : resources) {
            System.out.println(resource);
        }

        //多个resource
        Resource[] resources2 = context.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource : resources2) {
            System.out.println(resource);
        }

        /**
         * getEnvironment
         */
        System.out.println(context.getEnvironment().getProperty("java_home"));
        System.out.println(context.getEnvironment().getProperty("server.port"));


        /**
         * 发布事件
         */
//        context.publishEvent(new UserRegisteredEvent(context));

        context.getBean(Component2.class).register();
    }
}
