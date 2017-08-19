package cn.ms.neural.parameter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * int/boolean/float/long/short/double/string/bigdecimal
 * 
 * @author lry
 */
public class Parameter {

	public static ParamResult check(Map<String, Object> map, ConcurrentSkipListSet<ParamRule> rules) {
		if (rules == null || rules.size() == 0) {
			return new ParamResult(true, "PARAMETER_RULE_EMPTY", "The parameter rule is null or empty.");
		}

		for (ParamRule rule : rules) {
			Object object = map.get(rule.getKey());

			// 校验不允许为空
			if (!rule.isAllowNull()) {// 不能为空
				if (object == null || "".equals(object)) {
					String msg = String.format("The parameter(%s) is not allowed to be empty.", rule.getKey());
					return new ParamResult(false, "PARAMETER_NOTALLOWED_EMPTY", msg);
				}
			}

			// 参数值长度
			if (rule.getLength() > 0) {
				if (object != null){
					if (rule.getLength() != String.valueOf(object).length()) {
						String msg = String.format("The length of the parameter(%s) must be '%s'.", rule.getKey(), rule.getLength());
						return new ParamResult(false, "PARAMETER_ILLEGAL_LENGTH", msg);
					}					
				}
			}

			// 校验类型是否合法
			if (rule.getType() != null) {
				if (object != null) {
					if (!checkType(rule.getType(), object)) {
						String msg = String.format("The type of parameter(%s) a must be '%s'.", rule.getKey(), rule.getType());
						return new ParamResult(false, "PARAMETER_ILLEGAL_TYPE", msg);
					}					
				}
			}
		}

		return new ParamResult(true, "PARAMETER_RULECHECK_OK", "The parameter check through.");
	}

	/**
	 * int/boolean/float/long/short/double/string/bigdecimal
	 * 
	 * @param type
	 * @param obj
	 * @return
	 */
	public static boolean checkType(String type, Object object) {
		String obj = String.valueOf(object);

		switch (type) {
		case "int":
			try {
				Integer.parseInt(obj);
				return true;
			} catch (Exception e) {
				return false;
			}
		case "float":
			try {
				Float.parseFloat(obj);
				return true;
			} catch (Exception e) {
				return false;
			}
		case "long":
			try {
				Long.parseLong(obj);
				return true;
			} catch (Exception e) {
				return false;
			}
		case "short":
			try {
				Short.parseShort(obj);
				return true;
			} catch (Exception e) {
				return false;
			}
		case "double":
			try {
				Double.parseDouble(obj);
				return true;
			} catch (Exception e) {
				return false;
			}
		case "boolean":
			return Boolean.parseBoolean(obj);
		case "string":
			return true;
		case "bigdecimal":
			try {
				new BigDecimal(obj);
				return true;
			} catch (Exception e) {
				return false;
			}
		default:
			return false;
		}
	}

}
