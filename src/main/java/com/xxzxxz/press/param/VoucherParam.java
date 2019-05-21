package com.xxzxxz.press.param;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class VoucherParam {
	
	
	private Integer Id;
	
	@NotNull(message="代金券金额不能为空")
	private Integer sum;

	private Integer flag;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
/*
	@NotNull(message="代金券数量不能为空")
	private Integer num;
	*/
	

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

/*
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
*/
	public Integer getSum() {
		return sum;
	}

	public void setSum(Integer sum) {
		this.sum = sum;
	}

	
	

	
	

}
