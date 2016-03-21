package com.shedhack.thread.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation works in conjuction with {@link com.shedhack.thread.context.annotation.ThreadContext}.
 * For any params that may contain sensitive values such as password then mark those params using this
 * annotation.
 */
@Target({ElementType.PARAMETER})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface Ignore {
}
