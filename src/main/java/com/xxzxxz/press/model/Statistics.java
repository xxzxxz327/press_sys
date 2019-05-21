package com.xxzxxz.press.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Statistics {
    @Id
    private int stationId;
    private String stationName;
    private Integer id;
    private Integer consumerId;
    private Integer duration;
    private Integer copies;
    private Date startDate;
    private Date endDate;
    private Double price;
    private Double priceLonger;
    private Integer status;
    private String unsubReason;

    @Basic
    @Column(name = "station_id")
    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    @Basic
    @Column(name = "station_name")
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Basic
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "consumer_id")
    public Integer getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Integer consumerId) {
        this.consumerId = consumerId;
    }

    @Basic
    @Column(name = "duration")
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Basic
    @Column(name = "copies")
    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    @Basic
    @Column(name = "start_date")
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Basic
    @Column(name = "end_date")
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Basic
    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "price_longer")
    public Double getPriceLonger() {
        return priceLonger;
    }

    public void setPriceLonger(Double priceLonger) {
        this.priceLonger = priceLonger;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "unsub_reason")
    public String getUnsubReason() {
        return unsubReason;
    }

    public void setUnsubReason(String unsubReason) {
        this.unsubReason = unsubReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistics that = (Statistics) o;
        return stationId == that.stationId &&
                Objects.equals(stationName, that.stationName) &&
                Objects.equals(id, that.id) &&
                Objects.equals(consumerId, that.consumerId) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(copies, that.copies) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(price, that.price) &&
                Objects.equals(priceLonger, that.priceLonger) &&
                Objects.equals(status, that.status) &&
                Objects.equals(unsubReason, that.unsubReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId, stationName, id, consumerId, duration, copies, startDate, endDate, price, priceLonger, status, unsubReason);
    }
}
