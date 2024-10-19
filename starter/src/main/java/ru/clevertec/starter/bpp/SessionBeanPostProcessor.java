package ru.clevertec.starter.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import ru.clevertec.starter.annotation.Sessional;
import ru.clevertec.starter.service.impl.SessionInterceptor;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;

public class SessionBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (isHasSessionalAnnotation(bean)) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallback(beanFactory.getBean(SessionInterceptor.class));
            return isPresentDefaultConstructor(bean)
                    ? enhancer.create()
                    : enhancer.create(getNotDefaultConstructorArgTypes(bean), getNotDefaultConstructorArgs(bean));
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private boolean isHasSessionalAnnotation(Object bean) {
        return Arrays.stream(bean.getClass().getDeclaredMethods())
                .anyMatch(method -> method.isAnnotationPresent(Sessional.class));
    }

    private boolean isPresentDefaultConstructor(Object object) {
        return Arrays.stream(object.getClass().getConstructors())
                .anyMatch(constructor -> constructor.getParameterCount() == 0);
    }

    private Object[] getNotDefaultConstructorArgs(Object object) {
        Class<?>[] constructorArgTypes = getNotDefaultConstructorArgTypes(object);
        return Arrays.stream(constructorArgTypes)
                .map(beanFactory::getBean)
                .toArray();
    }

    private Class<?>[] getNotDefaultConstructorArgTypes(Object object) {
        return Arrays.stream(object.getClass().getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .map(Constructor::getParameterTypes)
                .orElseThrow(IllegalArgumentException::new);
    }


}
