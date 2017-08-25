package cn.ms.neural.extension.prototype;

import cn.ms.neural.extension.NSPI;
import cn.ms.neural.extension.Scope;

@NSPI(scope = Scope.PROTOTYPE)
public interface SpiPrototype {
    long spiHello();
}
