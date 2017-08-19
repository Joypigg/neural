package cn.ms.neural;

public class Result {

	private ResultType resultType;
	private String value;

	public Result() {
	}

	public Result(ResultType resultType) {
		this.resultType = resultType;
	}

	public Result(ResultType resultType, String value) {
		this.resultType = resultType;
		this.value = value;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Result [resultType=" + resultType + ", value=" + value
				+ "]";
	}

}
