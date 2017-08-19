package cn.ms.neural.parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Assert;
import org.junit.Test;

public class ParameterTest {

	@Test
	public void testParameter() {
		Parameter parameter = new Parameter();

		Assert.assertNotNull(parameter);

		Assert.assertFalse(Parameter.checkType("int", "sd"));
		Assert.assertFalse(Parameter.checkType("boolean", "true1"));
		Assert.assertFalse(Parameter.checkType("float", "wdd"));
		Assert.assertFalse(Parameter.checkType("long", "dasd"));
		Assert.assertFalse(Parameter.checkType("short", "dasddd"));
		Assert.assertFalse(Parameter.checkType("double", "asdasd"));
		Assert.assertTrue(Parameter.checkType("string", "asdasdsa"));
		Assert.assertFalse(Parameter.checkType("bigdecimal", "qwewqewq"));
		Assert.assertFalse(Parameter.checkType("sdfsdf", "12321"));
	}

	@Test
	public void testParamResult() {
		ParamResult paramResult = new ParamResult(false, "", "");
		paramResult.setMsg("msg");
		paramResult.setResult(false);
		paramResult.setTitle("title");

		Assert.assertEquals(paramResult.getMsg(), "msg");
		Assert.assertNotEquals(paramResult.toString().length(), 0);
		Assert.assertTrue(true);
	}

	@Test
	public void testParamRule() {
		ParamRule paramResult = new ParamRule("", false, "", 32);
		paramResult.setAllowNull(true);
		paramResult.setKey("key");
		paramResult.setLength(23);
		paramResult.setType("int");

		Assert.assertNotEquals(paramResult.toString().length(), 0);
		Assert.assertTrue(true);
	}

	@Test
	public void testRuleNull() {
		Assert.assertTrue(Parameter.check(null, null).isResult());
	}

	@Test
	public void testAllType() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key1", "12345");
		map.put("key2", "true");
		map.put("key3", "3.2f");
		map.put("key4", "12345678");
		map.put("key5", "12345");
		map.put("key7", "332.234");
		map.put("key8", "zcw2dd");
		map.put("key9", "12345678901234567890123456789022");

		ConcurrentSkipListSet<ParamRule> rules = new ConcurrentSkipListSet<ParamRule>();
		rules.add(new ParamRule("key1", false, "int", 5));
		rules.add(new ParamRule("key2", false, "boolean", 0));
		rules.add(new ParamRule("key3", false, "float", 0));
		rules.add(new ParamRule("key4", false, "long", 0));
		rules.add(new ParamRule("key5", false, "short", 0));
		;
		rules.add(new ParamRule("key7", false, "double", 0));
		rules.add(new ParamRule("key8", false, "string", 0));
		rules.add(new ParamRule("key9", false, "bigdecimal", 32));
		Assert.assertTrue(Parameter.check(map, rules).isResult());
	}

	@Test
	public void testAllowNull() {
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("key2", "test");
		ConcurrentSkipListSet<ParamRule> rules1 = new ConcurrentSkipListSet<ParamRule>();
		rules1.add(new ParamRule("key1", false, "boolean", 0));
		Assert.assertFalse(Parameter.check(map1, rules1).isResult());
		Assert.assertEquals("PARAMETER_NOTALLOWED_EMPTY",
				Parameter.check(map1, rules1).getTitle());

		ConcurrentSkipListSet<ParamRule> rules2 = new ConcurrentSkipListSet<ParamRule>();
		rules2.add(new ParamRule("key1", true, "boolean", 0));
		Assert.assertTrue(Parameter.check(map1, rules2).isResult());
	}

	@Test
	public void testLength() {
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("key1", "test");
		ConcurrentSkipListSet<ParamRule> rules1 = new ConcurrentSkipListSet<ParamRule>();
		rules1.add(new ParamRule("key1", false, "string", 5));
		Assert.assertFalse(Parameter.check(map1, rules1).isResult());
		Assert.assertEquals("PARAMETER_ILLEGAL_LENGTH",
				Parameter.check(map1, rules1).getTitle());

		map1.put("key1", "test1");
		Assert.assertTrue(Parameter.check(map1, rules1).isResult());
		Assert.assertEquals("PARAMETER_RULECHECK_OK",
				Parameter.check(map1, rules1).getTitle());

	}

	@Test
	public void testType() {
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("key1", "test");
		ConcurrentSkipListSet<ParamRule> rules1 = new ConcurrentSkipListSet<ParamRule>();
		rules1.add(new ParamRule("key1", false, "boolean", 0));
		Assert.assertFalse(Parameter.check(map1, rules1).isResult());
		Assert.assertEquals("PARAMETER_ILLEGAL_TYPE",
				Parameter.check(map1, rules1).getTitle());

		map1.put("key1", "true");
		Assert.assertTrue(Parameter.check(map1, rules1).isResult());
		Assert.assertEquals("PARAMETER_RULECHECK_OK",
				Parameter.check(map1, rules1).getTitle());
	}

}
