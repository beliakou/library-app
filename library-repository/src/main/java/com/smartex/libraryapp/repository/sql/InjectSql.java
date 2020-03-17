package com.smartex.libraryapp.repository.sql;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marker annotation for SqlInjectBeanPostProcessor
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectSql {
    String value();
}
