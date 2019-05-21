package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;

public class DiscountParam {

	private Integer pressId;
	
	private Integer vipTypeId;
	
	private Integer duration;
	
	@NotEmpty(message="折扣不能为空")
	private Double strategy;

	public Integer getPressId() {
		return pressId;
	}

	public void setPressId(Integer pressId) {
		this.pressId = pressId;
	}

	public Integer getVipTypeId() {
		return vipTypeId;
	}

	public void setVipTypeId(Integer vipTypeId) {
		this.vipTypeId = vipTypeId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	
}
