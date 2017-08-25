package cn.ms.neural.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface NSPI {

	/**
	 * 用于设置默认的实现类KEY
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 实现类实例是否是单例,默认为非单例,即每次都重新创建
	 * 
	 * @return
	 */
	boolean single() default false;

}
