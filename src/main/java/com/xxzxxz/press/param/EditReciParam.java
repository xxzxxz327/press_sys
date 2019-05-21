package com.xxzxxz.press.param;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class EditReciParam {

	@NotNull(message="分站ID不能为空")
	private Integer stationId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull(message="时间不能为空")
	private Date time;
	
	private Integer NeedNum;
	
	private Integer RealNum;

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getNeedNum() {
		return NeedNum;
	}

	public void setNeedNum(Integer needNum) {
		NeedNum = needNum;
	}

	public Integer getRealNum() {
		return RealNum;
	}

	public void setRealNum(Integer realNum) {
		RealNum = realNum;
	}
	
	
}
