package cn.ms.neural.limiter;

/**
 * 限流时间精度
 * 
 * @author lry
 */
public enum TimeGranularity {

	/** 秒 **/
	SECOND,

	/** 分 **/
	MINUTE,

	/** 时 **/
	HOUR,

	/** 天 **/
	DAY,

	/** 月 **/
	MONTH,

	/** 年 **/
	YEAR,

	/** 自定义 **/
	CUSTOM

}
