package com.vic.transfer.annotation;

import java.lang.annotation.*;

/**
 * @author vic
 * @date 2021/12/7 11:03 下午
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
