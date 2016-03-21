package com.shedhack.thread.context.aspect.config;

import com.shedhack.thread.context.annotation.Ignore;
import com.shedhack.thread.context.annotation.ThreadContext;
import com.shedhack.thread.context.handler.ThreadContextHandler;
import com.shedhack.thread.context.model.ThreadContextModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Created by imamchishty on 10/03/2016.
 */
@Controller
public class FooService {

    /**
     * This is a test restful service. It won't return the additional value,
     * but rather the thread name that we expected to have been set.
     */
    @RequestMapping("/add/{a}/{b}")
    @ThreadContext
    public ResponseEntity<String> addition(@PathVariable double a, @Ignore @PathVariable double b) {

        // The thread that executes the test will not be the same
        // one as that executes this method within the integration test mode.
        // Therefore we return the 'value' to the test.

        return new ResponseEntity<>(Thread.currentThread().getName(), HttpStatus.OK);
    }

}
