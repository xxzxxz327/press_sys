package com.xxzxxz.press.model;


import javax.persistence.*;


@Entity
@Table(name = "route_group")
public class RouteGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer startId;
  private Integer areaId;
  private Integer principalId;

  public Integer getPrincipalId() {
    return principalId;
  }

  public void setPrincipalId(Integer principalId) {
    this.principalId = principalId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getStartId() {
    return startId;
  }

  public void setStartId(Integer startId) {
    this.startId = startId;
  }

  public Integer getAreaId() {
    return areaId;
  }

  public void setAreaId(Integer areaId) {
    this.areaId = areaId;
  }
}
