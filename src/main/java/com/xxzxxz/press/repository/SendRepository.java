package com.xxzxxz.press.repository;

import java.util.Date;
import java.util.List;

import com.xxzxxz.press.model.SendStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;






public interface SendRepository extends JpaRepository<SendStation,Integer> {
	@Query(value = "select * from  send_station as s",nativeQuery = true)
	Page<SendStation>findList(Pageable pageable);
/*	@Query(value = "select s.id ,s.address,s.phone,s.name,sum(ps.need_num) as sum_n from print_send as ps left join send_station as s on ps.send_id=s.id where s.type=2 and s.id is not null",
			countQuery = "select count(s.id) from print_send as ps left join send_station as s on ps.send_id=s.id where s.type=2 and s.id is not null",nativeQuery = true)
	Page<Object[]>findSpeList(Pageable pageable);*/

	@Query(value = "select s.id,s.address,s.phone,s.name,sum(ps.need_num) as sum_n from print_send as ps left join send_station as s on ps.send_id=s.id where s.type=2 and trim(s.id) !=''",
			countQuery = "select count(s.id) from print_send as ps left join send_station as s on ps.send_id=s.id where s.type=2 and trim(s.id) !=''",nativeQuery = true)
	Page<Object[]>findSpeList(Pageable pageable);


	SendStation findById(int id);

	@Query(value = "select * from send_station where send_station.id=?1 and send_station.type=1",
			countQuery = "select count(id) from send_station where send_station.id=?1 and send_station.type=1",
			nativeQuery = true)
	Page<SendStation> findById1(int id,Pageable pageable);

	@Query(value = "select s.id,s.address,s.phone,s.name,sum(ps.need_num) from print_send as ps left join send_station as s on ps.send_id=s.id where s.id=?1 and s.type=2",nativeQuery = true)
	List<Object[]> findById2(int id);


	List<SendStation> findAll();
	
	SendStation save(SendStation sendStation);
	void deleteById(int id);

	@Query(value = "select ps.id,p.name as name1,s.name,ps.print_num,ps.date from (print_send as ps left join print as p on p.id=ps.print_id) left join send_station as s on s.id=ps.send_id where ps.id=?1", nativeQuery = true)
	List<Object[]> findPrintNumList(int id);

	@Query(value = "select ps.id,p.name as name1,s.name,ps.print_num,ps.date from (print_send as ps left join print as p on p.id=ps.print_id) left join send_station as s on s.id=ps.send_id",
			countQuery = "select count(ps.id) from (print_send as ps left join print as p on p.id=ps.print_id) left join send_station as s on s.id=ps.send_id",nativeQuery = true)
	Page<Object[]> findPrintNumPage(Pageable pageable);

	@Query(value = "select ps.id,p.name as name1,s.name,ps.need_num,ps.date from (print_send as ps left join print as p on p.id=ps.print_id) left join send_station as s on s.id=ps.send_id where ps.id=?1",nativeQuery = true)
	List<Object[]> findSendNumList(int id);

	@Query(value = "select ps.id,p.name as name1,s.name,ps.need_num,ps.date from (print_send as ps left join print as p on p.id=ps.print_id) left join send_station as s on s.id=ps.send_id",
			countQuery = "select count(ps.id) from (print_send as ps left join print as p on p.id=ps.print_id) left join send_station as s on s.id=ps.send_id",nativeQuery = true)
	Page<Object[]> findSendNumPage(Pageable pageable);

	@Query(value = "select ps.id,p.name as name1,s.name,ps.need_num,ps.date from (print_send as ps left join print as p on p.id=ps.print_id) left join send_station as s on s.id=ps.send_id where ps.id=?1",nativeQuery = true)
	Page<Object[]> findSendNumOne(int id,Pageable pageable);

	@Query(value = "select ps.id,p.name as name1,s.name,ps.print_num,ps.date from (print_send as ps left join print as p on p.id=ps.print_id) left join send_station as s on s.id=ps.send_id where ps.id=?1",
			countQuery = "select count(ps.id) from ((print_send as ps left join route as r on ps.print_id=r.print_id) left join print as p on p.id=ps.print_id) left join send_station as s on s.id=ps.send_id where ps.id=?1",nativeQuery = true)
	Page<Object[]> findPrintNumOne(int id,Pageable pageable);

	@Query(value = "select sum(ps.print_num) as sum_send,s.name,ps.date from print_send as ps left join send_station as s on ps.send_id=s.id where ps.send_id=?1 and ps.date=?2",nativeQuery = true)
	List<Object[]> findBySendIdAndDate(Integer send_id,Date date);


}
