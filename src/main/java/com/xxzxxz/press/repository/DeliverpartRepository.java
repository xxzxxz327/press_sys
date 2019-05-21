package com.xxzxxz.press.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



import com.xxzxxz.press.model.Deliverpart;

public interface DeliverpartRepository extends JpaRepository<Deliverpart, Integer> {
	@Query(value="select d.id,a.area_name,s.staff_name,s.phone,d.address "
			+ "from ((deliver_part as d left join area as a on a.id=d.area_id) "
			+ "left join staff as s on s.id=d.principal_id)left join department on s.depart_id=department.id "
			+ "where department.station_id=?1 "
			+ "order by d.id",
			countQuery="select count(d.id) "
					+ "from ((deliver_part as d left join area as a on a.id=d.area_id) "
					+ "left join staff as s on s.id=d.principal_id)left join department on s.depart_id=department.id "
					+ "where department.station_id=?1 "
					+ "order by d.id",
					nativeQuery=true)
    Page<Object[]> findAllDeliverpart(int sid,Pageable pageable);
    @Query(value="select d.id,a.area_name,s.staff_name,s.phone,d.address "
    		+ "from ((deliver_part as d left join area as a on a.id=d.area_id) "
			+ "left join staff as s on s.id=d.principal_id)left join department on s.depart_id=department.id "
			+ "where department.station_id=?2 and d.id=?1 "
			+ "order by d.id",
    		countQuery="select count(d.id) "
    				+ "from ((deliver_part as d left join area as a on a.id=d.area_id)"
    				+ "left join staff as s on s.id=d.principal_id)left join department on s.depart_id=department.id "
    				+ "where department.station_id=?2 and d.id=?1 "
    				+ "order by d.id",
    				nativeQuery=true)
    Page<Object[]> findById(int id,int sid,Pageable pageable);
    Deliverpart findById(int id);

	@Query(value="select d.id,a.area_name,s.staff_name,s.phone,d.address "
			+ "from ((deliver_part as d left join area as a on a.id=d.area_id) "
			+ "left join staff as s on s.id=d.principal_id)left join department on s.depart_id=department.id "
			+ "where department.station_id=?2 and d.address=?1 "
			+ "order by d.id",
			countQuery="select count(d.id) "
					+ "from ((deliver_part as d left join area as a on a.id=d.area_id)"
					+ "left join staff as s on s.id=d.principal_id)left join department on s.depart_id=department.id "
					+ "where department.station_id=?2 and d.address=?1 "
					+ "order by d.id",
			nativeQuery=true)
	Page<Object[]> findByAddress(String address,int sid,Pageable pageable);


	@Query(nativeQuery=true,value="select d.id,department.station_id "
    		+ "from (deliver_part as d left join staff as s on s.id=d.principal_id)"
    		+ "left join department on s.depart_id=department.id ")
    List<Object[]> showAll();


	//打印
	@Query(value="select d.id,a.area_name,s.staff_name,s.phone,d.address "
			+ "from ((deliver_part as d left join area as a on a.id=d.area_id) "
			+ "left join staff as s on s.id=d.principal_id)left join department on s.depart_id=department.id "
			+ "where department.station_id=?1 "
			+ "order by d.id",
			countQuery="select count(d.id) "
					+ "from ((deliver_part as d left join area as a on a.id=d.area_id) "
					+ "left join staff as s on s.id=d.principal_id)left join department on s.depart_id=department.id "
					+ "where department.station_id=?1 "
					+ "order by d.id",
			nativeQuery=true)
	List<Object[]> findAllDeliverpart(int sid);

	//模糊输入框
	@Query(nativeQuery=true,value="SELECT deliver_part.address "
			+ "FROM deliver_part")
	List<Object[]> showalldeliverpartAddress();

}
