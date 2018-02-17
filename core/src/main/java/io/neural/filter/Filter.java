package io.neural.filter;

import io.neural.extension.NPI;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Abstract Filter
 *
 * @param <M>
 * @author lry
 */
@NPI
@Slf4j
public abstract class Filter<M> {

    public String getId() {
        return this.getClass().getName();
    }

    public void init() throws Exception {
        log.debug("The initializing...");
    }

    public void destroy() throws Exception {
        log.debug("The destroy...");
    }

    public abstract void doFilter(Chain<M> chain, M m) throws Exception;

}
