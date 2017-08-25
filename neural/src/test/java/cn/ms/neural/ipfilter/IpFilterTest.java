package cn.ms.neural.ipfilter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IpFilterTest {
	@Test
	public void basicUsageOfIpFilter() {
		// initialize Config
		IpFilterConf ipFilterConf = new IpFilterConf();
		ipFilterConf.setAllowFirst(true);
		ipFilterConf.setDefaultAllow(false);
		ipFilterConf.allow("1.2.3.4"); // add allow ip
		ipFilterConf.allow("10.20.30.40");
		ipFilterConf.deny("101.102.103.104"); // add deny ip

		// create IpFilter by using IpFilters.create() or
		// IpFilters.createCached() method
		IpFilter ipFilter = IpFilters.create(ipFilterConf);
		// then, call accept method for filtering ip.
		assertTrue(ipFilter.accept("1.2.3.4"));
		assertFalse(ipFilter.accept("101.102.103.104"));
	}
}