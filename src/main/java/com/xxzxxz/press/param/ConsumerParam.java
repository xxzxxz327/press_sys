package com.xxzxxz.press.param;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

public class ConsumerParam {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    private int id;
    @NotEmpty(message = "姓名不能为空")
    private String name;
    private String address;
    @NotEmpty(message = "电话不能为空")
    @Pattern(regexp="(^[1][3,4,5,7,8][0-9]{9}$)|(^[0][1-9]{2,3}-[0-9]{5,10}$)|(^[1-9]{1}[0-9]{5,8}$)",message="联系电话格式不正确，请输入正确的联系电话")
    private String phone;
    @Min(value = 0,message = "等级不能为空")
    private int level;
    @Max(value=100,message = "年龄不能大于100")
    @Min(value=18,message = "年龄不能小于18")
    private int age;
    @Min(value = 0,message = "性别不能为空")
    private int sex;
    private String language;
    private String province;
    private String occupation;
    private String degree;
    private Integer zenkaId;

    public Integer getZenkaId() {
        return zenkaId;
    }

    public void setZenkaId(Integer zenkaId) {
        this.zenkaId = zenkaId;
    }
}
