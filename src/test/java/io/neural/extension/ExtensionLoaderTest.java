package io.neural.extension;

import io.neural.extension.ExtensionLoader;
import io.neural.extension.NSPI;
import io.neural.extension.prototype.NspiPrototype;
import io.neural.extension.prototype.NspiPrototypeImpl2;
import io.neural.extension.singleton.NspiSingleton;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ExtensionLoaderTest {
	
	@Test
	public void test() {
		List<NspiPrototype> nspiPrototype = ExtensionLoader.getLoader(NspiPrototype.class).getExtensions("ddd");
		System.out.println(nspiPrototype);
	}
	
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testExtensionNormal() {
        // 单例模式下只会构造一次实例
        Assert.assertEquals(1, ExtensionLoader.getLoader(NspiSingleton.class).getExtension("spiSingletonImpl").spiHello());
        Assert.assertEquals(1, ExtensionLoader.getLoader(NspiSingleton.class).getExtension("spiSingletonImpl").spiHello());

        // 多例模式下在每次获取的时候进行实例化
        Assert.assertEquals(1, ExtensionLoader.getLoader(NspiPrototype.class).getExtension("spiPrototypeImpl1").spiHello());
        Assert.assertEquals(2, ExtensionLoader.getLoader(NspiPrototype.class).getExtension("spiPrototypeImpl1").spiHello());

        // 手动添加实现类
        Assert.assertEquals(1, ExtensionLoader.getLoader(NspiPrototype.class).getExtensions("").size());
        ExtensionLoader loader = ExtensionLoader.getLoader(NspiPrototype.class);
        loader.addExtensionClass(NspiPrototypeImpl2.class);

        // 返回所有实现类
        ExtensionLoader.initExtensionLoader(NspiPrototype.class);
        Assert.assertEquals(1, ExtensionLoader.getLoader(NspiSingleton.class).getExtensions("").size());
        Assert.assertEquals(2, ExtensionLoader.getLoader(NspiPrototype.class).getExtensions("").size());

    }

    @Test
    public void testExtensionAbNormal() {
        // 没有注解spi的接口无法进行扩展
        try {
            ExtensionLoader.getLoader(NotSpiInterface.class);
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("without @NSPI annotation"));
        }

        Assert.assertNull(ExtensionLoader.getLoader(SpiWithoutImpl.class).getExtension("default"));
    }

    // not spi
    public interface NotSpiInterface {}

    // not impl
    @NSPI
    public interface SpiWithoutImpl {}
}
