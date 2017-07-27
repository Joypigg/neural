package cn.ms.neural.sideroad;

import org.junit.Assert;
import org.junit.Test;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;

public class SideRoadTest {

	@Test
	@SuppressWarnings("unchecked")
	public void testSideRoad() {
		SideRoadBuilder<SideRoadEvent> builder = new SideRoadBuilder<SideRoadEvent>();
		builder.setSideRoadWrapper(new SideRoadWrapper<SideRoadEvent>() {
			@Override
			public void wrapper(SideRoadEvent input, SideRoadEvent data) {
				data.set(input.getValue());
			}
		});
		builder.setEventFactory(new EventFactory<SideRoadEvent>() {
			@Override
			public SideRoadEvent newInstance() {
				return new SideRoadEvent();
			}
		});
		
		builder.setEventHandlers(new EventHandler<SideRoadEvent>() {
			@Override
			public void onEvent(SideRoadEvent event, long sequence, boolean endOfBatch) throws Exception {
				Assert.assertNotNull(event);
			}
		}, new EventHandler<SideRoadEvent>() {
			@Override
			public void onEvent(SideRoadEvent event, long sequence, boolean endOfBatch) throws Exception {
				Assert.assertNotNull(event);
			}
		});
		

		SideRoad<SideRoadEvent> sideroad = new SideRoad<SideRoadEvent>();
		sideroad.start(builder);
		sideroad.publish(new SideRoadEvent());
		sideroad.shutdown();
		
		Assert.assertNotNull(sideroad.getDisruptor());
		sideroad.setDisruptor(null);
		Assert.assertNull(sideroad.getDisruptor());
		Assert.assertTrue(true);
	}

}
