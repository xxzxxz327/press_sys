package com.xxzxxz.press.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xxzxxz.press.param.AdviceParam;
import com.xxzxxz.press.param.QuestionitemParam;

@Entity
@Table(name="questionitem")
public class Questionitem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="question_id")
	private Integer questionId;
	
	@Column(name="orderid")
	private Integer orderid;
	
	@Column(name="name")
	private String name;
	
	@Column(name="result")
	private Integer result;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setByParam(QuestionitemParam QitemP){
		
		if(QitemP.getId()!=null){this.id=QitemP.getId();}
		if(QitemP.getName()!=null){this.name=QitemP.getName();}
		if(QitemP.getOrderid()!=null){this.orderid=QitemP.getOrderid();}
		if(QitemP.getQuestionId()!=null){this.questionId=QitemP.getQuestionId();}
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	
}
