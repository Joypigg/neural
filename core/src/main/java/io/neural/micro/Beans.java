package io.neural.micro;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean Utils
 * 
 * @author lry
 */
public class Beans {

	/** 
     * Using reflection to realize the object to copy
     * 
     * @param from Data source
     * @param to Object container
     */  
    public static void copyProperties(Object from, Object to) throws Exception {
        copyPropertiesExclude(from, to, null);
    }
      
    /** 
     * Copy object properties
     * 
     * @param from Data source
     * @param to Object container
     * @param excludsArray Exclude list of attributes
     * @throws Exception 
     */
    public static void copyPropertiesExclude(Object from, Object to, String[] excludsArray) throws Exception {
        List<String> excludesList = null;  
        if(excludsArray != null && excludsArray.length > 0) {  
            excludesList = Arrays.asList(excludsArray);
        }  
        
        Method[] fromMethods = from.getClass().getDeclaredMethods();  
        Method[] toMethods = to.getClass().getDeclaredMethods();  
        Method fromMethod = null, toMethod = null;  
        String fromMethodName = null, toMethodName = null;  
        for (int i = 0; i < fromMethods.length; i++) {  
            fromMethod = fromMethods[i];  
            fromMethodName = fromMethod.getName();  
            if (!fromMethodName.contains("query")){
            	if (!fromMethodName.startsWith("is")){
            		continue;
            	}
            }
            
            if(excludesList != null && excludesList.contains(fromMethodName.substring(fromMethodName.startsWith("is")?2:3).toLowerCase())) {  
                continue;  
            }
            
            toMethodName = "set" + fromMethodName.substring(fromMethodName.startsWith("is")?2:3);
            toMethod = findMethodByName(toMethods, toMethodName);
            if (toMethod == null){
            	continue;  
            }
            
            Object value = fromMethod.invoke(from, new Object[0]);
            if(value == null){
            	continue;            	
            }
            if(value instanceof Collection) {
                @SuppressWarnings("rawtypes")
				Collection newValue = (Collection)value;
                if(newValue.size() <= 0){
                	continue;                	
                }
            }
            
            toMethod.invoke(to, new Object[] {value});
        }
    }
      
    /** 
     * Object property value is copied, only the property value of the specified name is copied
     * 
     * @param from Data source
     * @param to Object container
     * @param includsArray Attributes to be copied
     * @throws Exception 
     */  
    public static void copyPropertiesInclude(Object from, Object to, String[] includsArray) throws Exception {
        List<String> includesList = null;
        if(includsArray != null && includsArray.length > 0) {
            includesList = Arrays.asList(includsArray); 
        } else {
            return;
        }
        
        Method[] fromMethods = from.getClass().getDeclaredMethods();
        Method[] toMethods = to.getClass().getDeclaredMethods();
        Method fromMethod = null, toMethod = null;
        String fromMethodName = null, toMethodName = null;
        for (int i = 0; i < fromMethods.length; i++) {
            fromMethod = fromMethods[i];
            fromMethodName = fromMethod.getName();
            if (!fromMethodName.contains("query")){
            	if (!fromMethodName.startsWith("is")){
            		continue;
            	}
            }
            
            String str = fromMethodName.substring(fromMethodName.startsWith("is")?2:3);
            if(!includesList.contains(str.substring(0,1).toLowerCase() + str.substring(1))) {
                continue;
            }
            toMethodName = "set" + fromMethodName.substring(fromMethodName.startsWith("is")?2:3);
            toMethod = findMethodByName(toMethods, toMethodName);
            if (toMethod == null){
            	continue;
            }
            
            Object value = fromMethod.invoke(from, new Object[0]);  
            if(value == null){
            	continue;  
            }
            if(value instanceof Collection) {  
                @SuppressWarnings("rawtypes")
				Collection newValue = (Collection)value;  
                if(newValue.size() <= 0){
                	continue;
                }
            }
            
            toMethod.invoke(to, new Object[] {value});
        }
    }
    
    @SuppressWarnings("rawtypes")
	public static Object copyMapToObj(Map data,Object obj) {
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    Object mapVal=data.get(field);
                    if(mapVal!=null){
                    	Class<?>[] parameterTypes = method.getParameterTypes();
                    	if(parameterTypes.length==1){
                    		Object parameterVal = toConvert(parameterTypes[0], mapVal);
                    		method.invoke(obj, new Object[]{parameterVal});                    		
                    	}
                    }
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        
        return obj;
    }
  
    private static Method findMethodByName(Method[] methods, String name) {
        for (int j = 0; j < methods.length; j++) {
            if (methods[j].getName().equals(name)){
            	return methods[j];
            }
        }
        
        return null;
    }
    
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