# Thread Context Aspect

[![Build Status](https://travis-ci.org/imamchishty/thread-context-aspect.svg?branch=master "JMC threads list")](https://travis-ci.org/imamchishty/thread-context-aspect) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.shedhack.thread/thread-context-aspect/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.shedhack.thread/thread-context-aspect)

## Introduction
This is a sister project for [__Thread Context Handler__](https://github.com/imamchishty/thread-context-handler). It provides an easy abstraction using Spring Aspects to create the required thread context.

At the core of the project is the actual aspect [__ThreadContextAspect__](https://github.com/imamchishty/thread-context-aspect/blob/master/src/main/java/com/shedhack/thread/context/aspect/ThreadContextAspect.java). This provides a simple mechanism to set the thread context for methods marked with the [__ThreadContext annotation__](https://github.com/imamchishty/thread-context-aspect/blob/master/src/main/java/com/shedhack/thread/context/aspect/ThreadContextAspect.java). This is a method level annotation and will attempt to create the thread context with the following:

- __Id__:  Unique Id. This is best suited for the Request Id rather than the Session Id. The session Id could be set in the context.

- __Date__: Date/time for the request.

- __Method name__: Ideally this returns the fully qualified method name.

- __Context__: Context may contain items such as the htto method, the http request path, session Id etc.

- __Method params__: the name of the params will be set to ARGx, e.g. ARG0, ARG1 and the string represention of the values will be set. If you don't wish to show some params you can mark those params with the [__Ignore__ annotation](https://github.com/imamchishty/thread-context-aspect/blob/master/src/main/java/com/shedhack/thread/context/annotation/Ignore.java). 
 
## Example usage

Simply mark the method you wish to use to set the context. Notice that the second param will be ignored.

	@ThreadContext
    	public ResponseEntity<String> addition(@PathVariable double a, @Ignore @PathVariable double b) {

If you're using the [**SimpleThreadContextHandler**](https://github.com/imamchishty/thread-context-handler/blob/master/src/main/java/com/shedhack/thread/context/handler/SimpleThreadContextHandler.java) then the output would be similar to (separated by `~`):

`1cfbcdd7-e71f-4e87-96fb-e27c9be32a10~Thu Mar 31 15:41:28 GST 2016~com.foo.service.account.controller.AccountController.getBalance~{path=/api/v1/accounts/11111/balance, http-method=GET, session-id=ECFCBEA307CD6AD359843C70E99C741F}~{ARG0=11111}`

The first property is the request-id which could be set using a HTTP server or [__Filter Request Id__](https://github.com/imamchishty/filter-request-id). This value is part of the HTTP header and can be accessed using the [__ThreadLocal String Utility__ project](https://github.com/imamchishty/threadlocal-string-utility).

## Thread Context Adapter

ThreadContextAspect requires an implementation of [__ThreadContextAdapter__](https://github.com/imamchishty/thread-context-handler). This adapter wraps up the underlying implementation, thus wrapping, simplfying and providing a common way of interacting with a [__ThreadContextHandler__](https://github.com/imamchishty/thread-context-handler).

[__Aspect Helper](https://github.com/imamchishty/thread-context-aspect/blob/master/src/main/java/com/shedhack/thread/context/helper/AspectHelper.java) is also required when constructing the aspect. This helps in getting the correct ID and the context map.
The default implementation can be provided, [__RequestIdAspectHelper__](https://github.com/imamchishty/thread-context-aspect/blob/master/src/main/java/com/shedhack/thread/context/helper/RequestIdAspectHelper.java). This implementation will attempt to find the request-id, if unable to do so then it defaults to the session-id.

## Setup

In your Spring configuration:

    // -----------------
    // Request Id Filter
    // -----------------

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new RequestIdFilter());
        return filter;
    }

    // -----------------------
    // Thread Handler + Aspect
    // -----------------------

    @Bean
    public SimpleThreadContextHandler simpleThreadContextHandler() {
        return new SimpleThreadContextHandler();
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

The above also includes the filter for generating the request-id. Please note that their are several __ThreadContextHandler__ and __ThreadContectAdapter__ implementations to choose from, please refer to the [__thread-context-handler__ project for details](https://github.com/imamchishty/thread-context-handler)

## Dependencies

As mentioned above this uses the `thread-context-handler` library.
        <dependency>
            <groupId>com.shedhack.thread</groupId>
            <artifactId>thread-context-handler</artifactId>
            <version>1.0.2</version>
        </dependency>

Also Spring Boot AOP

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
            <version>1.3.3.RELEASE</version>
        </dependency>

If you are using a different version of spring-boot-starter-aop then you could set a Maven exclusion for `spring-boot-starter-aop`, for example:

           <dependency>
                <groupId>com.shedhack.thread</groupId>
                <artifactId>thread-context-aspect</artifactId>
                <version>x.x.x</version>
                <exclusions>
                    <exclusion>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-aop</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>

## Java requirements

Built using JDK8.

## Example

## Using different Spring Boot versions

## Maven central

This artifact is available in [Maven Central] (https://maven-badges.herokuapp.com/maven-central/com.shedhack.thread/thread-context-aspect).
 
    <dependency>
        <groupId>com.shedhack.thread</groupId>
        <artifactId>thread-context-aspect</artifactId>
        <version>x.x.x</version>
    </dependency>    


Contact
-------

	Please feel free to contact me via email, imamchishty@gmail.com
