package io.neural.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Plugin {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected PluginWrapper wrapper;

	public Plugin(final PluginWrapper wrapper) {
		if (wrapper == null) {
			throw new IllegalArgumentException("Wrapper cannot be null");
		}

		this.wrapper = wrapper;
	}

	public final PluginWrapper getWrapper() {
		return wrapper;
	}

	public void start() throws Exception {
	}

	public void stop() throws Exception {
	}

}
