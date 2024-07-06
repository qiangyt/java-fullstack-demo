package io.github.qiangyt.common.misc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import io.github.qiangyt.common.err.InternalError;

/**
 * Tool for obtaining Springframework Bean instances in particular scenarios. In most cases, use annotations
 * like @Autowired instead.
 */
@Component
public class SpringBeanHolder implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return context.getBean(name, clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (context != null) {
            throw new InternalError("context already initialized");
        }
        SpringBeanHolder.context = applicationContext;
    }

}
