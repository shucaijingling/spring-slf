package cn.shucai.a04;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Method;

//AutowiredAnnotationBeanPostProcessor
public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean2", new Bean2());//成品bean，不走创建过程，依赖注入，初始化
        beanFactory.registerSingleton("bean3", new Bean3());
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());//@Value
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders); // 解析 ${}

        //1. 查找哪些属性、方法加了@Autowired，这称之为 InjectionMetadata
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        //设置beanFactory，处理一些依赖关系的bean：比如，这里使用bean1为例，
        // 如果给bean1单独添加后置处理器AutowiredAnnotationBeanPostProcessor，此时，bean1中有bean2属性，此时就需要beanfactory来处理bean2
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
//        System.out.println(bean1);
//        processor.postProcessProperties(null, bean1, "bean1");
//        System.out.println(bean1);
        Method findAutowiringMetadata = processor.getClass().getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
        //获取bean1 上加了 @Value @Autowired 的 成员变量，方法参数
        InjectionMetadata metadata = (InjectionMetadata) findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);
        System.out.println(metadata);

        //2. 调用InjectionMetadata进行依赖注入， 注入按照类型查找值
        metadata.inject(bean1, "bean1", null);
        System.out.println(bean1);
    }
}
