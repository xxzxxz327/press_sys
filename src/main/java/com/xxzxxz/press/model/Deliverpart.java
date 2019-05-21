package com.xxzxxz.press.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="deliver_part")
public class Deliverpart {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private int id;
    
    private int areaId;
    private int principalId;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getareaId() {
        return areaId;
    }

    public void setareaId(int areaId) {
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

    public Deliverpart() {
    }

    public Deliverpart (int areaId,int principalId,String address){
        this.areaId=areaId;
        this.principalId=principalId;
        this.address=address;
    }
}
