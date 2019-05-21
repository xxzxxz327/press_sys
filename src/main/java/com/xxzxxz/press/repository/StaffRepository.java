package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Reci;
import com.xxzxxz.press.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Date;
import java.util.List;

public interface StaffRepository extends JpaRepository<Staff,Integer>{
	Staff findById(int id);
	Staff findByStaffName(String staffName);
	Staff findByStaffNameAndPassword(String staffName, String password);

	@Query(value = "select depart_id from duty where id=?1",nativeQuery = true)
	int findDepartIdByDutyId(int id);

	@Query(value="select * from reci_num as r where r.station_id=?1 and year(r.date)=?2 and month(r.date)=?3 order by date asc",nativeQuery=true)
	Page<Reci> findByStationIdAndTime(int stationId,int year,int month,Pageable pageable);

	@Query(value="select * from reci_num as r where r.station_id=?1 and r.date=?2",nativeQuery=true)
	Page<Reci> findOneByStationIdAndTime(int stationId,String time,Pageable pageable);

	@Query(value="select * from reci_num as r where r.station_id=?1 ",nativeQuery=true)
	Page<Reci> findByStationId(int stationId, Pageable pageable);


	//员工姓名模糊输入
	@Query(nativeQuery=true,value="select staff.staff_name "
			+ "from staff left join department on staff.depart_id=department.id "
			+ "where department.station_id=?1")
	List<Object[]> showAllFXStaffName(int sid);

	@Query(nativeQuery=true,value="select staff.staff_name "
			+ "from (staff left join department on staff.depart_id=department.id)"
			+ "left join duty on duty.id=staff.duty_id "
			+ "where department.station_id=?1 and duty.duty_name='投递员'")
	List<Object[]> showAllFXStaffNameByDuty(int sid);


	//以下是editreci时使用
	@Query(value="select * from reci_num as r where r.station_id=?1 and r.date=?2",nativeQuery=true)
	Reci findRByStationIdAndTime(int stationId,String time);

	//以下是Total要报使用
	@Query(value="select * from reci_num as r where r.date=?1",nativeQuery=true)
	List<Reci> findYBByTime(String time);

	@Query(nativeQuery=true,
			value="SELECT staff.id,duty.duty_name,department.station_id,staff.staff_name "
					+ "FROM (staff LEFT JOIN department on department.id=staff.depart_id) Left join duty on duty.id=staff.duty_id")
	List<Object[]> showAll();

	@Query(value="select s.id,s.staff_name,duty.duty_name,d.depart_name,s.authority,s.phone from (staff as s left join department as d on s.depart_id=d.id) left join duty on duty.id=s.duty_id where s.id=?2 and d.station_id=?1", nativeQuery = true)
	Page<Object[]> findByIdShow(int sid,int id,Pageable pageable);

	@Query(value="select s.id,s.staff_name,duty.duty_name,d.depart_name,s.authority,s.phone from (staff as s left join department as d on s.depart_id=d.id) left join duty on duty.id=s.duty_id where s.staff_name=?2 and d.station_id=?1",countQuery="select count(s.id) from (staff as s left join department as d on s.depart_id=d.id) left join duty on duty.id=s.duty_id where s.staff_name=?2 and d.station_id=?1" ,nativeQuery = true)
	Page<Object[]> findByStaffNameShow(int sid,String StaffName,Pageable pageable);

	@Query(value="select s.id,s.staff_name,duty.duty_name,d.depart_name,s.authority,s.phone from (staff as s left join department as d on s.depart_id=d.id) left join duty on duty.id=s.duty_id where d.station_id=?1 order by s.id",countQuery="select count(s.id) from staff as s", nativeQuery = true)
	Page<Object[]> findAllShow(int sid,Pageable pageable);

	Page<Staff> findById(int id,Pageable pageable);
	Page<Staff> findByStaffName(String staffName,Pageable pageable);
	Page<Staff> findAll(Pageable pageable);

	@Query(value="SELECT s.id, s.staff_name, s.authority, du.duty_name, de.depart_name "
			+ "FROM staff as s LEFT JOIN (duty as du LEFT JOIN department as de ON du.depart_id=de.id) "
			+ "ON s.duty_id=du.id "
			+ "WHERE s.id=?1",
			countQuery="SELECT count(s.id) "
					+ "FROM staff as s LEFT JOIN (duty as du LEFT JOIN department as de ON du.depart_id=de.id) "
					+ "ON s.duty_id=du.id "
					+ "WHERE s.id=?1", nativeQuery=true)
	Page<Object[]> find_st_duN_deN_byId(int id, Pageable pageable);

	@Query(value="SELECT s.id, s.staff_name, s.authority, du.duty_name, de.depart_name "
			+ "FROM staff as s LEFT JOIN (duty as du LEFT JOIN department as de ON du.depart_id=de.id) "
			+ "ON s.duty_id=du.id "
			+ "WHERE s.staff_name=?1 ",
			countQuery="SELECT count(s.id) "
					+ "FROM staff as s LEFT JOIN (duty as du LEFT JOIN department as de ON du.depart_id=de.id) "
					+ "ON s.duty_id=du.id "
					+ "WHERE s.staff_name=?1", nativeQuery=true)
	Page<Object[]> find_st_duN_deN_bystN(String staffName, Pageable pageable);

	@Query(value="SELECT s.id, s.staff_name, s.authority, du.duty_name, de.depart_name "
			+ "FROM staff as s LEFT JOIN (duty as du LEFT JOIN department as de ON du.depart_id=de.id) "
			+ "ON s.duty_id=du.id",
			countQuery="SELECT count(s.id) "
					+ "FROM staff as s LEFT JOIN (duty as du LEFT JOIN department as de ON du.depart_id=de.id) "
					+ "ON s.duty_id=du.id", nativeQuery=true)
	Page<Object[]> find_st_duN_deN_all(Pageable pageable);

