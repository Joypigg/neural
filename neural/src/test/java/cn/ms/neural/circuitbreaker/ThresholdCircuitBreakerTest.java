package cn.ms.neural.circuitbreaker;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@code ThresholdCircuitBreaker}.
 */
public class ThresholdCircuitBreakerTest {

    /**
     * Threshold used in tests.
     */
    private static final long threshold = 10L;

    private static final long zeroThreshold = 0L;

    /**
     * Tests that the threshold is working as expected when incremented and no exception is thrown.
     */
    @Test
    public void testThreshold() {
        final ThresholdCircuitBreaker circuit = new ThresholdCircuitBreaker(threshold);
        circuit.incrementAndCheckState(9L);
        assertFalse("Circuit opened before reaching the threshold", circuit.incrementAndCheckState(1L));
    }

    /**
     * Tests that exceeding the threshold raises an exception.
     */
    @Test
    public void testThresholdCircuitBreakingException() {
        final ThresholdCircuitBreaker circuit = new ThresholdCircuitBreaker(threshold);
        circuit.incrementAndCheckState(9L);
        assertTrue("The circuit was supposed to be open after increment above the threshold", circuit.incrementAndCheckState(2L));
    }

    /**
     * Test that when threshold is zero, the circuit breaker is always open.
     */
    @Test
    public void testThresholdEqualsZero() {
        final ThresholdCircuitBreaker circuit = new ThresholdCircuitBreaker(zeroThreshold);
        assertTrue("When the threshold is zero, the circuit is supposed to be always open", circuit.incrementAndCheckState(0L));
    }

    /**
     * Tests that closing a {@code ThresholdCircuitBreaker} resets the internal counter.
     */
    @Test
    public void testClosingThresholdCircuitBreaker() {
        final ThresholdCircuitBreaker circuit = new ThresholdCircuitBreaker(threshold);
        circuit.incrementAndCheckState(9L);
        circuit.close();
        // now the internal counter is back at zero, not 9 anymore. So it is safe to increment 9 again
        assertFalse("Internal counter was not reset back to zero", circuit.incrementAndCheckState(9L));
    }

    /**
     * Tests that we can get the threshold value correctly.
     */
    @Test
    public void testGettingThreshold() {
        final ThresholdCircuitBreaker circuit = new ThresholdCircuitBreaker(threshold);
        assertEquals("Wrong value of threshold", Long.valueOf(threshold), Long.valueOf(circuit.getThreshold()));
    }

}