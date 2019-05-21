package com.xxzxxz.press.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import java.util.Date;

@Entity
@Table(name="trans_count")
public class transcount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    @Column(name="base")
    private Integer base ;
    @Column(name="jiazhang")
    private Double jiazhang ;

    public Integer getBase() {
        return base;
    }

    public void setBase(Integer base) {
        this.base = base;
    }

    public Double getJiazhang() {
        return jiazhang;
    }

    public void setJiazhang(Double jiazhang) {
        this.jiazhang = jiazhang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
