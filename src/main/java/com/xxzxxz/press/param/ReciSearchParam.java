package com.xxzxxz.press.param;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;



public class ReciSearchParam {
	
	@NotNull(message="分站ID不能为空")
	private Integer StationId;
	
	@NotNull(message="时间不能为空")
	@DateTimeFormat(pattern="yyyy-MM")
	private Date TimeMonth;

	public Integer getStationId() {
		return StationId;
	}

	public void setStationId(Integer stationId) {
		StationId = stationId;
	}

	public Date getTimeMonth() {
		return TimeMonth;
	}

	public void setTimeMonth(Date timeMonth) {
		TimeMonth = timeMonth;
	}
	
}
