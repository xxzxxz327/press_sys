package com.xxzxxz.press.model;

import javax.persistence.*;

@Entity
@Table(name = "press_info")
public class PressInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String pressName;
    private Integer officeId;
    private Integer frequencyId;
    private Double dayPrice;
    private Double monthPrice;
    private Double sessionPrice;
    private Double halfYearPrice;
    private Double yearPrice;
    private Double weekPrice;

    public Double getWeekPrice() {
        return weekPrice;
    }

    public void setWeekPrice(Double weekPrice) {
        this.weekPrice = weekPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPressName() {
        return pressName;
    }

    public void setPressName(String pressName) {
        this.pressName = pressName;
    }

    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public Integer getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(Integer frequencyId) {
        this.frequencyId = frequencyId;
    }

    public Double getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(Double dayPrice) {
        this.dayPrice = dayPrice;
    }

    public Double getMonthPrice() {
        return monthPrice;
    }

    public void setMonthPrice(Double monthPrice) {
        this.monthPrice = monthPrice;
    }

    public Double getSessionPrice() {
        return sessionPrice;
    }

    public void setSessionPrice(Double sessionPrice) {
        this.sessionPrice = sessionPrice;
    }

    public Double getHalfYearPrice() {
        return halfYearPrice;
    }

    public void setHalfYearPrice(Double halfYearPrice) {
        this.halfYearPrice = halfYearPrice;
    }

    public Double getYearPrice() {
        return yearPrice;
    }

    public void setYearPrice(Double yearPrice) {
        this.yearPrice = yearPrice;
    }
}
