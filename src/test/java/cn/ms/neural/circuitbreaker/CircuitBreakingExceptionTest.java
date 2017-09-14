package cn.ms.neural.circuitbreaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * JUnit tests for {@link CircuitBreakingException}.
 */
public class CircuitBreakingExceptionTest {

	private static final String CAUSE_MESSAGE = "Cause message";
	private static final String EXCEPTION_MESSAGE = "Exception message";

	private static final String WRONG_EXCEPTION_MESSAGE = "Wrong exception message";
	private static final String WRONG_CAUSE_MESSAGE = "Wrong cause message";
	private Exception generateCause() {
        return new Exception(CAUSE_MESSAGE);
    }

	@Test(expected = CircuitBreakingException.class)
	public void testThrowingInformativeException() throws Exception {
		throw new CircuitBreakingException(EXCEPTION_MESSAGE, generateCause());
	}

	@Test(expected = CircuitBreakingException.class)
	public void testThrowingExceptionWithMessage() throws Exception {
		throw new CircuitBreakingException(EXCEPTION_MESSAGE);
	}

	@Test(expected = CircuitBreakingException.class)
	public void testThrowingExceptionWithCause() throws Exception {
		throw new CircuitBreakingException(generateCause());
	}

	@Test(expected = CircuitBreakingException.class)
	public void testThrowingEmptyException() throws Exception {
		throw new CircuitBreakingException();
	}

	@Test
	public void testWithCauseAndMessage() throws Exception {
		final Exception exception = new CircuitBreakingException(
				EXCEPTION_MESSAGE, generateCause());
		assertNotNull(exception);
		assertEquals(WRONG_EXCEPTION_MESSAGE, EXCEPTION_MESSAGE,
				exception.getMessage());

		final Throwable cause = exception.getCause();
		assertNotNull(cause);
		assertEquals(WRONG_CAUSE_MESSAGE, CAUSE_MESSAGE, cause.getMessage());
	}

	@Test
	public void testWithoutCause() throws Exception {
		final Exception exception = new CircuitBreakingException(
				EXCEPTION_MESSAGE);
		assertNotNull(exception);
		assertEquals(WRONG_EXCEPTION_MESSAGE, EXCEPTION_MESSAGE,
				exception.getMessage());

		final Throwable cause = exception.getCause();
		assertNull(cause);
	}

	@Test
	public void testWithoutMessage() throws Exception {
		final Exception exception = new CircuitBreakingException(
				generateCause());
		assertNotNull(exception);
		assertNotNull(exception.getMessage());

		final Throwable cause = exception.getCause();
		assertNotNull(cause);
		assertEquals(WRONG_CAUSE_MESSAGE, CAUSE_MESSAGE, cause.getMessage());
	}
}