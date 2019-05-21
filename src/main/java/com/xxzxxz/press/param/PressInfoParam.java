package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;

public class PressInfoParam {
    private int id;
    private String pressName;
    @NotEmpty(message = "报社id不能为空")
    private int officeId;
    private int frequencyId;
    private double dayPrice;
    private double monthPrice;
    private double sessionPrice;
    private double halfYearPrice;
    private double yearPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPressName() {
        return pressName;
    }

    public void setPressName(String pressName) {
        this.pressName = pressName;
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public int getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(int frequencyId) {
        this.frequencyId = frequencyId;
    }

    public double getDayPrice() {
        return dayPrice;
    }

    public void setDayPrice(double dayPrice) {
        this.dayPrice = dayPrice;
    }

    public double getMonthPrice() {
        return monthPrice;
    }

    public void setMonthPrice(double monthPrice) {
        this.monthPrice = monthPrice;
    }

    public double getSessionPrice() {
        return sessionPrice;
    }

    public void setSessionPrice(double sessionPrice) {
        this.sessionPrice = sessionPrice;
    }

    public double getHalfYearPrice() {
        return halfYearPrice;
    }

    public void setHalfYearPrice(double halfYearPrice) {
        this.halfYearPrice = halfYearPrice;
    }

    public double getYearPrice() {
        return yearPrice;
    }

    public void setYearPrice(double yearPrice) {
        this.yearPrice = yearPrice;
    }
}
