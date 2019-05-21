package com.xxzxxz.press.model.vo;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ComplainResult {
    @Id
    @GeneratedValue
    private int id;
    private String type;
    private Integer count;
}
