package com.xxzxxz.press.repository;

import java.util.Date;
import java.util.List;

import com.xxzxxz.press.model.Merit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xxzxxz.press.model.Staff;

public interface MeritRepository extends JpaRepository<Merit,Integer>{
	@Query
			(nativeQuery=true,countQuery = "SELECT count(staff.id)"
					+" from (((staff left join  duty on staff.duty_id=duty.id) left join merit_pay on duty.merit_id=merit_pay.id) left join department on staff.depart_id=department.id)left join "
					+ "(select solicit_id,count(id)as sum1 "
					+ " from orders"
					+ " where orders.pay_date BETWEEN ?2 AND ?3"
					+ " GROUP by solicit_id) as a on a.solicit_id=staff.id"
					+" WHERE department.station_id=?1 and duty.duty_name='征订员'"
					,
					value="SELECT staff.id,staff.staff_name,duty.duty_name,a.sum1,(merit_pay.base+merit_pay.tichen*(CASE WHEN a.sum1 is NULL THEN 0 ELSE a.sum2 END)) as meritsum"
							+" from (((staff left join  duty on staff.duty_id=duty.id) left join merit_pay on duty.merit_id=merit_pay.id) left join department on staff.depart_id=department.id)left join "
							+ "(select solicit_id,count(id)as sum1,sum(price) as sum2"
							+ " from orders"
							+ " where orders.pay_date BETWEEN ?2 AND ?3"
							+ " GROUP by solicit_id) as a on a.solicit_id=staff.id"
							+" WHERE department.station_id=?1 and duty.duty_name='征订员'")
	Page<Object[]> findDingMerit(int sId,Date startTime,Date endTime,Pageable pageable);

	@Query
			(nativeQuery=true,
					countQuery="SELECT count(staff.id)"
							+" FROM staff LEFT JOIN department ON staff.depart_id=department.id"
							+" WHERE department.station_id=?1 and staff.duty_id=1",
					value="SELECT staff.id,staff.staff_name,duty.duty_name,b.sum2,(merit_pay.base+(CASE WHEN sum2 is NULL THEN 0 ELSE sum2 END)*merit_pay.tichen+merit_pay.bonus) AS merit"
							+" from(((staff LEFT JOIN "
							+ "(SELECT deliver_part.principal_id,count( deliver_part.principal_id ) AS sum2 "
							+ "FROM deliver LEFT JOIN deliver_part ON deliver.deliverpart_id = deliver_part.id "
							+ "WHERE deliver.finish_date BETWEEN ?2 AND ?3 GROUP BY deliver_part.principal_id) "
							+ "AS b ON b.principal_id = staff.id )"
							+ "LEFT JOIN duty ON duty.id = staff.duty_id )"
							+ "LEFT JOIN department ON staff.depart_id = department.id )"
							+ "LEFT JOIN merit_pay ON merit_pay.id = duty.merit_id "
							+" WHERE department.station_id=?1 and duty.duty_name='投递员' ")
	Page<Object[]> findSendMerit(int sId,Date startTime,Date endTime,Pageable pageable);

	List<Merit> findAll();




}
