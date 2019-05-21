package com.xxzxxz.press.model;


import javax.persistence.*;
import java.awt.image.DataBufferInt;
import java.util.Date;

@Entity
@Table(name = "print_send")
public class PrintSend {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer printId;
  private Integer printNum;
  private Integer sendId;
  private Integer needNum;
  private Date date;

  public Integer getPrintNum() {
    return printNum;
  }

  public void setPrintNum(Integer printNum) {
    this.printNum = printNum;
  }

  public Integer getNeedNum() {
    return needNum;
  }

  public void setNeedNum(Integer needNum) {
    this.needNum = needNum;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

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
}
