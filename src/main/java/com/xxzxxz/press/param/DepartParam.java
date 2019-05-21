package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;

public class DepartParam {
	private Integer Id;
	@NotEmpty(message="部门名称不能为空")
	private String departName;
	private Integer principalId;
	private Integer stationId;
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public Integer getPrincipalId() {
		return principalId;
	}
	public void setPrincipalId(Integer principalId) {
		this.principalId = principalId;
	}
	public Integer getStationId() {
		return stationId;
	}
	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}
	
	
	

	
	
	

}
