package com.shedhack.thread.context.aspect.config;

import com.shedhack.thread.context.config.EnableTreadContextAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by imamchishty on 10/03/2016.
 */

@Configuration
@EnableTreadContextAspect
@SpringBootApplication
public class TestConfiguration {

    @Bean
    public FooService fooService() {
        return new FooService();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(TestConfiguration.class, args);
    }
}
