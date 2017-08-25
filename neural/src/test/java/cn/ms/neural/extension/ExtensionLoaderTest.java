package cn.ms.neural.extension;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.extension.prototype.SpiPrototype;
import cn.ms.neural.extension.prototype.SpiPrototypeImpl2;
import cn.ms.neural.extension.singleton.SpiSingleton;
import cn.ms.neural.extension.singleton.SpiSingletonImpl;

public class ExtensionLoaderTest {
	
	@Test
	public void test() {
		List<SpiPrototype> spiPrototype = ExtensionLoader.getExtensionLoader(SpiPrototype.class).getExtensions("ddd");
		System.out.println(spiPrototype);
	}
	
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testExtensionNormal() {
        // 单例模式下只会构造一次实例
        Assert.assertEquals(1, ExtensionLoader.getExtensionLoader(SpiSingleton.class).getExtension("spiSingletonImpl").spiHello());
        Assert.assertEquals(1, ExtensionLoader.getExtensionLoader(SpiSingleton.class).getExtension("spiSingletonImpl").spiHello());

        // 多例模式下在每次获取的时候进行实例化
        Assert.assertEquals(1, ExtensionLoader.getExtensionLoader(SpiPrototype.class).getExtension("spiPrototypeImpl1").spiHello());
        Assert.assertEquals(2, ExtensionLoader.getExtensionLoader(SpiPrototype.class).getExtension("spiPrototypeImpl1").spiHello());

        // 手动添加实现类
        Assert.assertEquals(1, ExtensionLoader.getExtensionLoader(SpiPrototype.class).getExtensions("").size());
        ExtensionLoader loader = ExtensionLoader.getExtensionLoader(SpiPrototype.class);
        loader.addExtensionClass(SpiPrototypeImpl2.class);

        // 返回所有实现类
        ExtensionLoader.initExtensionLoader(SpiPrototype.class);
        Assert.assertEquals(1, ExtensionLoader.getExtensionLoader(SpiSingleton.class).getExtensions("").size());
        Assert.assertEquals(2, ExtensionLoader.getExtensionLoader(SpiPrototype.class).getExtensions("").size());

    }

    @Test
    public void testExtensionAbNormal() {
        // 没有注解spi的接口无法进行扩展
        try {
            ExtensionLoader.getExtensionLoader(NotSpiInterface.class);
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("without @Spi annotation"));
        }

        // 非接口无法进行扩展
        try {
            ExtensionLoader.getExtensionLoader(SpiSingletonImpl.class);
            Assert.assertTrue(false);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("is not interface"));
        }

        Assert.assertNull(ExtensionLoader.getExtensionLoader(SpiWithoutImpl.class).getExtension("default"));
    }

    // not spi
    public interface NotSpiInterface {}

    // not impl
    @NSPI
    public interface SpiWithoutImpl {}
}
