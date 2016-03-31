package com.shedhack.thread.context.helper;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation for {@link com.shedhack.thread.context.helper.AspectHelper}
 *
 * The request-id is used as the ID, if not available then session Id is used.
 * The context map contains the http-method, session-id and the path.
 *
 * @author imamchishty
 */
public class RequestIdAspectHelper implements AspectHelper {

    // -------------
    // Static fields
    // -------------

    private static final String CONTEXT_PATH = "path";

    private static final String CONTEXT_HTTP_METHOD = "http-method";

    private static final String CONTEXT_SESSION_ID = "session-id";

    private String requestId = "request-id";

    /**
     * Constructor requires the header key for the request id property.
     * If you wish to default to 'request-id' please use the default constructor.
     */
    public RequestIdAspectHelper(String requestIdKey) {
        this.requestId = requestIdKey;
    }

    /**
     * Default constructor, the header key for the request-id defaults
     * to look for 'request-id'.
     */
    public RequestIdAspectHelper() {
    }

    /**
     * Attempts to find the 'request-id', if it fails then defaults to the session Id.
     */
    public String getId() {

        String id = getHttpRequestServlet().getHeader(requestId);

        if(id != null) {

            // try from the thread local
            //ThreadLocal.

            return id;
        }

        return getHttpSession().getId();
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getContext() {
        Map<String, Object> context = new HashMap<>();
        context.put(CONTEXT_PATH, getRequestPath());
        context.put(CONTEXT_HTTP_METHOD, getHttpMethod());
        context.put(CONTEXT_SESSION_ID, getHttpSession().getId());
        return context;
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
