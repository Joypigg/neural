package cn.ms.neural.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spi有多个实现时，可以根据条件进行过滤、排序后再返回。
 *
 * @author lry
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Activation {

	/** seq号越小，在返回的list<Instance>中的位置越靠前 */
	int seq() default 20;

	/** spi 的key，获取spi列表时，根据key进行匹配，当key中存在待过滤的search-key时，匹配成功 */
	String[] key() default "";

}
