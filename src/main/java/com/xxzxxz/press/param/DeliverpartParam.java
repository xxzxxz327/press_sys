package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DeliverpartParam {
	private int id;
    @NotNull(message = "地区编号不能为空")
    private int areaId;
    @NotNull(message = "负责人编号不能为空")
    private int principalId;
    @NotEmpty(message = "投递段地址不能为空")
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAreaId() {
        return areaId;
    }
    public int getareaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(int principalId) {
        this.principalId = principalId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
