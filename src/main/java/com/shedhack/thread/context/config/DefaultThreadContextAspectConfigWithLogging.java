package com.shedhack.thread.context.config;

import com.shedhack.thread.context.adapter.SimpleThreadContextAdapter;
import com.shedhack.thread.context.aspect.ThreadContextAspect;
import com.shedhack.thread.context.handler.LoggingAfterSet;
import com.shedhack.thread.context.handler.SimpleThreadContextHandler;
import com.shedhack.thread.context.helper.AspectHelper;
import com.shedhack.thread.context.helper.RequestIdAspectHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Default configuration using {@link SimpleThreadContextAdapter}
 */
@Configuration
public class DefaultThreadContextAspectConfigWithLogging {

    // -----------------------
    // Thread Handler + Aspect
    // -----------------------

    @Bean
    public SimpleThreadContextHandler simpleThreadContextHandler() {
        return new SimpleThreadContextHandler(Arrays.asList(new LoggingAfterSet()));
    }

    @Bean
    public SimpleThreadContextAdapter simpleThreadContextAdapter() {
        return new SimpleThreadContextAdapter(simpleThreadContextHandler());
    }

    @Bean
    public AspectHelper aspectHelper() {
        return new RequestIdAspectHelper();
    }

    @Bean
    public ThreadContextAspect simpleThreadContextAspect() {
        return new ThreadContextAspect(simpleThreadContextAdapter(), aspectHelper());
    }

}