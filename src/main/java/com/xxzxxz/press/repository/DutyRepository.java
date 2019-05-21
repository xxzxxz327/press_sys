package com.xxzxxz.press.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xxzxxz.press.model.Duty;

import java.util.List;

public interface DutyRepository extends JpaRepository<Duty,Integer>{
	
	@Query(value="select duty.id,duty.duty_name,duty.merit_id,department.depart_name from duty  left join department  on duty.depart_id=department.id where duty.id=?1", nativeQuery = true)
	Page<Object[]> findByIdShow(int id, Pageable pageable);
	
	@Query(value="select duty.id,duty.duty_name,duty.merit_id,department.depart_name from duty  left join department  on duty.depart_id=department.id where duty.duty_name=?1",countQuery="select count(duty.id) from duty  left join department  on duty.depart_id=department.id where duty.duty_name=?1" ,nativeQuery = true)
	Page<Object[]> findByDutyNameShow(String dutyName, Pageable pageable);
	
	@Query(value="select duty.id ,duty.duty_name ,duty.merit_id,department.depart_name  from duty left join department on duty.depart_id=department.id order by id asc",countQuery="select count(duty.id) from duty left join department on duty.depart_id=department.id order by duty.id asc", nativeQuery = true)
	/*
	@Query(value="select id,name,merit,depart from "+
"(select du.id as id,du.duty_name as name,du.merit_id as merit,de.depart_name as depart  from "+
"duty as du left join department as de on du.depart_id=de.id) as dd order by id", nativeQuery = true)*/
	Page<Object[]> findAllShow(Pageable pageable);
	
	Duty findById(int id);

	@Query(value="select duty.id,duty.duty_name,duty.merit_id,department.depart_name from duty  left join department  on duty.depart_id=department.id where duty.id=?1 and department.station_id=?2", nativeQuery = true)
	Page<Object[]> findByIdShow(int id,int sid,Pageable pageable);

	@Query(value="select duty.id,duty.duty_name,duty.merit_id,department.depart_name from duty  left join department  on duty.depart_id=department.id where duty.duty_name=?1 and department.station_id=?2",countQuery="select count(duty.id) from duty  left join department  on duty.depart_id=department.id where duty.duty_name=?1 and department.station_id=?2" ,nativeQuery = true)
	Page<Object[]> findByDutyNameShow(String dutyName,int sid,Pageable pageable);

	@Query(value="select duty.id ,duty.duty_name ,duty.merit_id,department.depart_name  from duty left join department on duty.depart_id=department.id where department.station_id=?1 order by id asc",countQuery="select count(duty.id) from duty left join department on duty.depart_id=department.id where department.station_id=?2 order by duty.id asc", nativeQuery = true)
	/*
	@Query(value="select id,name,merit,depart from "+
"(select du.id as id,du.duty_name as name,du.merit_id as merit,de.depart_name as depart  from "+
"duty as du left join department as de on du.depart_id=de.id) as dd order by id", nativeQuery = true)*/
	Page<Object[]> findAllShow(int sid,Pageable pageable);

	@Query(value="SELECT du.id, du.duty_name, de.depart_name FROM duty as du LEFT JOIN department as de ON du.depart_id=de.id", nativeQuery=true,
			countQuery="SELECT count(du.id) FROM duty as du LEFT JOIN department as de ON du.depart_id=de.id")
	List<Object[]> findList();



	//职务模糊输入（发行站）
	@Query(nativeQuery=true,value="select duty.duty_name "
			+ "from duty left join department on duty.depart_id=department.id "
			+ "where department.station_id=?1")
	List<Object[]> showAllFXDutyName(int sid);

	//下拉框(STAFFYONG)
	@Query(nativeQuery=true,value="select duty.id,duty.duty_name "
			+ "from duty left join department on duty.depart_id=department.id "
			+ "where department.station_id=?1")
	List<Object[]> showAllFXDuty(int sid);




}
