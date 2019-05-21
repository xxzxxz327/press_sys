package com.xxzxxz.press.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class PressVipDuration implements Serializable{
	private int pressId;
	private int vipTypeId;
	private int duration;
	
	public PressVipDuration() {
		
	}
	
	public PressVipDuration(int pressId, int vipTypeId, int duration) {
		this.pressId = pressId;
		this.vipTypeId = vipTypeId;
		this.duration = duration;
	}

	public int getPressId() {
		return pressId;
	}

	public void setPressId(int pressId) {
		this.pressId = pressId;
	}

	public int getVipTypeId() {
		return vipTypeId;
	}

	public void setVipTypeId(int vipTypeId) {
		this.vipTypeId = vipTypeId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}