package io.neural.common.event;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * The Event Processor.
 *
 * @author lry
 **/
@Slf4j
public enum EventProcessor implements EventListener {

    EVENT;

    private List<EventListener> eventListeners = new CopyOnWriteArrayList<>();
    private ExecutorService eventListenerExecutor = null;

    EventProcessor() {
        this.start();
    }

    /**
     * The start EventProcessor
     */
    public void start() {
        // start EventListener executor
        log.debug("The starting EventListener executor……");
        ThreadFactoryBuilder subscribeBuilder = new ThreadFactoryBuilder();
        ThreadFactory subscribeThreadFactory =
                subscribeBuilder.setDaemon(true).setNameFormat("event-processor").build();
        this.eventListenerExecutor = Executors.newFixedThreadPool(3, subscribeThreadFactory);
        // add shutdown Hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
    }

    /**
     * The add EventListener
     *
     * @param eventListener
     */
    public void addEventListener(EventListener eventListener) {
        if (eventListener instanceof EventProcessor) {
            throw new IllegalArgumentException("The don't core adding " + EventProcessor.class);
        }

        eventListeners.add(eventListener);
    }

    /**
     * The remove EventListener
     *
     * @param eventListener
     */
    public void removeEventListener(EventListener eventListener) {
        eventListeners.remove(eventListener);
    }

    /**
     * The get all EventListener
     *
     * @return
     */
    public List<EventListener> getEventListeners() {
        return eventListeners;
    }

    @Override
    public void notify(String module, EventType eventType, Object... args) {
        if (eventListeners.isEmpty()) {
            return;
        }

        // for each notify EventListener
        eventListeners.forEach(eventListener -> {
            this.eventListenerExecutor.execute(() ->
            {
                try {
                    eventListener.notify(module, eventType, args);
                } catch (Exception e) {
                    log.error("The " + module + " EventListener[" +
                            eventListener + "] notify[" + eventType + "] is exception.", e);
                }
            });
        });
    }

    /**
     * The destroy
     */
    public void destroy() {
        log.debug("The EventProcessor is executing destroy……");
        if (null != eventListenerExecutor) {
            eventListenerExecutor.shutdown();
        }
        if (!eventListeners.isEmpty()) {
            eventListeners.clear();
        }
    }

}
