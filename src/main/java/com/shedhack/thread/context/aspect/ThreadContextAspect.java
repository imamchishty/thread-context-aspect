package com.shedhack.thread.context.aspect;

import com.shedhack.thread.context.adapter.ThreadContextAdapter;
import com.shedhack.thread.context.annotation.Ignore;
import com.shedhack.thread.context.annotation.ThreadContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
 * @author imamchishty
 */
@Aspect
@Component
public class ThreadContextAspect {

    public ThreadContextAspect(ThreadContextAdapter adapter) {
        this.adapter = adapter;
    }

    public ThreadContextAdapter adapter;

    @Before("execution(* *.*(..)) && @annotation(threadContext) ")
    public void interception(JoinPoint joinPoint, ThreadContext threadContext) throws Throwable {
        adapter.setContext(getSessionId(), new Date(), getMethodName(joinPoint), getContext(), getMethodParams(joinPoint));
    }

    // -------------
    // Static fields
    // -------------

    private static final String ARG = "ARG";

    private static final String DOT = ".";

    private static final String IGNORED = "IGNORED";

    private static final String CONTEXT_PATH = "path";

    private static final String CONTEXT_HTTP_METHOD = "http-method";

    private static final String CONTEXT_SESSION_ID = "session-id";

    // --------------
    // Helper methods
    // --------------

    protected HttpServletRequest getHttpRequestServlet() {
        return (HttpServletRequest) RequestContextHolder.currentRequestAttributes()
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
    }

    protected String getRequestPath() {
        return getHttpRequestServlet().getRequestURI();
    }

    protected String getHttpMethod() {
        return getHttpRequestServlet().getMethod();
    }

    protected String getSessionId() {
        return getHttpSession().getId();
    }

    protected HttpSession getHttpSession() {
        return (HttpSession) RequestContextHolder.currentRequestAttributes()
                .resolveReference(RequestAttributes.REFERENCE_SESSION);
    }

    protected String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringTypeName() + DOT + joinPoint.getSignature().getName();
    }

    protected Map<String, Object> getContext() {
        Map<String, Object> context = new HashMap<>();
        context.put(CONTEXT_PATH, getRequestPath());
        context.put(CONTEXT_HTTP_METHOD, getHttpMethod());
        context.put(CONTEXT_SESSION_ID, getSessionId());
        return context;
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
