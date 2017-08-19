package cn.ms.neural.ipfilter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConfIpFilterTest {
    @Test
    public void shouldReturnTrueToAllowedIp() {
        IpFilterConf ipFilterConf = IpFilterTestUtil.createConfigForAllow();
        IpFilter ipFilter = new ConfIpFilter(ipFilterConf);
        IpFilterTestUtil.assertAcceptForAllow(ipFilter);
    }

    @Test
    public void shouldReturnFalseToDeniedIp() {
        IpFilterConf ipFilterConf = IpFilterTestUtil.createConfigForDeny();
        IpFilter ipFilter = new ConfIpFilter(ipFilterConf);
        IpFilterTestUtil.assertAcceptForDeny(ipFilter);
    }

    @Test
    public void shouldReturnTrueDupIpInAllowAndDenyListWhenAllowFirst() {
        IpFilterConf ipFilterConf = new IpFilterConf();
        ipFilterConf.setAllowFirst(true);
        ipFilterConf.allow("1.2.3.4");
        ipFilterConf.deny("1.2.3.4");
        IpFilter ipFilter = new ConfIpFilter(ipFilterConf);
        assertTrue(ipFilter.accept("1.2.3.4"));
    }

    @Test
    public void shouldReturnFalseDupIpInAllowAndDenyListWhenDenyFirst() {
        IpFilterConf ipFilterConf = new IpFilterConf();
        ipFilterConf.setAllowFirst(false);
        ipFilterConf.allow("1.2.3.4");
        ipFilterConf.deny("1.2.3.4");
        IpFilter ipFilter = new ConfIpFilter(ipFilterConf);
        assertFalse(ipFilter.accept("1.2.3.4"));
    }
}
