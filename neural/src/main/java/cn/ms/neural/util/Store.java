package cn.ms.neural.util;

import java.io.Serializable;

public class Store<K, V> implements Serializable {

	private static final long serialVersionUID = 1L;

	private K key;
	private V value;

	public Store() {
	}

	public Store(K key) {
		this.key = key;
	}

	public Store(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Store [key=" + key + ", value=" + value + "]";
	}

}
