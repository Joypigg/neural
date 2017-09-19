package io.neural.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据转换工具
 * 
 * @author lry
 */
public class ConvertUtils {
	
	public static Object toConvert(Class<?> cl, Object val) {
		for (Map.Entry<Class<?>[], CT<?>> entry : TYPES.entrySet()) {
			Class<?>[] types = entry.getKey();
			for (Class<?> type:types) {
				if (type.equals(cl) || type.getName().equals(cl.getName())) {
					return entry.getValue().covert(val);
				}
			}
		}
		
		return String.valueOf(val);
	}
	
	private static final Map<Class<?>[], CT<?>> TYPES = new HashMap<Class<?>[], CT<?>>();

	static {
		// boolean、Boolean
		TYPES.put(new Class<?>[]{boolean.class, Boolean.class}, new CT<Boolean>() {
			public Boolean covert(Object obj) {return Boolean.parseBoolean(String.valueOf(obj));}
		});
		// byte、Byte
		TYPES.put(new Class<?>[]{byte.class, Byte.class}, new CT<Byte>() {
			public Byte covert(Object obj) {return Byte.parseByte(String.valueOf(obj));}
		});
		// short、Short
		TYPES.put(new Class<?>[]{short.class, Short.class}, new CT<Boolean>() {
			public Boolean covert(Object obj) {return Boolean.parseBoolean(String.valueOf(obj));}
		});
		// int、Integer
		TYPES.put(new Class<?>[]{int.class, Integer.class}, new CT<Integer>() {
			public Integer covert(Object obj) {return Integer.parseInt(String.valueOf(obj));}
		});
		// long、Long
		TYPES.put(new Class<?>[]{long.class, Long.class}, new CT<Long>() {
			public Long covert(Object obj) {return Long.parseLong(String.valueOf(obj));}
		});
		// float、Float
		TYPES.put(new Class<?>[]{float.class, Float.class}, new CT<Float>() {
			public Float covert(Object obj) {return Float.parseFloat(String.valueOf(obj));}
		});
		// double、Double
		TYPES.put(new Class<?>[]{double.class, Double.class}, new CT<Double>() {
			public Double covert(Object obj) {return Double.parseDouble(String.valueOf(obj));}
		});
		// char、Character
		TYPES.put(new Class<?>[]{char.class, Character.class}, new CT<Character>() {
			public Character covert(Object obj) {return new Character(String.valueOf(obj).charAt(0));}
		});
	}
	
	public interface CT<T>{
		T covert(Object obj);
	}
	
}