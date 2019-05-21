package com.xxzxxz.press.model;


import javax.persistence.*;


@Entity
@Table(name = "route")
public class Route {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer printId;
  private Integer sendId;
  private Integer principalId;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getPrintId() {
    return printId;
  }

  public void setPrintId(Integer printId) {
    this.printId = printId;
  }

  public Integer getSendId() {
    return sendId;
  }

  public void setSendId(Integer sendId) {
    this.sendId = sendId;
  }

  public Integer getPrincipalId() {
    return principalId;
  }

  public void setPrincipalId(Integer principalId) {
    this.principalId = principalId;
  }
}
