package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class QuestionParam {
	
	
	private Integer id;
	
	private Integer askId ;
	
	
	@NotNull(message="问题编号不能为空")
	private Integer orderid;
	
	@NotNull(message="问题类型不能为空,0为单选,其它为多选")
	private Integer type;
	
	@NotEmpty(message="问题名称不能为空")
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAskId() {
		return askId;
	}

	public void setAskId(Integer askId) {
		this.askId = askId;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	
	

}
