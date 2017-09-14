package cn.ms.neural.extension.singleton;

import cn.ms.neural.extension.NSPI;

@NSPI(single = true)
public interface NspiSingleton {
	long spiHello();
}
