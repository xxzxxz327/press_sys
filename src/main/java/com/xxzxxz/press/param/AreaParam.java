package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;

public class AreaParam {
	
	private Integer Id;
	
	@NotEmpty(message="地区名称不能为空")
	private String Name;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
	

}
