package com.xxzxxz.press.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;



@Entity
@Table(name="discounts")
@IdClass(PressVipDuration.class)
public class Discount implements Serializable{
	
	@Id
	@Column(name="press_id", nullable=false)
	private Integer pressId;
	
	@Id
	@Column(name="vip_type_id", nullable=false)
	private Integer vipTypeId;
	
	@Id
	@Column(name="duration", nullable=false)
	private Integer duration;
	
	@Column(name="strategy")
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

	public Double getStrategy() {
		return strategy;
	}

	public void setStrategy(Double strategy) {
		this.strategy = strategy;
	}

	
	
}
