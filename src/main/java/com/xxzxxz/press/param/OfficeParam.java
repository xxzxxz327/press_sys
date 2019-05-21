package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;

public class OfficeParam {

	private Integer Id;
	
	@NotEmpty(message="报社名称不能为空")
	private String officeName;



	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	
}
