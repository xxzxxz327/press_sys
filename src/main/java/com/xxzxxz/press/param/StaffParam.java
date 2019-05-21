package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;

public class StaffParam {
	private int id;
	
	@NotEmpty(message="姓名不能为空")
	private String staffName;
	private int dutyId;
	private Integer authority;
	private int departId;
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
	public int getDepartId() {
		return departId;
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
	public void setDepartId(int departId) {
		this.departId = departId;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
