package cn.ms.neural.extension.singleton;

import io.neural.extension.NSPI;

@NSPI(single = true)
public interface NspiSingleton {
	long spiHello();
}
