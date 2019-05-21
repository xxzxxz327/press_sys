package com.xxzxxz.press.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xxzxxz.press.param.AdviceParam;

@Entity
@Table(name="ask")
public class Ask {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@Column(name="name")
	private String name;
	
	
	@Column(name="orderid")
	private Integer orderid;


	@Column(name="writenum")
	private Integer writenum;
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Integer getOrderid() {
		return orderid;
	}


	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}


	public Integer getWritenum() {
		return writenum;
	}


	public void setWritenum(Integer writenum) {
		this.writenum = writenum;
	}
	

}
