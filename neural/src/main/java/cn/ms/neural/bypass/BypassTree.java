package cn.ms.neural.bypass;

public class BypassTree<K, V> {

	public BypassTreeNode<K, V> root;

	public void addNode(K newTreeNode) {
		this.addNode(null, newTreeNode);
	}

	public void addNode(BypassTreeNode<K, V> treeNode, K newTreeNode) {
		this.addNode(treeNode, newTreeNode, null);
	}

	public void addNode(BypassTreeNode<K, V> treeNode, K newTreeNode, V value) {
		if (null == treeNode) {
			if (null == root) {
				root = new BypassTreeNode<K, V>(newTreeNode, value);
			}
		} else {
			BypassTreeNode<K, V> tempTreeNode = this.search(root, newTreeNode);
			if (tempTreeNode != null) {
				throw new IllegalArgumentException(newTreeNode + "重复");
			}
			BypassTreeNode<K, V> temp = new BypassTreeNode<K, V>(newTreeNode, value);
			treeNode.nodes.add(temp);
		}
	}

	public BypassTreeNode<K, V> search(BypassTreeNode<K, V> treeNode, K newTreeNode) {
		BypassTreeNode<K, V> temp = null;
		if (treeNode.key.equals(newTreeNode)) {
			return treeNode;
		}

		for (int i = 0; i < treeNode.nodes.size(); i++) {
			temp = search(treeNode.nodes.get(i), newTreeNode);
			if (null != temp) {
				break;
			}
		}

		return temp;
	}

	public BypassTreeNode<K, V> getNode(K newTreeNode) {
		return search(root, newTreeNode);
	}

}