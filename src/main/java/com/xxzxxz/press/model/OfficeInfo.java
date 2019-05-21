package com.xxzxxz.press.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "office_info", schema = "test", catalog = "")
public class OfficeInfo {
    private int id;
    private String officeName;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "office_name")
    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfficeInfo that = (OfficeInfo) o;
        return id == that.id &&
                Objects.equals(officeName, that.officeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, officeName);
    }
}
