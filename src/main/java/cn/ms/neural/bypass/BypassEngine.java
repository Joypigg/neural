package cn.ms.neural.bypass;

import cn.ms.neural.ResultType;
import cn.ms.neural.Result;

/**
 * 流量分流
 * 
 * @author lry
 */
public class BypassEngine {

	volatile BypassTree<String, String> bypassTree;

	public void notifys(BypassTree<String, String> tree) {
		this.bypassTree = tree;
	}

	public Result doBypass(String node) {
		if (bypassTree == null) {
			return new Result(ResultType.NONINITIALIZE);
		}

		BypassTreeNode<String, String> treeNode = bypassTree.getNode(node);
		if (treeNode == null) {
			return new Result(ResultType.NOTFOUND);
		}

		return new Result(ResultType.SUCCESS, treeNode.value);
	}

}