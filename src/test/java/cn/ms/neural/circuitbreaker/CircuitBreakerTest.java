package cn.ms.neural.circuitbreaker;

import java.util.concurrent.TimeUnit;

public class CircuitBreakerTest {

	public static void main(String[] args) {
		EventCountCircuitBreaker breaker = new EventCountCircuitBreaker(5, 2, TimeUnit.MINUTES, 5, 10, TimeUnit.MINUTES);
		if (breaker.checkState()) {
	         try {
	        	 
	         } catch (Exception ex) {
	             breaker.incrementAndCheckState();
	         }
	     } else {
	         // return an error code, use an alternative service, etc.
	     }
	}
	
}
