package ru.clevertec.starter.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import ru.clevertec.starter.annotation.Sessional;
import ru.clevertec.starter.dto.Session;
import ru.clevertec.starter.dto.SessionRequest;
import ru.clevertec.starter.exception.SessionException;
import ru.clevertec.starter.service.BlackListChecker;
import ru.clevertec.starter.service.SessionSupplier;

import java.lang.reflect.Method;
import java.util.Arrays;

public class SessionInterceptor implements MethodInterceptor, BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        boolean hasAnnotation = method.isAnnotationPresent(Sessional.class);
        if (hasAnnotation) {
            SessionRequest sessionRequest = getSessionRequest(args);
            Sessional sessional = method.getDeclaredAnnotation(Sessional.class);
            checkBlackLists(sessional.blackLists(), sessionRequest.getLogin());
            Session session = getSession(sessional.supplier(), sessionRequest);
            sessionRequest.setSession(session);
        }
        return proxy.invokeSuper(obj, args);
    }

    private Session getSession(Class<? extends SessionSupplier> supplierClass, SessionRequest sessionRequest) {
        SessionSupplier sessionSupplier = beanFactory.getBean(supplierClass);
        return sessionSupplier.getSession(sessionRequest)
                .or(() -> sessionSupplier.crateSession(sessionRequest))
                .orElseThrow(() -> new SessionException("Can not get session"));
    }

    private SessionRequest getSessionRequest(Object[] args) {
        return (SessionRequest) Arrays.stream(args)
                .filter(arg -> arg instanceof SessionRequest)
                .findFirst()
                .orElseThrow(() -> new SessionException("Can not get argument SessionRequest.class"));
    }

    private void checkBlackLists(Class<? extends BlackListChecker>[] checkers, String login) {
        Arrays.stream(checkers)
                .map(beanFactory::getBean)
                .forEach(blackListChecker -> blackListChecker.check(login));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
