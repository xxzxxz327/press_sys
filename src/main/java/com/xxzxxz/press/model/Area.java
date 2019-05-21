package com.xxzxxz.press.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "area_name")
    private String areaName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area area = (Area) o;
        return id == area.id &&
                Objects.equals(areaName, area.areaName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, areaName);
    }
}
