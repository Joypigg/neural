package cn.ms.neural.extension.singleton;

import io.neural.extension.Extension;

import java.util.concurrent.atomic.AtomicLong;

@Extension("spiSingletonImpl")
public class NspiSingletonImpl implements NspiSingleton {
    private static AtomicLong counter = new AtomicLong(0);
    private long index = 0;

    public NspiSingletonImpl() {
        index = counter.incrementAndGet();
    }

    @Override
    public long spiHello() {
        return index;
    }

}
