package cn.ms.neural.extension.prototype;

import java.util.concurrent.atomic.AtomicLong;

import cn.ms.neural.extension.SpiMeta;

@SpiMeta(name = "spiPrototypeImpl1")
public class SpiPrototypeImpl1 implements SpiPrototype {
    private static AtomicLong counter = new AtomicLong(0);
    private long index = 0;

    public SpiPrototypeImpl1() {
        index = counter.incrementAndGet();
    }

    @Override
    public long spiHello() {
        return index;
    }

}
