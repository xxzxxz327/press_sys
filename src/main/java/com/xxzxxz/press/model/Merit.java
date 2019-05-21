package com.xxzxxz.press.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="merit_pay")
public class Merit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "base")
    private Integer base;

    @Column(name = "tichen")
    private Double tichen;

    @Column(name = "bonus")
    private Integer bonus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getBase() {
        return base;
    }

    public void setBase(Integer base) {
        this.base = base;
    }

    public Double getTichen() {
        return tichen;
    }

    public void setTichen(Double tichen) {
        this.tichen = tichen;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }
}
