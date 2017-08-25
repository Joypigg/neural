package cn.ms.neural.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * NSPI<br>
 * <br>
 * 1.支持自定义实现类为单例/多例<br>
 * 2.支持设置默认的实现类<br>
 * 3.支持实现类order排序<br>
 * 4.支持实现类定义特征属性category,用于区分多维度的不同类别<br>
 * 
 * @author lry
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface NSPI {

	/** 自定义默认的实现类ID **/
	String value() default "";

	/** 声明每次获取实现类时是否需要创建新对象,即是否单例 **/
	boolean single() default false;

}
