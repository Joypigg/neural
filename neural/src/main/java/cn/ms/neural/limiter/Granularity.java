package cn.ms.neural.limiter;

public class Granularity {

	private String category;
	private Long maxAmount;
	private Long nowAmount;

	public Granularity() {
	}

	public Granularity(String category, Long maxAmount) {
		this(category, maxAmount, null);
	}
	
	public Granularity(String category, Long maxAmount, Long nowAmount) {
		this.category = category;
		this.maxAmount = maxAmount;
		this.nowAmount = nowAmount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Long maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Long getNowAmount() {
		return nowAmount;
	}

	public void setNowAmount(Long nowAmount) {
		this.nowAmount = nowAmount;
	}

	@Override
	public String toString() {
		return "Granularity [category=" + category + ", maxAmount=" + maxAmount
				+ ", nowAmount=" + nowAmount + "]";
	}

}
