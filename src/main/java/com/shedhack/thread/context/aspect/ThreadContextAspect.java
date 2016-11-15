package com.shedhack.thread.context.aspect;

import com.google.gson.Gson;
import com.shedhack.thread.context.adapter.JsonThreadContextAdapter;
import com.shedhack.thread.context.adapter.SimpleThreadContextAdapter;
import com.shedhack.thread.context.adapter.ThreadContextAdapter;
import com.shedhack.thread.context.annotation.Ignore;
import com.shedhack.thread.context.annotation.ThreadContext;
import com.shedhack.thread.context.annotation.ThreadContextType;
import com.shedhack.thread.context.handler.JsonThreadContextHandler;
import com.shedhack.thread.context.handler.SimpleThreadContextHandler;
import com.shedhack.thread.context.helper.AspectHelper;
import com.shedhack.thread.context.helper.SpanIdAspectHelper;
import com.shedhack.thread.context.model.DefaultThreadContextModel;
import com.shedhack.thread.context.model.ThreadContextModel;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ThreadContextAspect provides a simple mechanism to set the thread context for methods
 * marked with {@link com.shedhack.thread.context.annotation.ThreadContext}. This will
 * attempt to set the following to the thread context:
 *
 * <pre>
 *  Id:  Unique Id. This is best suited for the Trace Id rather than the Session Id. The session Id could be set in the context.
 *  Date: Date/time for the request.
 *  Method name: Ideally this returns the fully qualified method name.
 *  Context: Context may contain items such as the htto method, the http request path, session Id etc.
 *  Method params: the name of the params will be set to ARGx, e.g. ARG0, ARG1
 * </pre>
 *
 * {@link AspectHelper} is required when constructing the aspect. This helps in getting the correct ID and the context map.
 * The default implementation can be provided, {@link SpanIdAspectHelper}
 *
 * @author imamchishty
 */
@Aspect
@Component
public class ThreadContextAspect {

    private final ThreadContextAdapter jsonAdapter;
    private final ThreadContextAdapter stringAdapter;

    /**
     * Internally this constructor will create two adapters, one for string output and the other for json.
     * Which one to be used is dependent on the type() property on {@link ThreadContext}
     *
     * Two adapters are:
     * {@link SimpleThreadContextAdapter} and {@link JsonThreadContextAdapter} for json.
     *
     * @param aspectHelper helper to identify the ID, based on {@link AspectHelper}
     * @param gson for JSON conversions.
     */
    public ThreadContextAspect(AspectHelper aspectHelper, Gson gson) {
        this.helper = aspectHelper;
        this.stringAdapter= new SimpleThreadContextAdapter(new SimpleThreadContextHandler());
        this.jsonAdapter = new JsonThreadContextAdapter(new JsonThreadContextHandler(gson));
    }

    private AspectHelper helper;

    @Before("execution(* *.*(..)) && @annotation(threadContext) ")
    public void interception(JoinPoint joinPoint, ThreadContext threadContext) throws Throwable {

        ThreadContextModel model = createThreadModel(joinPoint, threadContext);

        if(threadContext.type().equals(ThreadContextType.JSON)) {
            jsonAdapter.setContext(model.getId(), model.getTimestamp(), model.getMethodName(), model.getContext(), model.getParams());
        }

        // String is the back up option.
        else {
            stringAdapter.setContext(model.getId(), model.getTimestamp(), model.getMethodName(), model.getContext(), model.getParams());
        }
    }

    private ThreadContextModel createThreadModel(JoinPoint joinPoint, ThreadContext threadContext) {

        ThreadContextModel model = new DefaultThreadContextModel();
        model.setId(helper.getId());
        model.setContext(helper.getContext());
        model.setParams(getMethodParams(joinPoint));
        model.setMethodName(getMethodName(joinPoint));
        model.setTimestamp(new Date());
        return model;
    }

    // -------------
    // Static fields
    // -------------

    private static final String ARG = "ARG";

    private static final String DOT = ".";

    private static final String IGNORED = "IGNORED";

    // --------------
    // Helper methods
    // --------------

    protected String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringTypeName() + DOT + joinPoint.getSignature().getName();
    }

    protected Map<String, Object> getMethodParams(JoinPoint joinPoint) {

        MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
        Annotation[][] annotations = methodSig.getMethod().getParameterAnnotations();

        Map<String, Object> params = new HashMap<>();

        boolean ignore = false;

        // Create the ignore list
        for (int i = 0; i < joinPoint.getArgs().length; i++) {

            for (Annotation annotation : annotations[i]) {
                if (Ignore.class.isInstance(annotation)) {
                    ignore = true;
                }
            }

            if(!ignore) {
                params.put(ARG + i, joinPoint.getArgs()[i]);
            }
            else {
                params.put(ARG + i, IGNORED);
            }
        }

        return params;
    }
}
