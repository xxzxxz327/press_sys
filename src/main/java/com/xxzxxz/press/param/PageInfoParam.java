package com.xxzxxz.press.param;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class PageInfoParam {
    private int id;
    private int pageNum;
    private String type;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
