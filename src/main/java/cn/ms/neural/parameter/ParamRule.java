package cn.ms.neural.parameter;

public class ParamRule implements Comparable<ParamRule> {

	private String key;
	private boolean allowNull;
	private String type;
	private int length;

	@Override
	public int compareTo(ParamRule o) {
		return o.getKey().compareTo(this.key);
	}

	public ParamRule(String key, boolean allowNull, String type, int length) {
		this.key = key;
		this.allowNull = allowNull;
		this.type = type;
		this.length = length;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isAllowNull() {
		return allowNull;
	}

	public void setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "ParameterRule [key=" + key + ", allowNull=" + allowNull
				+ ", type=" + type + ", length=" + length + "]";
	}

}
