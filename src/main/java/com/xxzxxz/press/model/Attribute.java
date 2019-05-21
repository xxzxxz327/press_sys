package com.xxzxxz.press.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "attribute")
public class Attribute {

	@Id
	private Integer pressId;

	private Integer unsub;
	private Integer changeable;


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

	public Integer getChange() {
		return changeable;
	}

	public void setChange(Integer change) {
		this.changeable = change;
	}
}
