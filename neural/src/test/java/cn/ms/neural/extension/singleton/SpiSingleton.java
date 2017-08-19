package cn.ms.neural.extension.singleton;

import cn.ms.neural.extension.Scope;
import cn.ms.neural.extension.Spi;

@Spi(scope = Scope.SINGLETON)
public interface SpiSingleton {
    long spiHello();
}
