package com.everyday.caipiao.weaver;

/**
 * Created by bjliuzhanyong on 2017/9/19.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by bjliuzhanyong on 2017/9/19.
 */
@Target({ TYPE, METHOD, CONSTRUCTOR })
@Retention(CLASS)
public @interface AfterLoginCheck {

}

