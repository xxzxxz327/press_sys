package com.xxzxxz.press.model;

import com.xxzxxz.press.param.PraiseParam;

import javax.persistence.*;

@Entity
@Table(name = "praise")
public class Praise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;
    private Integer consumerId;
    private Integer departId;
    private Integer status;
    private Integer stationId;

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Integer consumerId) {
        this.consumerId = consumerId;
    }

    public Integer getDepartId() {
        return departId;
    }

    public void setDepartId(Integer departId) {
        this.departId = departId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setByParam(PraiseParam pp){

        if(pp.getId()!=null){this.id=pp.getId();}
        if(pp.getConsumerId()!=null){this.consumerId=pp.getConsumerId();}
        if(pp.getDepartId()!=null){this.departId=pp.getDepartId();}
        if(pp.getContent()!=null){this.content=pp.getContent();}
        if(pp.getStatus()!=null){this.status=pp.getStatus();}
        if(pp.getOrderId()!=null){this.orderId=pp.getOrderId();}

    }

}
