package cn.ms.neural.extension.prototype;

import io.neural.extension.Extension;

import java.util.concurrent.atomic.AtomicLong;

@Extension("spiPrototypeImpl1")
public class NspiPrototypeImpl1 implements NspiPrototype {
    private static AtomicLong counter = new AtomicLong(0);
    private long index = 0;

    public NspiPrototypeImpl1() {
        index = counter.incrementAndGet();
    }

    @Override
    public long spiHello() {
        return index;
    }

}
