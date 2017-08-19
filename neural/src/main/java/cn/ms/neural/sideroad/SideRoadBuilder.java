package cn.ms.neural.sideroad;

import java.util.concurrent.ThreadFactory;

import cn.ms.micro.threadpool.NamedThreadFactory;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 数据旁路模型
 * 
 * @author lry
 *
 * @param <T>
 */
public class SideRoadBuilder<T> {

	private EventFactory<T> eventFactory;
	private int ringBufferSize = 1 * 1024;
	private SideRoadWrapper<T> sideRoadWrapper;
	private EventHandler<T>[] eventHandlers;
	private ProducerType producerType = ProducerType.SINGLE;
	private WaitStrategy waitStrategy = new YieldingWaitStrategy();
	private ThreadFactory threadFactory = new NamedThreadFactory();

	private long shutdownTimeout = 60 * 1000;

	public EventFactory<T> getEventFactory() {
		return eventFactory;
	}

	public SideRoadBuilder<T> setEventFactory(EventFactory<T> eventFactory) {
		this.eventFactory = eventFactory;
		return this;
	}

	public int getRingBufferSize() {
		return ringBufferSize;
	}

	public SideRoadBuilder<T> setRingBufferSize(int ringBufferSize) {
		this.ringBufferSize = ringBufferSize;
		return this;
	}

	public SideRoadWrapper<T> getSideRoadWrapper() {
		return sideRoadWrapper;
	}

	public SideRoadBuilder<T> setSideRoadWrapper(
			SideRoadWrapper<T> sideRoadWrapper) {
		this.sideRoadWrapper = sideRoadWrapper;
		return this;
	}

	public EventHandler<T>[] getEventHandlers() {
		return eventHandlers;
	}

	public SideRoadBuilder<T> setEventHandlers(
			@SuppressWarnings("unchecked") EventHandler<T>... eventHandlers) {
		this.eventHandlers = eventHandlers;
		return this;
	}

	public ProducerType getProducerType() {
		return producerType;
	}

	public SideRoadBuilder<T> setProducerType(ProducerType producerType) {
		this.producerType = producerType;
		return this;
	}

	public WaitStrategy getWaitStrategy() {
		return waitStrategy;
	}

	public SideRoadBuilder<T> setWaitStrategy(WaitStrategy waitStrategy) {
		this.waitStrategy = waitStrategy;
		return this;
	}

	public ThreadFactory getThreadFactory() {
		return threadFactory;
	}

	public SideRoadBuilder<T> setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
		return this;
	}

	public long getShutdownTimeout() {
		return shutdownTimeout;
	}

	public SideRoadBuilder<T> setShutdownTimeout(long shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
		return this;
	}

}
