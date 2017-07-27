package cn.ms.neural.bypass;

import java.util.ArrayList;
import java.util.List;

public class BypassTreeNode<K, V> {

	public K key;
	public V value;
	private BypassTreeNode<K, V> parent;
	public List<BypassTreeNode<K, V>> nodes;

	public BypassTreeNode(K key, V value) {
		this.key = key;
		this.value = value;
		parent = null;
		nodes = new ArrayList<BypassTreeNode<K, V>>();
	}

	public BypassTreeNode<K, V> getParent() {
		return parent;
	}

}