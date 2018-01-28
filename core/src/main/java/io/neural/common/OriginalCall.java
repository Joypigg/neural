package io.neural.common;

/**
 * The Original Call.
 *
 * @author lry
 */
public abstract class OriginalCall {

    /**
     * The process original call
     *
     * @return
     * @throws Throwable
     */
    public abstract Object call() throws Throwable;

    /**
     * The process fall back
     *
     * @return
     * @throws Throwable
     */
    public Object fallback() throws Throwable {
        return null;
    }

}
