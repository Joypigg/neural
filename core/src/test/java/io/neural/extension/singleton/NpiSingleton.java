package io.neural.extension.singleton;

import io.neural.extension.NPI;

@NPI(single = true)
public interface NpiSingleton {
	long spiHello();
}
