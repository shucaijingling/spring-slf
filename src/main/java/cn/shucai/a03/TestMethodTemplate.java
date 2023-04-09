package cn.shucai.a03;

import java.util.ArrayList;
import java.util.List;

public class TestMethodTemplate {

    public static void main(String[] args) {
        MyBeanFactory beanFactory = new MyBeanFactory();
        beanFactory.addProcessor(bean -> System.out.println("依赖注入增强@Autowired"));
        beanFactory.addProcessor(bean -> System.out.println("依赖注入增强@Resource"));
        beanFactory.getBean();
    }

    //模板方法 Template Method Pattern
    static class MyBeanFactory {
        public Object getBean() {
            Object bean = new Object();
            System.out.println("实例化 " + bean);
            System.out.println("依赖注入 " + bean);
            //增加后置处理器
            for (PostProcessor processor : processors) {
                processor.inject(bean);
            }
            System.out.println("初始化 " + bean);

            return bean;
        }
        List<PostProcessor> processors = new ArrayList<>();
        public void addProcessor(PostProcessor processor) {
            processors.add(processor);
        }
    }
    static interface PostProcessor {
        void inject(Object bean);
    }
}
