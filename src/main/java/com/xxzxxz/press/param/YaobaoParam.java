package com.xxzxxz.press.param;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class YaobaoParam {
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull(message="时间不能为空")
	private Date time;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	

}
