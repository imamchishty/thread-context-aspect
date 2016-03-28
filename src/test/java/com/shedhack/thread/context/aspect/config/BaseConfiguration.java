package com.shedhack.thread.context.aspect.config;

import com.shedhack.thread.context.adapter.JsonThreadContextAdapter;
import com.shedhack.thread.context.adapter.ListThreadContextAdapter;
import com.shedhack.thread.context.adapter.SimpleThreadContextAdapter;
import com.shedhack.thread.context.handler.JsonThreadContextHandler;
import com.shedhack.thread.context.handler.ListThreadContextHandler;
import com.shedhack.thread.context.handler.SimpleThreadContextHandler;
import com.shedhack.thread.context.helper.AspectHelper;
import com.shedhack.thread.context.helper.DefaultAspectHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Test configuration.
 */
@Configuration
public class BaseConfiguration {

    // ------------
    // Test Service
    // ------------

    @Bean
    public FooService fooService() {
        return new FooService();
    }

    // -----------------------
    // Thread Context Handlers
    // -----------------------

    @Bean
    public JsonThreadContextHandler jsonThreadContextHandler() {
        return new JsonThreadContextHandler();
    }

    @Bean
    public ListThreadContextHandler listThreadContextHandler() {
        return new ListThreadContextHandler();
    }

    @Bean
    public SimpleThreadContextHandler simpleThreadContextHandler() {
        return new SimpleThreadContextHandler();
    }

    // ----------------------
    // Thread Context Helpers
    // ----------------------

    @Bean
    public JsonThreadContextAdapter jsonThreadContextAdapter() {
        return new JsonThreadContextAdapter(jsonThreadContextHandler());
    }

    @Bean
    public ListThreadContextAdapter listThreadContextAdapter() {
        return new ListThreadContextAdapter(listThreadContextHandler());
    }

    @Bean
    public SimpleThreadContextAdapter simpleThreadContextAdapter() {
        return new SimpleThreadContextAdapter(simpleThreadContextHandler());
    }

    @Bean
    public AspectHelper aspectHelper() {
        return new DefaultAspectHelper();
    }

}
