package com.promegu.xlog.base;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Target({METHOD, CONSTRUCTOR})
@Retention(SOURCE)
public @interface XLog {
}
