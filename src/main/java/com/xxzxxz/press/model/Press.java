package com.xxzxxz.press.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="press_info")
public class Press {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String pressName;
	private int officeId;
	private int frequencyId;
	private double dayPrice;
	private double monthPrice;
	private double sessonPrice;
	private double halfYearPrice;
	private double yearPrice;
	private double weekPrice;
	
	
	public int getOfficeId() {
		return officeId;
	}
	public double getWeekPrice() {
		return weekPrice;
	}
	public void setOfficeId(int officeId) {
		this.officeId = officeId;
	}
	public void setWeekPrice(double weekPrice) {
		this.weekPrice = weekPrice;
	}
	public double getHalfYearPrice() {
		return halfYearPrice;
	}
	public double getYearPrice() {
		return yearPrice;
	}
	public void setHalfYearPrice(double halfYearPrice) {
		this.halfYearPrice = halfYearPrice;
	}
	public void setYearPrice(double yearPrice) {
		this.yearPrice = yearPrice;
	}
	public int getId() {
		return id;
	}
	public String getPressName() {
		return pressName;
	}
	public int getFrequencyId() {
		return frequencyId;
	}
	public double getDayPrice() {
		return dayPrice;
	}
	public double getMonthPrice() {
		return monthPrice;
	}
	public double getSessionPrice() {
		return sessonPrice;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setPressName(String pressName) {
		this.pressName = pressName;
	}
	public void setFrequencyId(int frequencyId) {
		this.frequencyId = frequencyId;
	}
	public void setDayPrice(double dayPrice) {
		this.dayPrice = dayPrice;
	}
	public void setMonthPrice(double monthPrice) {
		this.monthPrice = monthPrice;
	}
	public void setSessonPrice(double sessonPrice) {
		this.sessonPrice = sessonPrice;
	}
	
	
}
