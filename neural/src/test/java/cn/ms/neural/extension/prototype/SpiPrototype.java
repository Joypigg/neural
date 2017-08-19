package cn.ms.neural.extension.prototype;

import cn.ms.neural.extension.Scope;
import cn.ms.neural.extension.Spi;

@Spi(scope = Scope.PROTOTYPE)
public interface SpiPrototype {
    long spiHello();
}
