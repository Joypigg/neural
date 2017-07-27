package cn.ms.neural;

import org.junit.Assert;
import org.junit.Test;

public class ResultTest {

	@Test
	public void testRouterResult() throws Exception {
		Result result = new Result();
		Assert.assertNotNull(result);
		Assert.assertNull(result.getValue());
		Assert.assertNull(result.getResultType());
	}

	@Test
	public void testName() throws Exception {
		Result result = new Result();
		result.setResultType(ResultType.SUCCESS);
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());

		result.setValue("1.0.0");
		Assert.assertEquals("1.0.0", result.getValue());
		
		String toString = result.toString();
		Assert.assertNotNull(toString);
	}

}
