package com.smartexlab.libraryapp.repository.util;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Insert into fields annotated with @InjectSql annotation
 * actual SQL queries from files in sql-folder with the
 * specified name.
 */
@Component
public class SqlInjectBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = bean.getClass().getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(InjectSql.class))
                .forEach(field -> {
                    String filePath = field.getAnnotation(InjectSql.class).value();
                    try {
                        String sql = IOUtils.toString(new ClassPathResource(filePath).getInputStream(),
                                Charset.defaultCharset());
                        field.setAccessible(true);
                        ReflectionUtils.setField(field, bean, sql);
                    } catch (IOException e) {
                        throw new BeanCreationException(beanName, "No such file:" + filePath);
                    }
                });
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
