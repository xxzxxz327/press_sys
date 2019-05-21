package com.xxzxxz.press.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.xxzxxz.press.model.Department;



public interface DepartRepository extends JpaRepository<Department,Integer> {
	Department findById(int id);
	List<Department> findByDepartName(String departName);
	List<Department> findAll();
	
	Department save(Department depart);
	
	Page<Department> findById(int id, Pageable pageable);
	Page<Department> findByDepartName(String departName, Pageable pageable);
	//@Query(value="select department.id as id,department.depart_name as departName,staff.staff_name as principalName,station.station_name as stationName from department,station,staff where department.principal_id=staff.id and department.station_id=station.id", nativeQuery = true)
	Page<Department> findAll(Pageable pageable);
	
	//@Query(value="select id as id,depart_name as departName,station_id as stationId,principal_id as principalId from department", nativeQuery = true)
	//@Query(value="select department.id as id,department.depart_name as departName,staff.staff_name as principalName,station.station_name as stationName from department,station,staff where department.principal_id=staff.id and department.station_id=station.id", nativeQuery = true)
	@Query(value="select department.id as id,department.depart_name as departName,staff.staff_name as principalName,station.station_name as stationName from ((department left join station on department.station_id=station.id) left join staff on department.principal_id=staff.id ) order by department.id asc",countQuery="select count(department.id)  from ((department left join station on department.station_id=station.id) left join staff on department.principal_id=staff.id ) order by department.id asc",nativeQuery=true)
	Page<Object[]> findAllShow(Pageable pageable);
	
	@Query(value="select department.id as id,department.depart_name as departName,staff.staff_name as principalName,station.station_name as stationName from department,station,staff where department.principal_id=staff.id and department.station_id=station.id", nativeQuery = true)
	List<Object[]> findtest();
	
	@Query(value="select department.id as id,department.depart_name as departName,staff.staff_name as principalName,station.station_name as stationName from ((department left join station on department.station_id=station.id) left join staff on department.principal_id=staff.id ) where department.id=?1 order by department.id asc",countQuery="select count(department.id)  from ((department left join station on department.station_id=station.id) left join staff on department.principal_id=staff.id ) where department.id=?1  order by department.id asc",nativeQuery=true)
	Page<Object[]> findByIdShow(int id, Pageable pageable);
	
	
	@Query(value="select department.id as id,department.depart_name as departName,staff.staff_name as principalName,station.station_name as stationName from ((department left join station on department.station_id=station.id) left join staff on department.principal_id=staff.id ) where department.depart_name=?1 order by department.id asc",countQuery="select count(department.id)  from ((department left join station on department.station_id=station.id) left join staff on department.principal_id=staff.id ) where department.depart_name=?1  order by department.id asc",nativeQuery=true)
	Page<Object[]> findByDepartNameShow(String departName, Pageable pageable);
	
}
