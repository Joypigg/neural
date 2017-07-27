package cn.ms.neural.retryer;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import cn.ms.neural.retryer.strategy.StopStrategies;

import com.google.common.base.Predicates;

public class RetryerTest {

	public static void main(String[] args) {
		Callable<Boolean> callable = new Callable<Boolean>() {
			int time = 0;
		    public Boolean call() throws Exception {
		    	if(time > 1){
		    		return true; // do something useful here
		    	}
		    	time++;
		    	throw new IOException();
		    }
		};

		Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
		        .retryIfResult(Predicates.<Boolean>isNull())
		        .retryIfExceptionOfType(IOException.class)
		        .retryIfRuntimeException()
		        .withStopStrategy(StopStrategies.stopAfterAttempt(3))
		        .build();
		try {
		    System.out.println("执行结果:"+retryer.call(callable));
		} catch (RetryException e) {
		    e.printStackTrace();
		} catch (ExecutionException e) {
		    e.printStackTrace();
		}
	}
	
}
