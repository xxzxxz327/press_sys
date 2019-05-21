package com.xxzxxz.press.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xxzxxz.press.model.Discount;
import com.xxzxxz.press.model.PressVipDuration;

public interface DiscountRepository extends JpaRepository<Discount,PressVipDuration> {

	@Query(value="SELECT d.vip_type_id, p.press_name, d.duration, d.strategy, d.press_id FROM discounts as d LEFT JOIN press_info as p ON d.press_id=p.id where d.press_id=?1", countQuery="select count(d.press_id) FROM discounts as d LEFT JOIN press_info as p ON d.press_id=p.id where d.press_id=?1", nativeQuery=true)
	Page<Object[]> findDisPNbyPID(int pressId,Pageable pageable);
	//findDisPNbyDURA findDisPNbyPID
	@Query(value="SELECT d.vip_type_id, p.press_name, d.duration, d.strategy, d.press_id FROM discounts as d LEFT JOIN press_info as p ON d.press_id=p.id where d.vip_type_id=?1", countQuery="select count(d.press_id) FROM discounts as d LEFT JOIN press_info as p ON d.press_id=p.id where d.vip_type_id=?1", nativeQuery=true)
	Page<Object[]> findDisPNbyVTID(int vipTypeId,Pageable pageable);
	
	@Query(value="SELECT d.vip_type_id, p.press_name, d.duration, d.strategy, d.press_id FROM discounts as d LEFT JOIN press_info as p ON d.press_id=p.id where d.duration=?1", countQuery="select count(d.press_id) FROM discounts as d LEFT JOIN press_info as p ON d.press_id=p.id where d.duration=?1", nativeQuery=true)
	Page<Object[]> findDisPNbyDURA(int duration,Pageable pageable);
	
	@Query(value="SELECT d.vip_type_id, p.press_name, d.duration, d.strategy, d.press_id FROM discounts as d LEFT JOIN press_info as p ON d.press_id=p.id", countQuery="select count(d.press_id) FROM discounts as d LEFT JOIN press_info as p ON d.press_id=p.id ", nativeQuery=true)
	Page<Object[]> find_dis_pressN_all(Pageable pageable);
	
	@Query(value="select * from discounts as d where d.press_id=?1 and d.vip_type_id=?2 and d.duration=?3", nativeQuery=true)
	Discount find_one(int pressId, int vipTypeId, int duration);

	Discount findByPressId(int id);
	
}
