package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class InfoParam {

	private Integer Id;
	
	@NotEmpty(message="公司名称不能为空")
	private String name;
	

	@NotEmpty(message="公司地址不能为空")
	private String address;
	
	@NotEmpty(message="公司法人不能为空")
	private String principal;
	
	@NotEmpty(message="公司邮箱不能为空")
	@Pattern(regexp= "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",message="邮箱格式不正确，请输入正确的邮箱")
	private String mailaddress;
	
	@NotEmpty(message="联系电话不能为空")
	@Pattern(regexp="(^[1][3,4,5,7,8][0-9]{9}$)|(^[0][1-9]{2,3}-[0-9]{5,10}$)|(^[1-9]{1}[0-9]{5,8}$)",message="联系电话格式不正确，请输入正确的联系电话")
	private String phoneNumber;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getMailaddress() {
		return mailaddress;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
