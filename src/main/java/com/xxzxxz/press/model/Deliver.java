package com.xxzxxz.press.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="deliver")
public class Deliver {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private int id;
	
	private Integer orderId;
	private Integer deliverpartId;
	private int priority;
	private int status;
	private Date finishDate;
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	public int getId() {
		return id;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public Integer getDeliverpartId() {
		return deliverpartId;
	}
	public int getPriority() {
		return priority;
	}
	public int getStatus() {
		return status;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public void setDeliverpartId(Integer deliverpartId) {
		this.deliverpartId = deliverpartId;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
