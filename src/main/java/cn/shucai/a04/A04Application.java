package cn.shucai.a04;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

public class A04Application {

    public static void main(String[] args) {

        //GenericApplicationContext是一个干净的容器
        GenericApplicationContext context = new GenericApplicationContext();

        //用原始方法注册三个bean
        context.registerBean("bean1", Bean1.class);
        context.registerBean("bean2", Bean2.class);
        context.registerBean("bean3", Bean3.class);

        //添加后置处理器才能处理解析对应注解的操作
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());//解析@Value注解
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);//解析@Autowired注解

        context.registerBean(CommonAnnotationBeanPostProcessor.class);// @Resource @PostConstruct @PreDestroy

        //初始化容器
        context.refresh();//执行BeanFactory后置处理器 ， 添加bean后置处理器， 初始化所有单例

        //销毁容器
        context.close();
    }
}
