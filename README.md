# 微服务神经元(Neural)

微服务架构中的神经组织，主要为分布式架构提供了集群容错的三大利刃：限流、降级和熔断。 

微服务基础设施<font color="red">QQ交流群：191958521</font>


微服务的可靠性：
1 流量限流
2 集群容错
	2.1 路由容错：失败重试、失败切换
	2.2 服务降级：容错降级
	2.3 熔断隔离

http://www.51testing.com/html/87/n-3719887.html


## 0 SPI
### 0.1 JDK标准的SPI缺陷

+ JDK标准的SPI会一次性实例化扩展点所有实现，如果有扩展实现初始化很耗时，但如果没用上也加载，会很浪费资源
+ 如果扩展点加载失败，连扩展点的名称都拿不到了
+ 增加了对扩展点IoC和AOP的支持，一个扩展点可以直接setter注入其它扩展点

## 1 限流（Limiter）
在分布式架构中，限流的场景主要分为两种：injvm模式和cluster模式。

### 1.1 injvm模式
#### 1.1.1 并发量（Concurrency）
使用JDK中的信号量(Semaphore)进行控制。

#### 1.1.2 速率控制（Rate）
使用Google的Guava中的限速器(RateLimiter)进行控制。

### 1.2 cluster模式
分布式限流主要适用于保护集群的安全或者用于严格控制用户的请求量（API经济）。


## 2 降级（Degrade）
在分布式架构中，降级的场景主要分为两种：injvm模式和cluster模式。

### 2.1 injvm模式

### 2.2 cluster模式



## 3 熔断（CircuitBreaker）
在分布式架构中，熔断的场景主要分为两种：injvm模式和cluster模式。

### 3.1 injvm模式

### 3.2 cluster模式
