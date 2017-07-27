package cn.ms.neural.ipfilter;

/**
 * IP pattern<br>
 * <br>
 * Available string for parameter value of allow/deny method of Config class<br>
 * simple ip: 1.2.3.4 (exact matching)<br>
 * network range: 1.1.1.64/26 (1.1.1.64 ~ 1.1.1.127) ** 64/26: 26 means a length
 * of network address<br>
 * all: 1.2.3.*, 1.2.*, 1.*, *<br>
 * 
 * IP段可用IP数目快速查询：<br>
 * /30 IP段共1个可用IP<br>
 * /29 IP段共5个可用IP<br>
 * /28 IP段共13个可用IP<br>
 * /27 IP段共29个可用IP<br>
 * /26 IP段共61个可用IP<br>
 * /24 IP段共253个可用IP<br>
 * @author lry
 */
public class IpFilters {
	
	public static IpFilter create(IpFilterConf ipFilterConf) {
		return new ConfIpFilter(ipFilterConf);
	}

	public static IpFilter createCached(IpFilterConf ipFilterConf) {
		return new CachedIpFilter(create(ipFilterConf));
	}
	
}
