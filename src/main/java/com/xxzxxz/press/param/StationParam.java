package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class StationParam {
	private Integer Id;
	@NotEmpty(message="分站名称不能为空")
	private String stationName;
	@NotNull(message="地区ID不能为空")
	private Integer areaId;
	private Integer principalId;
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public Integer getAreaId() {
		return areaId;
	}
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}
	public Integer getPrincipalId() {
		return principalId;
	}
	public void setPrincipalId(Integer principalId) {
		this.principalId = principalId;
	}
	
	
	
	
	

}
