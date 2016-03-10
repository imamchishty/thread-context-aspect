package com.shedhacl.thread.context.aspect;

import com.shedhack.thread.context.annotation.ThreadContext;
import com.shedhack.thread.context.aspect.ThreadContextAspect;
import com.shedhack.thread.context.aspect.config.FooService;
import com.shedhack.thread.context.aspect.config.TestConfiguration;
import com.shedhack.thread.context.handler.ThreadContextHandler;
import com.shedhack.thread.context.model.ThreadContextModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by imamchishty on 10/03/2016.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class ThreadContextAspectTest {

    @Autowired
    private FooService fooService;

    @Autowired
    private ThreadContextHandler handler;

    @Autowired
    private ThreadContextAspect aspect;


    @Test
    public void should_create_thread_context_via_aspect() {

        // Act - call the dummy service
        assertEquals(new Integer(3), fooService.addition(1, 2));

        // Assert - check the thread context
        Optional<ThreadContextModel> model = handler.getThreadContext();

        // TODO finish
    }

}
