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

    private static final String CONTEXT_GROUP_ID = "group-id";

    private static final String CONTEXT_CALLER_ID = "caller-id";

    private static final String CONTEXT_SESSION_ID = "session-id";

    private static final String REQUEST_ID = "request-id";

    /**
     * Default constructor.
     */
    public RequestIdAspectHelper() {
    }

    /**
     * Attempts to find the 'request-id', if it fails then defaults to the session Id.
     */
    public String getId() {

        String id = getHttpRequestServlet().getHeader(REQUEST_ID);

        if(id != null) {
            return id;
        }

        return getHttpSession().getId();
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getContext() {
        Map<String, Object> context = new HashMap<>();

        context.put(CONTEXT_GROUP_ID, getHttpRequestServlet().getHeader(CONTEXT_GROUP_ID));
        context.put(CONTEXT_CALLER_ID, getHttpRequestServlet().getHeader(CONTEXT_CALLER_ID));
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
