package com.xxzxxz.press.param;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class TotalReciParam {
	
	//@NotNull(message="分站ID不能为空")
	private Integer stationId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	//@NotNull(message="时间不能为空")
	private Date time;

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

}
