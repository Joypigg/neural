package cn.ms.neural.bypass;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.Result;
import cn.ms.neural.ResultType;

public class BypassEngineTest {

	BypassEngine bypassEngine = new BypassEngine();

	public BypassEngineTest() {
		BypassTree<String, String> tree = new BypassTree<String, String>();
		tree.addNode("bypass");
		tree.addNode(tree.getNode("bypass"), "weixin");
		tree.addNode(tree.getNode("bypass"), "app");
		tree.addNode(tree.getNode("weixin"), "weixinQuery", "S01");
		tree.addNode(tree.getNode("weixin"), "weixinAdd", "S02");
		tree.addNode(tree.getNode("app"), "appQuery", "S02");
		tree.addNode(tree.getNode("app"), "appAdd", "S01");
		bypassEngine.notifys(tree);
	}

	@Test
	public void testweinQuery() throws Exception {
		Result result = bypassEngine.doBypass("weixinQuery");
		Assert.assertNotNull(result);
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());
		Assert.assertEquals("S01", result.getValue());
	}

	@Test
	public void testWeixinAdd() throws Exception {
		Result result = bypassEngine.doBypass("weixinAdd");
		Assert.assertNotNull(result);
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());
		Assert.assertEquals("S02", result.getValue());
	}

	@Test
	public void testAppQuery() throws Exception {
		Result result = bypassEngine.doBypass("appQuery");
		Assert.assertNotNull(result);
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());
		Assert.assertEquals("S02", result.getValue());
	}

	@Test
	public void testAppAdd() throws Exception {
		Result result = bypassEngine.doBypass("appAdd");
		Assert.assertNotNull(result);
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());
		Assert.assertEquals("S01", result.getValue());
	}

	@Test
	public void testNonInitialize() throws Exception {
		BypassEngine bypassEngine1 = new BypassEngine();
		Result result = bypassEngine1.doBypass("weixinQuery");
		Assert.assertNotNull(result);
		Assert.assertEquals(ResultType.NONINITIALIZE, result.getResultType());
	}

	@Test
	public void testNotFound() throws Exception {
		Result result = bypassEngine.doBypass("weixinTest");
		Assert.assertNotNull(result);
		Assert.assertEquals(ResultType.NOTFOUND, result.getResultType());
	}

}