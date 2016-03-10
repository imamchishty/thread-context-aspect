package com.shedhack.thread.context.aspect.config;

import com.shedhack.thread.context.annotation.Ignore;
import com.shedhack.thread.context.annotation.ThreadContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by imamchishty on 10/03/2016.
 */
@Controller
public class FooService {

    @RequestMapping("/add/{a}/{b}")
    @ThreadContext
    public Integer addition(@PathVariable int a, @Ignore @PathVariable int b) {
        return a+b;
    }


    @RequestMapping("/login/{name}/{password}")
    @ThreadContext
    public String login(@PathVariable String name, @Ignore @PathVariable String password) {
        return "hello " + name;
    }
}
