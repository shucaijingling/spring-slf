package cn.shucai.a02;

import org.mockito.internal.SuppressSignatureCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.channels.ClosedSelectorException;

public class TestBeanFactory {

    public static final Logger log = LoggerFactory.getLogger(TestBeanFactory.class);

    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //生成一个bean定义
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();

        beanFactory.registerBeanDefinition("config", beanDefinition);

        //给BeanFactory中添加一些后置处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        //通过beanfactory的后置处理器，补充一些bean定义
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().forEach(beanFactoryPostProcessor -> {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });

        //bean后置处理器，针对bean的生命周期各个阶段进行拓展，比如@Autowired，@Resource等
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream()
                //通过比较器排序，可以变换执行顺序
                .sorted(beanFactory.getDependencyComparator())
                .forEach(/*beanFactory::addBeanPostProcessor*/
                beanPostProcessor -> {
                    System.out.println(">>>>>>>>>>>>>>>>>>>" + beanPostProcessor);
                    beanFactory.addBeanPostProcessor(beanPostProcessor);
                }
        );
        //输出
        //>>>>>>>>>>>>>>>>>>>org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@ffaa6af
        //>>>>>>>>>>>>>>>>>>>org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@53ce1329

        //比较器排序后输出
        //>>>>>>>>>>>>>>>>>>>org.springframework.context.annotation.CommonAnnotationBeanPostProcessor@957e06
        //>>>>>>>>>>>>>>>>>>>org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor@32502377

        for (String bdName : beanFactory.getBeanDefinitionNames()) {
//            log.debug("beanDefinition: {}", bd);

            System.out.println(bdName);
        }

        System.out.println(beanFactory.getBean(Bean1.class).getBean2());


    }

    @Configuration
    static class Config {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }

    static class Bean1 {
        private static final Logger log = LoggerFactory.getLogger(Bean1.class);

        public Bean1() {
            log.debug("构造 Bean1");
        }

        @Autowired
        private Bean2 bean2;

        public Bean2 getBean2() {
            return bean2;
        }
    }

    static class Bean2 {
        private static final Logger log = LoggerFactory.getLogger(Bean2.class);

        public Bean2() {
            log.debug("构造 Bean2");
        }

    }
}
