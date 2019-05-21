package com.xxzxxz.press.param;


import java.util.Date;

public class PackParam {
    private int id;
    private int num;
    private Date regTime;

    public int getNum() {
        return num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }
}
