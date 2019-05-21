package com.xxzxxz.press.model;

import javax.persistence.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@Table(name="consumer")
public class Consumer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false,unique=true)
    private String name;
    private String address;
    private String phone;
    private int level;
    private int age;
    private int sex;
    private String language;
    private String province;
    private String occupation;
    private String degree;

    public Integer getZenkaId() {
        return zenkaId;
    }

    public void setZenkaId(Integer zenkaId) {
        this.zenkaId = zenkaId;
    }

    private Integer zenkaId;

    public Consumer(){}

    public Consumer(String name,String address,String phone,int level,int age,int sex,String language,String province,String occupation,String degree){
        this.name=name;
        this.address=address;
        this.phone=phone;
        this.level=level;
        this.age=age;
        this.sex=sex;
        this.language=language;
        this.province=province;
        this.occupation=occupation;
        this.degree=degree;
    }

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
}
