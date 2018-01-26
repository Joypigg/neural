package io.neural.common.event;

/**
 * The Event Listener.
 *
 * @author lry
 **/
public interface EventListener {

    /**
     * The notify
     *
     * @param module
     * @param eventType
     * @param args
     */
    void notify(String module, EventType eventType, Object... args);

    /**
     * The Event Type.
     *
     * @author lry
     */
    public static interface EventType {

        /**
         * The get message
         *
         * @return
         */
        String getMessage();
    }

}
