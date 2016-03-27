package com.shedhack.thread.context.helper;

import java.util.Map;

/**
 * This helper is responsible for obtaining the unique ID for the thread,
 * as well as setting context map with contextual meta-data such as session id.
 *
 *
 * @author imamchishty
 */
public interface AspectHelper {

    /**
     * Returns the unique ID.
     */
    String getId();

    /**
     * Contextual map containing appropriate meta-data.
     */
    Map<String, Object> getContext();

}
