package com.xxzxxz.press.param;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class MonthSearchParam {

	@NotNull(message="时间不能为空")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startTime;
	
	@NotNull(message="时间不能为空")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endTime;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
