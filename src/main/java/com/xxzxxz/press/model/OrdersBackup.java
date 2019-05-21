package com.xxzxxz.press.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="orders_backup")
public class OrdersBackup {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatusNow() {
        return statusNow;
    }

    public void setStatusNow(int statusNow) {
        this.statusNow = statusNow;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Id
    private int id;
    private int duration;
    private String address;
    private Date endDate;
    private int status;
    private int statusNow;
    double price;
}
