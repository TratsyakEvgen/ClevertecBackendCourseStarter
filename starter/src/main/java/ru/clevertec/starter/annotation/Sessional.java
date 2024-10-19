package ru.clevertec.starter.annotation;

import ru.clevertec.starter.service.BlackListChecker;
import ru.clevertec.starter.service.SessionSupplier;
import ru.clevertec.starter.service.impl.HttpSessionSupplier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sessional {
    Class<? extends SessionSupplier> supplier() default HttpSessionSupplier.class;

    Class<? extends BlackListChecker>[] blackLists() default {};
}
