package io.neural.isolation;

public class IsolationBuilder {

	/** 隔离资源组KEY **/
	private String groupKey;
	/** 隔离资命令KEY **/
	private String commondKey;
	/** 线程池名称 **/
	private String threadPoolName;
	/** 隔离类型 **/
	private IsolationType isolationType;
	
	public String getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
	}
	public String getCommondKey() {
		return commondKey;
	}
	public void setCommondKey(String commondKey) {
		this.commondKey = commondKey;
	}
	public String getThreadPoolName() {
		return threadPoolName;
	}
	public void setThreadPoolName(String threadPoolName) {
		this.threadPoolName = threadPoolName;
	}
	public IsolationType getIsolationType() {
		return isolationType;
	}
	public void setIsolationType(IsolationType isolationType) {
		this.isolationType = isolationType;
	}

}
