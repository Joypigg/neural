package cn.ms.neural.parameter;

public class ParamResult {

	private boolean result;
	private String title;
	private String msg;

	public ParamResult(boolean result, String title, String msg) {
		this.result = result;
		this.title = title;
		this.msg = msg;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ParameterResult [result=" + result + ", title=" + title
				+ ", msg=" + msg + "]";
	}

}
