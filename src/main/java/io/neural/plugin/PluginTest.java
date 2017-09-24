package io.neural.plugin;

public class PluginTest {

	public static void main(String[] args) {
		PluginManager pluginManager = new PluginManager();
		pluginManager.loadPlugins();
		pluginManager.startPlugins();
	}

}
