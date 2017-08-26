# 微服务神经元(Neural)

[TOC]


微服务架构中的神经组织，主要为分布式架构提供了集群容错的三大利刃：限流、降级和熔断。 

微服务基础设施<font color="red">QQ交流群：191958521</font>


微服务的可靠性：
1 流量限流
2 集群容错
	2.1 路由容错：失败重试、失败切换
	2.2 服务降级：容错降级
	2.3 熔断隔离

http://www.51testing.com/html/87/n-3719887.html


## 1 NSPI
### 1.1 JDK中SPI缺陷

+ JDK标准的SPI会一次性实例化扩展点所有实现，如果有扩展实现初始化很耗时，但如果没用上也加载，会很浪费资源
+ 不支持扩展点的IoC和AOP
+ 不支持实现排序
+ 不支持实现类分组
+ 不支持单例/多例的选择

### 1.2 NSPI功能特性

+ 支持自定义实现类为单例/多例

* 支持设置默认的实现类
* 支持实现类order排序
* 支持实现类定义特征属性category，用于区分多维度的不同类别
* 支持根据category属性值来搜索实现类
* 支持自动扫描实现类
* 支持手动添加实现类
* 支持获取所有实现类
* 支持只创建所需实现类，解决JDK原生的全量方式
* 支持自定义ClassLoader来加载class

+ **TODO：**需要实现对扩展点IoC和AOP的支持，一个扩展点可以直接setter注入其它扩展点。

### 1.3 使用方式

```java
package cn.ms.neural.demo;

//第一步：定义接口
@NSPI
public interface IDemo {}

//第二步：定义接口实现类
@Activation("demo1")
public class Demo1Impl implements IDemo {}
@Activation("demo2")
public class Demo2Impl implements IDemo {}

//第三步：使用接口全路径（包名+类名）创建接口资源文件
src/main/resources/META-INF/services/cn.ms.neural.demo.IDemo

//第四步：在接口资源文件中写入实现类全路径（包名+类名）
cn.ms.neural.demo.Demo1Impl
cn.ms.neural.demo.Demo2Impl

//第五步：使用ExtensionLoader来获取接口实现类
IDemo demo1 =ExtensionLoader.getExtensionLoader(IDemo.class).getExtension("demo1");
IDemo demo2 =ExtensionLoader.getExtensionLoader(IDemo.class).getExtension("demo2");
```



## 2 限流（Limiter）
在分布式架构中，限流的场景主要分为两种：injvm模式和cluster模式。

### 2.1 injvm模式
#### 2.1.1 并发量（Concurrency）
使用JDK中的信号量(Semaphore)进行控制。

```java
Semaphore semaphore = new Semaphore(10,true);
semaphore.acquire();
//do something here
semaphore.release();
```

#### 2.1.2 速率控制（Rate）
使用Google的Guava中的限速器(RateLimiter)进行控制。

```java
RateLimiter limiter = RateLimiter.create(10.0); // 每秒不超过10个任务被提交
limiter.acquire(); // 请求RateLimite
```

### 2.2 cluster模式

分布式限流主要适用于保护集群的安全或者用于严格控制用户的请求量（API经济）。



## 3 熔断（CircuitBreaker）
在分布式架构中，熔断的场景主要分为两种：injvm模式和cluster模式。

### 3.1事件统计熔断器（EventCountCircuitBreaker）

在指定时间周期内根据事件发生的次数来实现精简版熔断器。如10秒之内触发5次事件，则进行熔断。

### 3.2 门限熔断器（ThresholdCircuitBreaker）



## 4 降级（Degrade）

在分布式架构中，降级的场景主要分为两种：injvm模式和cluster模式。



## 5 重试（Retryer）

### 5.1 重试策略

#### 5.1.1 块策略（BlockStrategy）

使当前线程使用Thread.sleep()的方式进行休眠重试。

#### 5.1.2 停止策略（StopStrategy）

+ NeverStopStrategy：从不停止策略


+ StopAfterAttemptStrategy：尝试后停止策略


+ StopAfterDelayStrategy：延迟后停止策略

#### 5.1.3 等待策略（WaitStrategy）

+ FixedWaitStrategy：固定休眠时间等待策略
+ RandomWaitStrategy：随机休眠时间等待策略，支持设置随机休眠时间的下限值（minmum）与上限值（maxmum）
+ IncrementingWaitStrategy：定长递增休眠时间等待策略
+ ExponentialWaitStrategy：指数函数（2^x，其中x表示尝试次数）递增休眠时间等待策略。支持设置休眠时间的上限值（maximumWait）
+ FibonacciWaitStrategy：斐波那契数列递增休眠时间等待策略。支持设置休眠时间的上限值（maximumWait）
+ CompositeWaitStrategy：复合等待策略，即支持以上等待策略的组合计算休眠时间，最终休眠时间是以上策略中休眠时间之和
+ ExceptionWaitStrategy：异常等待策略

### 5.2 指定结果重试

retryIfResult(Predicate< V> resultPredicate)：设置重试不满足条件的结果

eg：如果返回结果为空则重试：retryIfResult(Predicates.< Boolean>isNull())

### 5.3 指定异常重试

+ retryIfException()：重试所有异常
+ retryIfRuntimeException()：重试运行时异常
+ retryIfExceptionOfType(Class<? extends Throwable> exceptionClass)：重试指定类型异常
+ retryIfException(Predicate< Throwable> exceptionPredicate) ：自定义过滤后的异常重试

### 5.4 重试监听器（RetryListener）

withRetryListener(RetryListener listener)：添加重试监听器

### 5.5 尝试时间限制器（AttemptTimeLimiter）

withAttemptTimeLimiter(AttemptTimeLimiter< V> attemptTimeLimiter)：添加尝试时间限制器