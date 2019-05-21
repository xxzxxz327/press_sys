package com.xxzxxz.press.param;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MeritParam {
	
	private Integer Id;
	
	@NotNull(message="基础值不能为空")
	private Integer base;

	@NotNull(message="提成不能为空")
	private Double tichen;

	@NotNull(message="奖金不能为空")
	private Integer bonus;


	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
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
