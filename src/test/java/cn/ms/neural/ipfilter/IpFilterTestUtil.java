package cn.ms.neural.ipfilter;

import static org.junit.Assert.assertEquals;

public class IpFilterTestUtil {
    private static String[] ipPatterns = {
            "1.2.3.4",
            "1.2.3.5",
            "1.2.3.64/26",
            "10.20.*",
            "10.10.10.*"
    };

    public static IpFilterConf createConfigForAllow() {
        IpFilterConf ipFilterConf = new IpFilterConf();
        ipFilterConf.setDefaultAllow(false);
        ipFilterConf.setAllowFirst(true);
        for (String ipPattern : ipPatterns)
            ipFilterConf.allow(ipPattern);
        return ipFilterConf;
    }

    public static void assertAcceptForAllow(IpFilter ipFilter) {
        assertAccept(ipFilter, true);
    }

    private static void assertAccept(IpFilter ipFilter, boolean b) {
        assertEquals(b, ipFilter.accept("1.2.3.4"));
        assertEquals(b, ipFilter.accept("1.2.3.5"));
        assertEquals(b, ipFilter.accept("1.2.3.64"));
        assertEquals(b, ipFilter.accept("1.2.3.65"));
        assertEquals(b, ipFilter.accept("1.2.3.127"));
        assertEquals(b, ipFilter.accept("10.10.10.1"));
        assertEquals(b, ipFilter.accept("10.20.1.1"));
        assertEquals(b, ipFilter.accept("10.20.10.10"));
        assertEquals(!b, ipFilter.accept("10.10.11.1"));
        assertEquals(!b, ipFilter.accept("1.2.3.63"));
    }

    public static IpFilterConf createConfigForDeny() {
        IpFilterConf ipFilterConf = new IpFilterConf();
        ipFilterConf.setDefaultAllow(true);
        ipFilterConf.setAllowFirst(true);
        for (String ipPattern : ipPatterns)
            ipFilterConf.deny(ipPattern);
        return ipFilterConf;
    }

    public static void assertAcceptForDeny(IpFilter ipFilter) {
        assertAccept(ipFilter, false);
    }
}
