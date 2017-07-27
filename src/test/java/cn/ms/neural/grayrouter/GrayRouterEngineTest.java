package cn.ms.neural.grayrouter;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.ms.neural.Result;
import cn.ms.neural.ResultType;

public class GrayRouterEngineTest {

	private GrayRouterEngine newGrayRouter() {
		GrayRouterEngine grayRouterEngine = new GrayRouterEngine();
		String[] granularities = { "areaId", "channelId" };
		grayRouterEngine.initialize(granularities);
		
		Map<String, String> routerRuleMap = new HashMap<String, String>();
		routerRuleMap.put("(shenzheng),(weixin01),(testService01)", "1.0.0");
		routerRuleMap.put("(beijing),(weixin01),(testService01)", "2.0.0");
		routerRuleMap.put("(shenzheng),(weixin02),(testService01)", "3.0.0");
		routerRuleMap.put("(shenzheng),(weixin01),(testService02)", "1.0.0");
		grayRouterEngine.notifys(routerRuleMap);
		return grayRouterEngine;
	}

	@Test
	public void testNonInitialize() throws Exception {
		GrayRouterEngine grayRouterEngine = new GrayRouterEngine();
		Result result =  grayRouterEngine.doGrayRouter(new HashMap<String, Object>(), "testService01");
		Assert.assertEquals(ResultType.NONINITIALIZE, result.getResultType());
	}
	
	@Test
	public void testShenzhengWeixin01TestService01() throws Exception {
		GrayRouterEngine grayRouterEngine =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "shenzheng");
		routers.put("channelId", "weixin01");
		Result result =  grayRouterEngine.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());
		Assert.assertEquals("1.0.0", result.getValue());
	}
	
	@Test
	public void testBeijingWeixin01TestService01() throws Exception {
		GrayRouterEngine grayRouterEngine =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "beijing");
		routers.put("channelId", "weixin01");
		Result result =  grayRouterEngine.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());
		Assert.assertEquals("2.0.0", result.getValue());
	}
	
	@Test
	public void testShenzhengWeixin02TestService01() throws Exception {
		GrayRouterEngine grayRouterEngine =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "shenzheng");
		routers.put("channelId", "weixin02");
		Result result =  grayRouterEngine.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());
		Assert.assertEquals("3.0.0", result.getValue());
	}
	
	@Test
	public void testShenzhengWeixin01TestService02() throws Exception {
		GrayRouterEngine grayRouterEngine =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "shenzheng");
		routers.put("channelId", "weixin01");
		Result result =  grayRouterEngine.doGrayRouter(routers, "testService02");
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());
		Assert.assertEquals("1.0.0", result.getValue());
	}
	
	@Test
	public void testGuangzhouWeixin01TestService01() throws Exception {
		GrayRouterEngine grayRouterEngine =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "guangzhou");
		routers.put("channelId", "weixin01");
		Result result =  grayRouterEngine.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.NOTFOUND, result.getResultType());
	}
	
	@Test
	public void testShenzhengApp01TestService01() throws Exception {
		GrayRouterEngine grayRouterEngine =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "shenzheng");
		routers.put("channelId", "app01");
		Result result =  grayRouterEngine.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.NOTFOUND, result.getResultType());
	}
	
	@Test
	public void testShenzhengWeixin01TestService03() throws Exception {
		GrayRouterEngine grayRouterEngine =  this.newGrayRouter();
		
		Map<String, Object> routers = new HashMap<String, Object>();
		routers.put("areaId", "shenzheng");
		routers.put("channelId", "weixin01");
		Result result =  grayRouterEngine.doGrayRouter(routers, "testService03");
		Assert.assertEquals(ResultType.NOTFOUND, result.getResultType());
	}

	@Test
	public void testGrapRouterRegexEquals() throws Exception {
		GrayRouterEngine grayRouterEngine = new GrayRouterEngine();
		String[] granularities = {};
		grayRouterEngine.initialize(granularities);
		
		Map<String, String> routerRuleMap = new HashMap<String, String>();
		routerRuleMap.put("testService01", "1.0.0");
		grayRouterEngine.notifys(routerRuleMap);
		
		Map<String, Object> routers = new HashMap<String, Object>();
		Result result =  grayRouterEngine.doGrayRouter(routers, "testService01");
		Assert.assertEquals(ResultType.SUCCESS, result.getResultType());
		Assert.assertEquals("1.0.0", result.getValue());
	}
	
}
