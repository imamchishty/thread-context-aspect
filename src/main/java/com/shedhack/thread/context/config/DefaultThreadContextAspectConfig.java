package com.shedhack.thread.context.config;

import com.google.gson.Gson;
import com.shedhack.thread.context.adapter.SimpleThreadContextAdapter;
import com.shedhack.thread.context.aspect.ThreadContextAspect;
import com.shedhack.thread.context.handler.SimpleThreadContextHandler;
import com.shedhack.thread.context.helper.AspectHelper;
import com.shedhack.thread.context.helper.SpanIdAspectHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Default configuration using {@link SimpleThreadContextAdapter}
 */
@Configuration
public class DefaultThreadContextAspectConfig {

    // -----------------------
    // Thread Handler + Aspect
    // -----------------------

    @Autowired(required = false)
    private Gson gson;

    @Bean
    public AspectHelper aspectHelper() {
        return new SpanIdAspectHelper();
    }

    @Bean
    public ThreadContextAspect simpleThreadContextAspect() {

        if(gson == null) {
            gson = new Gson();
        }

        return new ThreadContextAspect(aspectHelper(), gson);
    }

}
