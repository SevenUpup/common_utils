package com.fido.common.common_utils.test.asm.apitest.measure_methodtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: FiDo
 * @date: 2024/5/6
 * @des:  一个计算方法耗时的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface MeasureTime { }
