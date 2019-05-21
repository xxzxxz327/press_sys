package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;

public class FXStaffParam {
	private int id;
	
	@NotEmpty(message="姓名不能为空")
	private String staffName;
	private int dutyId;
	private Integer authority;
	private String phone;
	private String password;
	
	public int getId() {
		return id;
	}
	public String getStaffName() {
		return staffName;
	}
	public int getDutyId() {
		return dutyId;
	}
	public Integer getAuthority() {
		return authority;
	}
	public String getPhone() {
		return phone;
	}
	public String getPassword() {
		return password;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	public void setDutyId(int dutyId) {
		this.dutyId = dutyId;
	}
	public void setAuthority(Integer authority) {
		this.authority = authority;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
