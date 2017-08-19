package cn.ms.neural.ipfilter;

import java.util.List;

public class ConfIpFilter implements IpFilter {

	private boolean defaultAllow;
	private IpTree allowIpTree;
	private IpTree denyIpTree;
	private boolean allowFirst;

	public ConfIpFilter(IpFilterConf ipFilterConf) {
		defaultAllow = ipFilterConf.isDefaultAllow();
		allowFirst = ipFilterConf.isAllowFirst();
		allowIpTree = makeIpTree(ipFilterConf.getAllowList());
		denyIpTree = makeIpTree(ipFilterConf.getDenyList());
	}

	private IpTree makeIpTree(List<String> ipList) {
		IpTree ipTree = new IpTree();
		for (String ip : ipList) {
			ipTree.add(ip);
		}

		return ipTree;
	}

	@Override
	public boolean accept(String ip) {
		return allowFirst ? accepAllowFirst(ip) : acceptDenyFirst(ip);
	}

	private boolean accepAllowFirst(String ip) {
		if (allowIpTree.containsIp(ip)) {
			return true;
		}
		if (denyIpTree.containsIp(ip)) {
			return false;
		}

		return defaultAllow;
	}

	private boolean acceptDenyFirst(String ip) {
		if (denyIpTree.containsIp(ip)) {
			return false;
		}
		if (allowIpTree.containsIp(ip)) {
			return true;
		}

		return defaultAllow;
	}

}
