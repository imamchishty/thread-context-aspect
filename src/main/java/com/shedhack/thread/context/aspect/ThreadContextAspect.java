package com.shedhack.thread.context.aspect;

import com.shedhack.thread.context.annotation.Ignore;
import com.shedhack.thread.context.annotation.ThreadContext;
import com.shedhack.thread.context.handler.ThreadContextHandler;
import com.shedhack.thread.context.model.DefaultThreadContextModel;
import com.shedhack.thread.context.model.ThreadContextModel;
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
import java.util.*;

/**
 * @author imamchishty
 */
@Aspect
@Component
public class ThreadContextAspect {

    private ThreadContextHandler handler;

    private static String ARG = "ARG";

    private static String DOT = ".";

    private static String IGNORED = "IGNORED";

    public ThreadContextAspect(ThreadContextHandler handler) {
        this.handler = handler;
    }

    @Before("execution(* *.*(..)) && @annotation(threadContext) ")
    public void setThreadContext(JoinPoint joinPoint, ThreadContext threadContext) throws Throwable {

        ThreadContextModel model = new DefaultThreadContextModel();
        model.setMethodName(joinPoint.getSignature().getDeclaringTypeName() + DOT + joinPoint.getSignature().getName());
        model.setTimestamp(new Date());

        try {
            HttpSession session = (HttpSession) RequestContextHolder.currentRequestAttributes()
                    .resolveReference(RequestAttributes.REFERENCE_SESSION);

            HttpServletRequest request = (HttpServletRequest) RequestContextHolder.currentRequestAttributes()
                    .resolveReference(RequestAttributes.REFERENCE_REQUEST);

            model.setId(session.getId());
            model.addContext("path", request.getRequestURI());
            model.addContext("http-verb", request.getMethod());
        }
        catch(Exception ex) {
            // ignore
        }

        MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
        Annotation[][] annotations = methodSig.getMethod().getParameterAnnotations();
        boolean ignore = false;

        // Create the ignore list
        for (int i = 0; i < joinPoint.getArgs().length; i++) {

            for (Annotation annotation : annotations[i]) {
                if (Ignore.class.isInstance(annotation)) {
                    ignore = true;
                }
            }

            if(!ignore) {
                model.addParam(ARG + i, joinPoint.getArgs()[i]);
            }
            else {
                model.addParam(ARG + i, IGNORED);
            }
        }

        handler.setThreadContext(model);
    }

}