	@Query(value="SELECT staff.id, staff.staff_name as staffName, station.station_name as stationName, department.depart_name as departName, duty.duty_name as dutyName, b.sum2 AS deliverNum from (((staff LEFT JOIN (SELECT deliver_part.principal_id,count( deliver_part.principal_id ) AS sum2 FROM deliver LEFT JOIN deliver_part ON deliver.deliverpart_id = deliver_part.id WHERE deliver.finish_date BETWEEN ?2 AND ?3 GROUP BY deliver_part.principal_id) AS b ON b.principal_id = staff.id )LEFT JOIN duty ON duty.id = staff.duty_id )LEFT JOIN department ON duty.depart_id = department.id )LEFT JOIN station ON department.station_id = station.id WHERE station.station_name=?1 and duty.duty_name = '投递员'",
			countQuery="SELECT COUNT(staff.id) from (((staff LEFT JOIN (SELECT deliver_part.principal_id,count( deliver_part.principal_id ) AS sum2 FROM deliver LEFT JOIN deliver_part ON deliver.deliverpart_id = deliver_part.id WHERE deliver.finish_date BETWEEN ?2 AND ?3 GROUP BY deliver_part.principal_id) AS b ON b.principal_id = staff.id )LEFT JOIN duty ON duty.id = staff.duty_id )LEFT JOIN department ON duty.depart_id = department.id )LEFT JOIN station ON department.station_id = station.id WHERE station.station_name=?1 and duty.duty_name = '投递员'",
			nativeQuery = true)
	Page<Object[]> find_deliver_by_station(String stationName, Date startDate, Date endDate, Pageable pageable);

	@Query(value="SELECT staff.id, staff.staff_name as staffName, station.station_name as stationName, department.depart_name as departName, duty.duty_name as dutyName, b.sum2 AS deliverNum from (((staff LEFT JOIN (SELECT deliver_part.principal_id,count( deliver_part.principal_id ) AS sum2 FROM deliver LEFT JOIN deliver_part ON deliver.deliverpart_id = deliver_part.id WHERE deliver.finish_date BETWEEN ?1 AND ?2 GROUP BY deliver_part.principal_id) AS b ON b.principal_id = staff.id )LEFT JOIN duty ON duty.id = staff.duty_id )LEFT JOIN department ON duty.depart_id = department.id )LEFT JOIN station ON department.station_id = station.id WHERE duty.duty_name = '投递员'",
			countQuery="SELECT COUNT(staff.id) from (((staff LEFT JOIN (SELECT deliver_part.principal_id,count( deliver_part.principal_id ) AS sum2 FROM deliver LEFT JOIN deliver_part ON deliver.deliverpart_id = deliver_part.id WHERE deliver.finish_date BETWEEN ?1 AND ?2 GROUP BY deliver_part.principal_id) AS b ON b.principal_id = staff.id )LEFT JOIN duty ON duty.id = staff.duty_id )LEFT JOIN department ON duty.depart_id = department.id )LEFT JOIN station ON department.station_id = station.id WHERE duty.duty_name = '投递员'",
			nativeQuery = true)
	Page<Object[]> find_all_deliver(Date startDate, Date endDate, Pageable pageable);

	@Query(value="SELECT staff.id, staff.staff_name, station.station_name, department.depart_name, duty.duty_name, a.sum1 from (((staff left join  duty on staff.duty_id=duty.id)  left join department on duty.depart_id=department.id)left join (select solicit_id,count(id)as sum1 from orders where orders.pay_date BETWEEN ?2 AND ?3 GROUP by solicit_id) as a on a.solicit_id=staff.id) LEFT JOIN station ON department.station_id = station.id WHERE station.station_name=?1 and duty.duty_name='征订员'",
			countQuery="SELECT COUNT(staff.id) from (((staff left join  duty on staff.duty_id=duty.id)  left join department on duty.depart_id=department.id)left join (select solicit_id,count(id)as sum1 from orders where orders.pay_date BETWEEN ?2 AND ?3 GROUP by solicit_id) as a on a.solicit_id=staff.id) LEFT JOIN station ON department.station_id = station.id WHERE station.station_name=?1 and duty.duty_name='征订员'",
			nativeQuery=true)
	Page<Object[]> find_solicit_by_station(String stationName, Date startDate, Date endDate, Pageable pageable);

	@Query(value="SELECT staff.id, staff.staff_name, station.station_name, department.depart_name, duty.duty_name, a.sum1 from (((staff left join  duty on staff.duty_id=duty.id)  left join department on duty.depart_id=department.id)left join (select solicit_id,count(id)as sum1 from orders where orders.pay_date BETWEEN ?1 AND ?2 GROUP by solicit_id) as a on a.solicit_id=staff.id) LEFT JOIN station ON department.station_id = station.id WHERE duty.duty_name='征订员'",
			countQuery="SELECT COUNT(staff.id) from (((staff left join  duty on staff.duty_id=duty.id)  left join department on duty.depart_id=department.id)left join (select solicit_id,count(id)as sum1 from orders where orders.pay_date BETWEEN ?1 AND ?2 GROUP by solicit_id) as a on a.solicit_id=staff.id) LEFT JOIN station ON department.station_id = station.id WHERE duty.duty_name='征订员'",
			nativeQuery=true)
	Page<Object[]> find_all_solicit(Date startDate, Date endDate, Pageable pageable);


}
