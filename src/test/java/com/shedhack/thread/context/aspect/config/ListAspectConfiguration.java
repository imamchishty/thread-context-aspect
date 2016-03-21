package com.shedhack.thread.context.aspect.config;

import com.shedhack.thread.context.adapter.ListThreadContextAdapter;
import com.shedhack.thread.context.aspect.ThreadContextAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by imamchishty on 16/03/2016.
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

    @Bean
    public ThreadContextAspect listThreadContextAspect() {
        return new ThreadContextAspect(listThreadContextAdapter);
    }
}
