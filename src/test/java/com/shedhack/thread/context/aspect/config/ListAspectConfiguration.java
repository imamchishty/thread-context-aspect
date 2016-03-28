package com.shedhack.thread.context.aspect.config;

import com.shedhack.thread.context.adapter.ListThreadContextAdapter;
import com.shedhack.thread.context.aspect.ThreadContextAspect;
import com.shedhack.thread.context.helper.AspectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * List specific configuration.
 */
@Configuration
@SpringBootApplication
@Import(BaseConfiguration.class)
public class ListAspectConfiguration {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ListAspectConfiguration.class, args);
    }

    // ---------------------
    // Thread Context Aspect
    // ---------------------

    @Autowired
    private ListThreadContextAdapter listThreadContextAdapter;

    @Autowired
    private AspectHelper aspectHelper;

    @Bean
    public ThreadContextAspect listThreadContextAspect() {
        return new ThreadContextAspect(listThreadContextAdapter, aspectHelper);
    }
}
