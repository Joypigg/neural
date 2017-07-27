package cn.ms.neural.ipfilter;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IpFiltersTest {
    @Test
    public void createIpFilterWithConfig() {
        IpFilter filter = IpFilters.create(createConfig());
        assertTrue(filter instanceof ConfIpFilter);
    }

    @Test
    public void createCachedIpFilterWithConfig() {
        IpFilter filter = IpFilters.createCached(createConfig());
        assertTrue(filter instanceof CachedIpFilter);
    }

    private IpFilterConf createConfig() {
        IpFilterConf ipFilterConf = new IpFilterConf();
        ipFilterConf.allow("1.2.3.4");
        return ipFilterConf;
    }
}
