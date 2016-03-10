package com.shedhack.thread.context.config;


import com.shedhack.thread.context.aspect.ThreadContextAspect;
import com.shedhack.thread.context.handler.JsonThreadContextHandler;
import com.shedhack.thread.context.handler.ThreadContextHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Default configuration.
 * Uses {@link com.shedhack.thread.context.handler.JsonThreadContextHandler}.
 *
 * @author imamchishty
 */
@Configuration
public class ThreadContextAspectConfiguration {

    @Bean
    public ThreadContextHandler handler() {
        return new JsonThreadContextHandler();
    }

    @Bean
    public ThreadContextAspect threadContextAspect() {
        return new ThreadContextAspect(handler());
    }
}
