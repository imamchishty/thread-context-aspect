package com.shedhack.thread.context.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Enables the default settings for the thread context aspect
 * with logging enabled.
 *
 * @author imamchishty
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Import(DefaultThreadContextAspectConfigWithLogging.class)
public @interface EnableThreadContextAspectWithLogging {
}
