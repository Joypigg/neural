package io.neural.retryer;

import io.neural.retryer.support.Attempt;

import com.google.common.annotations.Beta;

@Beta
public interface RetryListener {

    <V> void onRetry(Attempt<V> attempt);
    
}
