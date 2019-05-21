package com.xxzxxz.press.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="repay")
public class Repay {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private int id;
	
	@Column(nullable = false)
	private int amount;
	@Column(nullable = false)
	private int consumerId;
	
	public Repay(){}
	public Repay(int amount,int consumerId){
		this.amount = amount;
		this.consumerId = consumerId;
	}
	public int getId() {
		return id;
	}
	public int getAmount() {
		return amount;
	}
	public int getConsumerId() {
		return consumerId;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setConsumerId(int consumerId) {
		this.consumerId = consumerId;
	}
}
