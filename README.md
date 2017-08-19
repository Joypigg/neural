# 微服务神经元(Neural)

微服务架构中的神经组织，主要为分布式架构提供三大利刃：限流、降级和熔断。 

微服务基础设施<font color="red">QQ交流群：191958521</font>


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
