package com.shedhack.thread.context.helper;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation for {@link com.shedhack.thread.context.helper.AspectHelper}
 *
 * The traceId (requires Sleuth) is used as the ID, if not available then session Id is used.
 *
 * @author imamchishty
 */
public class SpanIdAspectHelper implements AspectHelper {

    // -------------
    // Static fields
    // -------------

    private static final String SPAN_ID = "spanId";
    private static final String TRACE_ID = "traceId";

    /**
     * Default constructor.
     */
    public SpanIdAspectHelper() {
    }

    /**
     * Attempts to find the 'spanId', if it fails then defaults to the session Id.
     */
    public String getId() {

        String id = getHttpRequestServlet().getHeader(TRACE_ID);

        if(id != null) {
            return id;
        }

        return getHttpSession().getId();
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getContext() {

        String spanId = getHttpRequestServlet().getHeader(SPAN_ID);
        Map<String, Object> context = new HashMap<>();

        context.put(SPAN_ID, spanId);
        context.put(TRACE_ID, getId());

        return Collections.emptyMap();
    }

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

    protected HttpSession getHttpSession() {
        return (HttpSession) RequestContextHolder.currentRequestAttributes()
                .resolveReference(RequestAttributes.REFERENCE_SESSION);
    }
}
