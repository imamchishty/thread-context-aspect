package com.shedhack.thread.context.aspect;

import com.shedhack.thread.context.adapter.ThreadContextAdapter;
import com.shedhack.thread.context.annotation.Ignore;
import com.shedhack.thread.context.annotation.ThreadContext;
import com.shedhack.thread.context.helper.AspectHelper;
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
 *  Id:  Unique Id. This is best suited for the Request Id rather than the Session Id. The session Id could be set in the context.
 *  Date: Date/time for the request.
 *  Method name: Ideally this returns the fully qualified method name.
 *  Context: Context may contain items such as the htto method, the http request path, session Id etc.
 *  Method params: the name of the params will be set to ARGx, e.g. ARG0, ARG1
 * </pre>
 *
 * ThreadContextAspect requires an implementation of {@link ThreadContextAdapter}. This adapter
 * wraps up the underlying implementation, thus wrapping, simplfying and providing a common way
 * of interacting with a {@link com.shedhack.thread.context.handler.ThreadContextHandler}.
 *
 * {@link AspectHelper} is also required when constructing the aspect. This helps in getting the correct ID and the context map.
 * The default implementation can be provided, {@link com.shedhack.thread.context.helper.RequestIdAspectHelper}
 *
 * @author imamchishty
 */
@Aspect
@Component
public class ThreadContextAspect {

    public ThreadContextAspect(ThreadContextAdapter adapter, AspectHelper aspectHelper) {
        this.adapter = adapter;
        this.helper = aspectHelper;
    }

    public ThreadContextAdapter adapter;

    private AspectHelper helper;

    @Before("execution(* *.*(..)) && @annotation(threadContext) ")
    public void interception(JoinPoint joinPoint, ThreadContext threadContext) throws Throwable {
        adapter.setContext(helper.getId(), new Date(), getMethodName(joinPoint), helper.getContext(), getMethodParams(joinPoint));
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
