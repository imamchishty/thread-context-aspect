package com.shedhack.thread.context.aspect.config;

import com.shedhack.thread.context.adapter.JsonThreadContextAdapter;
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
public class JsonAspectConfiguration {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(JsonAspectConfiguration.class, args);
    }

    // ---------------------
    // Thread Context Aspect
    // ---------------------

    @Autowired
    private JsonThreadContextAdapter jsonThreadContextAdapter;

    @Bean
    public ThreadContextAspect jsonThreadContextAspect() {
        return new ThreadContextAspect(jsonThreadContextAdapter);
    }

}
