package com.xxzxxz.press.param;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class OrdersParam {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getXiaTime() {
        return xiaTime;
    }

    public void setXiaTime(Date xiaTime) {
        this.xiaTime = xiaTime;
    }

    private Integer id;
    @NotNull(message = "客户id不能为空")
    private Integer consumerId;
    @NotNull(message = "报刊id不能为空")
    private Integer pressId;
    @NotNull(message = "份数不能为空")
    private Integer copies;
    @NotNull(message = "订购时长不能为空")
    private Integer duration;
    private Integer payType;
    @NotNull(message = "所属站点不能为空")
    private Integer stationId;
    private Integer zenkaId;
    @NotEmpty(message = "订单地址不能为空")
    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private Integer status;
    private Double price;
    private Double priceLonger;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date postponeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date postponeEnd;
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

    private Integer printId;

    public Integer getPrintId() {
        return printId;
    }

    public void setPrintId(Integer printId) {
        this.printId = printId;
    }

    private Integer unsubReason;

    public Integer getUnsubReason() {
        return unsubReason;
    }

    public void setUnsubReason(Integer unsubReason) {
        this.unsubReason = unsubReason;
    }

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



    public Integer getPrIntegerId() {
        return prIntegerId;
    }

    public void setPrIntegerId(Integer prIntegerId) {
        this.prIntegerId = prIntegerId;
    }

    private Integer prIntegerId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date xiaTime;
    private Integer orderType;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    private Integer voucherId;

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }
}
