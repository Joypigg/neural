package io.neural.common;

/**
 * The Subscribe Listener
 *
 * @author lry
 **/
public interface SubscribeListener {

    /**
     * The notify
     *
     * @param channel
     * @param message
     */
    void notify(String channel, String message);

}