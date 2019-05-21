package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class QuestionitemParam {
	
	
	private Integer id;
	
	private Integer questionId ;
	
	
	@NotNull(message="选项编号不能为空")
	private Integer orderid;
	
	
	@NotEmpty(message="选项名称不能为空")
	private String name;


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

	
	

	
	

}
