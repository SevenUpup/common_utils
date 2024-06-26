package com.fido.annotation.asm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: FiDo
 * @date: 2024/5/7
 * @des:  该注解将替换指定类名的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface AsmMethodReplace {

    Class oriClass();

    String oriMethod() default "";

    int oriAccess() default AsmMethodOpcodes.INVOKESTATIC;

}
