package com.xxzxxz.press.model;

import com.xxzxxz.press.param.AdviceParam;

import javax.persistence.*;

@Entity
@Table(name = "advice")
public class Advice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String content;
    private Integer consumerId;
    private Integer departId;
    private Integer status;
    private Integer stationId;

    public void setByParam(AdviceParam ap){

        if(ap.getId()!=null){this.id=ap.getId();}
        if(ap.getConsumerId()!=null){this.consumerId=ap.getConsumerId();}
        if(ap.getDepartId()!=null){this.departId=ap.getDepartId();}
        if(ap.getContent()!=null){this.content=ap.getContent();}
        if(ap.getStatus()!=null){this.status=ap.getStatus();}
        if(ap.getOrderId()!=null){this.orderId=ap.getOrderId();}

    }

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

}
