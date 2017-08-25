package cn.ms.neural.extension.singleton;

import cn.ms.neural.extension.NSPI;
import cn.ms.neural.extension.Scope;

@NSPI(scope = Scope.SINGLETON)
public interface SpiSingleton {
    long spiHello();
}
