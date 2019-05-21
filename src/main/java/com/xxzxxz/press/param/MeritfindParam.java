package com.xxzxxz.press.param;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class MeritfindParam {
	
	private int stationId;
	private String dutyName;
	@NotNull(message = "开始时间不能为空")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startTime;
	@NotNull(message = "结束时间不能为空")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endTime;
	public int getStationId() {
		return stationId;
	}
	public String getDutyName() {
		return dutyName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
