package com.xxzxxz.press.param;

import javax.validation.constraints.NotNull;

public class AttributeParam {

	private Integer pressId;
	private Integer unsub;



	private Integer changeable;
	public Integer getChangeable() {
		return changeable;
	}

	public void setChangeable(Integer change) {
		this.changeable = change;
	}

	public Integer getPressId() {
		return pressId;
	}

	public void setPressId(Integer pressId) {
		this.pressId = pressId;
	}

	public Integer getUnsub() {
		return unsub;
	}

	public void setUnsub(Integer unsub) {
		this.unsub = unsub;
	}

}
