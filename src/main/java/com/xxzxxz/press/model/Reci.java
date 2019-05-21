package com.xxzxxz.press.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="reci_num")
@IdClass(ReciKey.class)
public class Reci {
	
	@Id
	@Column(name="station_id")
	private Integer stationId;
	
	
	@Id
	@Column(name="date")
	private Date date;
	
	@Column(name="need_num")
	private Integer needNum;
	
	public Integer getNeedNum() {
		return needNum;
	}


	public void setNeedNum(Integer needNum) {
		this.needNum = needNum;
	}


	public Integer getRealNum() {
		return realNum;
	}


	public void setRealNum(Integer realNum) {
		this.realNum = realNum;
	}


	@Column(name="real_num")
	private Integer realNum;


	public Integer getStationId() {
		return stationId;
	}


	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}
	
	
	

}
