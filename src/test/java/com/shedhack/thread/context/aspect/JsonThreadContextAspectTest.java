package com.shedhack.thread.context.aspect;

import com.google.gson.Gson;
import com.shedhack.thread.context.aspect.config.SimpleAspectConfiguration;
import com.shedhack.thread.context.handler.JsonThreadContextHandler;
import com.shedhack.thread.context.model.ThreadContextModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * This tests uses HTTP calls to test the Foo Controllers Restful Endpoints.
 * For the purpose of testing the API's return back the {@link com.shedhack.thread.context.model.ThreadContextModel},
 * reason being that the APIs are more likely to have been executed on a different thread and finding the context
 * becomes a bit cumbersome (I know I could use Thread.getAllStackTraces().keySet())
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(SimpleAspectConfiguration.class)
@WebIntegrationTest(randomPort = true)
@Profile("json")
public class JsonThreadContextAspectTest {

    @Value("${local.server.port}")
    private int serverPort;

    private RestTemplate template = new TestRestTemplate();

    private JsonThreadContextHandler helper = new JsonThreadContextHandler(new Gson());

    @Test
    public void should_create_thread_context_via_aspect() throws Exception {

        // Simple random number which can be used to check the thread name.
        double b = Math.random();

        // Act - now the API is rubbish, but we just want to check the aspect kicks in.
        String response = template.getForObject(getRootUrl() + "/add/1/" + b, String.class);
        Optional<ThreadContextModel> model = helper.convertFromString(response);

        // Assert
        assertTrue(model.isPresent());

        // check all of the model props
        assertEquals("com.shedhack.thread.context.aspect.config.FooService.addition", model.get().getMethodName());

        assertNotNull(model.get().getId());
        assertNotNull(model.get().getParams());
        assertNotNull(model.get().getTimestamp());
        assertNotNull(model.get().getContext());

        // check params have been added, should show 2, although the second argument value should be hidden/ignored
        assertTrue(model.get().getParams().containsKey("ARG0"));
        assertTrue(model.get().getParams().containsKey("ARG1"));
        assertEquals(1.0, model.get().getParams().get("ARG0"));
        assertEquals("IGNORED", model.get().getParams().get("ARG1"));

        // Check the context
        assertTrue(model.get().getContext().isEmpty());
    }

    private String getRootUrl() {
        return "http://localhost:" + serverPort;
    }
}
