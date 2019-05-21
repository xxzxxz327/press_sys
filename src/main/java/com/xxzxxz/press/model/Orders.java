package com.xxzxxz.press.model;

import com.xxzxxz.press.param.OrdersParam;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name="orders")
public class Orders {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Integer consumerId) {
        this.consumerId = consumerId;
    }

    public Integer getPressId() {
        return pressId;
    }

    public void setPressId(Integer pressId) {
        this.pressId = pressId;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getZenkaId() {
        return zenkaId;
    }

    public void setZenkaId(Integer zenkaId) {
        this.zenkaId = zenkaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Integer getPrintId() {
        return printId;
    }

    public void setPrintId(Integer printId) {
        this.printId = printId;
    }

    public Date getXiaTime() {
        return xiaTime;
    }

    public void setXiaTime(Date xiaTime) {
        this.xiaTime = xiaTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer consumerId;
    @Column(nullable = false)
    private Integer pressId;
    private Integer copies;
    private Integer duration;
    private Integer payType;
    @Column(nullable = false)
    private Integer stationId;
    private Integer zenkaId;
    private String address;
    private Date startDate;
    private Date endDate;
    private Integer status;
    private Integer orderType;
    private Double priceLonger;
    private Integer solicitId;
    private Integer voucherId;
    private Integer unsub;
    private Integer changeable;


    public Integer getUnsub() {
        return unsub;
    }

    public void setUnsub(Integer unsub) {
        this.unsub = unsub;
    }

    public Integer getChange() {
        return changeable;
    }

    public void setChange(Integer change) {
        this.changeable = change;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getSolicitId() {
        return solicitId;
    }

    public void setSolicitId(Integer solicitId) {
        this.solicitId = solicitId;
    }

    public Integer getUnsubReason() {
        return unsubReason;
    }

    public void setUnsubReason(Integer unsubReason) {
        this.unsubReason = unsubReason;
    }

    private Integer unsubReason;
    private Date postponeStart;
    private Date postponeEnd;


    public Double getPriceLonger() {
        return priceLonger;
    }

    public void setPriceLonger(Double priceLonger) {
        this.priceLonger = priceLonger;
    }

    public Date getPostponeStart() {
        return postponeStart;
    }

    public void setPostponeStart(Date postponeStart) {
        this.postponeStart = postponeStart;
    }

    public Date getPostponeEnd() {
        return postponeEnd;
    }

    public void setPostponeEnd(Date postponeEnd) {
        this.postponeEnd = postponeEnd;
    }



    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    private Date payDate;

    public Integer getOrderType() {
        return orderType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    Double price;
    Integer printId;
    private Date xiaTime;
    public String getStartDateString() {

        if (this.startDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(this.startDate);
        }
        return "";
    }

    public String getEndDateString() {

        if (this.endDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(this.endDate);
        }
        return "";
    }

    public String getXiaTimeString(){

        if(this.xiaTime!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(this.xiaTime);
        }
        return "";
    }

    public String getNullString(){
        return "";
    }

    public void setByParam(OrdersParam oP){
        this.setId(oP.getId());
        if(oP.getConsumerId()!=null){this.setConsumerId(oP.getConsumerId());}
        if(oP.getPressId()!=null){this.setPressId(oP.getPressId());}
        if(oP.getCopies()!=null){this.setCopies(oP.getCopies());}
        if(oP.getDuration()!=null){this.setDuration(oP.getDuration());}
        if(oP.getPayType()!=null){this.setPayType(oP.getPayType());}
        if(oP.getStationId()!=null){this.setStationId(oP.getStationId());}
        if(oP.getZenkaId()!=null){this.setZenkaId(oP.getZenkaId());}
        this.setAddress(oP.getAddress());
        if(oP.getStartDate()!=null){this.setStartDate(oP.getStartDate());}
        if(oP.getEndDate()!=null){this.setEndDate(oP.getEndDate());}
        if(oP.getStatus()!=null){this.setStatus(oP.getStatus());}
        if(oP.getPrice()!=null){this.setPrice(oP.getPrice());}
        if(oP.getPrintId()!=null){this.setPrintId(oP.getPrintId());}
        if(oP.getXiaTime()!=null){this.setXiaTime(oP.getXiaTime());}

    }


}
