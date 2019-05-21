package com.xxzxxz.press.param;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class ReciAddParam {
	

	
	@NotNull(message="分站ID不能为空")
	private Integer stationId;
	
	@NotNull(message="时间不能为空")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date time;

	@NotNull(message = "需要数量不能为空")
	private Integer needNum;
	

	public int getNeedNum() {
		return needNum;
	}

	public void setNeedNum(Integer needNum) {
		this.needNum = needNum;
	}

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
