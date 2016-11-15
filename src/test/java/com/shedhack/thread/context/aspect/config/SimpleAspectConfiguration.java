package com.shedhack.thread.context.aspect.config;

import com.shedhack.thread.context.config.EnableThreadContextAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * String specific aspect handler config.
 */
@Configuration
@SpringBootApplication
@EnableThreadContextAspect
public class SimpleAspectConfiguration {

    // ------------
    // Test Service
    // ------------

    @Bean
    public FooService fooService() {
        return new FooService();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SimpleAspectConfiguration.class, args);
    }

}
