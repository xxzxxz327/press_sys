package com.xxzxxz.press.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Duty {
    private int id;
    private Integer departId;
    private String dutyName;
    private Integer meritId;

    public Integer getMeritId() {
        return meritId;
    }

    public void setMeritId(Integer meritId) {
        this.meritId = meritId;
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "depart_id")
    public Integer getDepartId() {
        return departId;
    }

    public void setDepartId(Integer departId) {
        this.departId = departId;
    }

    @Basic
    @Column(name = "duty_name")
    public String getDutyName() {
        return dutyName;
    }

    public void setDutyName(String dutyName) {
        this.dutyName = dutyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Duty duty = (Duty) o;
        return id == duty.id &&
                Objects.equals(departId, duty.departId) &&
                Objects.equals(dutyName, duty.dutyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departId, dutyName);
    }
}
