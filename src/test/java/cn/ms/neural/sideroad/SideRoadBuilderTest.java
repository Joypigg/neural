package cn.ms.neural.sideroad;

import org.junit.Assert;
import org.junit.Test;

public class SideRoadBuilderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testName() throws Exception {
		SideRoadBuilder<SideRoadEvent> sideRoadBuilder = new SideRoadBuilder<SideRoadEvent>();
		sideRoadBuilder.setEventFactory(null).setEventHandlers()
				.setProducerType(null).setRingBufferSize(0)
				.setShutdownTimeout(0).setSideRoadWrapper(null)
				.setThreadFactory(null).setWaitStrategy(null);
		Assert.assertTrue(true);
	}

}
