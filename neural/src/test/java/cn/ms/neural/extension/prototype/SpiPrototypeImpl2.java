package cn.ms.neural.extension.prototype;

import java.util.concurrent.atomic.AtomicLong;

import cn.ms.neural.extension.SpiMeta;

@SpiMeta("spiPrototypeImpl2")
public class SpiPrototypeImpl2 implements SpiPrototype {
    private static AtomicLong counter = new AtomicLong(0);
    private long index = 0;

    public SpiPrototypeImpl2() {
        index = counter.incrementAndGet();
    }

    @Override
    public long spiHello() {
        System.out.println("SpiPrototypeTestImpl_" + index + " say hello");
        return index;
    }

}
