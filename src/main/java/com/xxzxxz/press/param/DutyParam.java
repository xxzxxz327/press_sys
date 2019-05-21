package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;

public class DutyParam {
	
	private Integer dutyId;
	
	@NotEmpty(message="职务名称不能为空")
	private String dutyName;
	

	private Integer dutyMeritId;
	
	
	private Integer dutyDepartId;


	


	public String getDutyName() {
		return dutyName;
	}


	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}


	public Integer getDutyMeritId() {
		return dutyMeritId;
	}


	public void setDutyMeritId(Integer dutyMeritId) {
		this.dutyMeritId = dutyMeritId;
	}


	public Integer getDutyDepartId() {
		return dutyDepartId;
	}


	public void setDutyDepartId(Integer dutyDepartId) {
		this.dutyDepartId = dutyDepartId;
	}


	public Integer getDutyId() {
		return dutyId;
	}


	public void setDutyId(Integer dutyId) {
		this.dutyId = dutyId;
	}
	
	
	
	
	

}
