package io.neural.extension.prototype;

import io.neural.extension.Extension;

import java.util.concurrent.atomic.AtomicLong;

@Extension("spiPrototypeImpl2")
public class NspiPrototypeImpl2 implements NspiPrototype {
    private static AtomicLong counter = new AtomicLong(0);
    private long index = 0;

    public NspiPrototypeImpl2() {
        index = counter.incrementAndGet();
    }

    @Override
    public long spiHello() {
        System.out.println("SpiPrototypeTestImpl_" + index + " say hello");
        return index;
    }

}
