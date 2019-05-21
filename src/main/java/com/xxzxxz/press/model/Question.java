package com.xxzxxz.press.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xxzxxz.press.param.AdviceParam;
import com.xxzxxz.press.param.QuestionParam;

@Entity
@Table(name="question")
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="ask_id")
	private Integer askId;
	
	@Column(name="name")
	private String name;
	
	
	@Column(name="type")
	private Integer type;
	
	
	public Integer getAskId() {
		return askId;
	}


	public void setAskId(Integer askId) {
		this.askId = askId;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	@Column(name="orderid")
	private Integer orderid;


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
	
	public void setByParam(QuestionParam QP){
		
		if(QP.getId()!=null){this.id=QP.getId();}
		if(QP.getAskId()!=null){this.askId=QP.getAskId();}
		if(QP.getName()!=null){this.name=QP.getName();}
		if(QP.getOrderid()!=null){this.orderid=QP.getOrderid();}
		if(QP.getType()!=null){this.type=QP.getType();}
	}
	

}
