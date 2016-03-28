package com.shedhack.thread.context.aspect.config;

import com.shedhack.thread.context.adapter.SimpleThreadContextAdapter;
import com.shedhack.thread.context.aspect.ThreadContextAspect;
import com.shedhack.thread.context.helper.AspectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * String specific aspect handler config.
 */
@Configuration
@SpringBootApplication
@Import(BaseConfiguration.class)
public class SimpleAspectConfiguration {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SimpleAspectConfiguration.class, args);
    }

    // ---------------------
    // Thread Context Aspect
    // ---------------------

    @Autowired
    private SimpleThreadContextAdapter simpleThreadContextAdapter;

    @Autowired
    private AspectHelper aspectHelper;

    @Bean
    public ThreadContextAspect simpleThreadContextAspect() {
        return new ThreadContextAspect(simpleThreadContextAdapter, aspectHelper);
    }
}
