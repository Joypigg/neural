package cn.ms.neural.sideroad;

import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * 数据旁路
 * 
 * @author lry
 *
 * @param <T>
 */
public class SideRoad<T> {

	private Disruptor<T> disruptor;
	private SideRoadBuilder<T> sideRoadBuilder;

	public void start(SideRoadBuilder<T> bypassBuilder) {
		this.sideRoadBuilder = bypassBuilder;
		disruptor = new Disruptor<T>(this.sideRoadBuilder.getEventFactory(),
				this.sideRoadBuilder.getRingBufferSize(),
				this.sideRoadBuilder.getThreadFactory(),
				this.sideRoadBuilder.getProducerType(),
				this.sideRoadBuilder.getWaitStrategy());
		disruptor.handleEventsWith(this.sideRoadBuilder.getEventHandlers());
		disruptor.start();
	}

	/**
	 * 发布数据
	 * 
	 * @param input
	 */
	public void publish(T input) {
		RingBuffer<T> ringBuffer = disruptor.getRingBuffer();
		long sequence = ringBuffer.next();

		try {
			T data = ringBuffer.get(sequence);
			sideRoadBuilder.getSideRoadWrapper().wrapper(input, data);// 数据复制
		} finally {
			ringBuffer.publish(sequence);
		}
	}

	public Disruptor<T> getDisruptor() {
		return disruptor;
	}

	public void setDisruptor(Disruptor<T> disruptor) {
		this.disruptor = disruptor;
	}

	public void shutdown() {
		if (disruptor != null) {
			try {
				disruptor.shutdown(sideRoadBuilder.getShutdownTimeout(),
						TimeUnit.MILLISECONDS);
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
		}
	}

}
