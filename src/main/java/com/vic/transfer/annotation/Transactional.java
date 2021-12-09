package com.vic.transfer.annotation;

import java.lang.annotation.*;

/**
 * @author vic
 * @date 2021/12/9 10:41 下午
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
}
