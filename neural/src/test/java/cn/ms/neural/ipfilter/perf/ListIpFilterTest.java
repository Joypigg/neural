package cn.ms.neural.ipfilter.perf;

import org.junit.Test;

import cn.ms.neural.ipfilter.IpFilter;
import cn.ms.neural.ipfilter.IpFilterConf;
import cn.ms.neural.ipfilter.IpFilterTestUtil;

public class ListIpFilterTest {

    @Test
    public void shouldReturnTrueToAllowedIp() {
        IpFilterConf ipFilterConf = IpFilterTestUtil.createConfigForAllow();
        IpFilter ipFilter = new ListIpFilter(ipFilterConf);
        IpFilterTestUtil.assertAcceptForAllow(ipFilter);
    }

    @Test
    public void shouldReturnFalseToDeniedIp() {
        IpFilterConf ipFilterConf = IpFilterTestUtil.createConfigForDeny();
        IpFilter ipFilter = new ListIpFilter(ipFilterConf);
        IpFilterTestUtil.assertAcceptForDeny(ipFilter);
    }

}
