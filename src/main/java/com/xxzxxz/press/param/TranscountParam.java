package com.xxzxxz.press.param;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TranscountParam {
    private int id;
    private  int base;
    private double jiazhang;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public double getJiazhang() {
        return jiazhang;
    }

    public void setJiazhang(double jiazhang) {
        this.jiazhang = jiazhang;
    }
}
