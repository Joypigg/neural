package cn.ms.neural.retryer;

import com.google.common.annotations.Beta;

@Beta
public interface RetryListener {

    <V> void onRetry(Attempt<V> attempt);
    
}
