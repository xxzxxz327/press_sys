package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.PressInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PressRepository extends JpaRepository<PressInfo,Integer>{
	
	@Query(value="select p.id,p.press_name,p.office_id,o.office_name from press_info as p left join office_info as o on p.office_id=o.id where o.office_name=?1 order by p.id asc",countQuery="select count(p.id) from press_info as p left join office_info as o on p.office_id=o.id where o.office_name=?1 order by p.id asc" ,nativeQuery = true)
	Page<Object[]> findByOfficeName(String officeName, Pageable pageable);
	
	@Query(value="select p.id,p.press_name,p.office_id,o.office_name from press_info as p left join office_info as o on p.office_id=o.id where p.press_name=?1 order by p.id asc",countQuery="select count(p.id) from press_info as p left join office_info as o on p.office_id=o.id where p.press_name=?1 order by p.id asc" ,nativeQuery = true)
	Page<Object[]> findByPressName(String pressName, Pageable pageable);
	
	@Query(value="select p.id,p.press_name,p.office_id,o.office_name from press_info as p left join office_info as o on p.office_id=o.id  order by p.id asc",countQuery="select count(p.id) from press_info as p left join office_info as o on p.office_id=o.id  order by p.id asc" ,nativeQuery = true)
	Page<Object[]> findAllPress(Pageable pageable);


	@Query(value="select o.id ,s.station_name,d.status,o.station_id ,o.copies,p.press_name,o.press_id from(( orders as o left join station as s on o.station_id=s.id  )left join press_info as p on o.press_id=p.id ) left join deliver as d on o.id=d.order_id  order by o.id asc",countQuery = "select count(o.id) from orders as o left join station as s on o.station_id=s.id order by o.id asc",nativeQuery = true)
	Page<Object[]> findallpressstu(Pageable pageable);
	@Query(value="select o.id ,s.station_name,d.status,o.station_id ,o.copies,p.press_name,o.press_id from(( orders as o left join station as s on o.station_id=s.id  )left join press_info as p on o.press_id=p.id ) left join deliver as d on o.id=d.order_id where p.press_name=?1 order by o.id asc",countQuery = "select count(o.id) from orders as o left join station as s on o.station_id=s.id order by o.id asc",nativeQuery = true)
	Page<Object[]>findAllBypressId(String id, Pageable pageable);


	//报刊名模糊输入
	@Query(nativeQuery=true,value="select p.press_name from press_info as p")
	List<Object[]>  showAllpName();

	@Query(nativeQuery=true,
			value="SELECT press_info.id,press_info.press_name,office_info.office_name,frequency.type,press_info.day_price,press_info.week_price,press_info.month_price,press_info.session_price,press_info.half_year_price,press_info.year_price "
					+ "FROM (press_info LEFT JOIN office_info on press_info.office_id=office_info.id)LEFT JOIN frequency on press_info.frequency_id=frequency.id ")
	Page<Object[]> showPressInfo(Pageable pageable);

	@Query(nativeQuery=true,
			value="SELECT press_info.id,press_info.press_name,office_info.office_name,frequency.type,press_info.day_price,press_info.week_price,press_info.month_price,press_info.session_price,press_info.half_year_price,press_info.year_price "
					+ "FROM (press_info LEFT JOIN office_info on press_info.office_id=office_info.id)LEFT JOIN frequency on press_info.frequency_id=frequency.id "
					+ "where press_info.id=?1")
	Page<Object[]> showonePressInfo(int id,Pageable pageable);

	@Query(nativeQuery=true,
			value="SELECT press_info.id,press_info.press_name,office_info.office_name,frequency.type,press_info.day_price,press_info.week_price,press_info.month_price,press_info.session_price,press_info.half_year_price,press_info.year_price "
					+ "FROM (press_info LEFT JOIN office_info on press_info.office_id=office_info.id)LEFT JOIN frequency on press_info.frequency_id=frequency.id "
					+ "where press_info.press_name=?1")
	Page<Object[]> showonePressInfo(String pName,Pageable pageable);



	//报刊订阅信息查询
	@Query(nativeQuery=true,
			value="SELECT press_info.id,press_info.press_name,frequency.type,COUNT(orders.id)AS sum1,SUM(orders.price)AS sum2 "
					+ "FROM (press_info LEFT JOIN frequency ON frequency.id=press_info.frequency_id)LEFT JOIN orders ON orders.press_id=press_info.id "
					+ "WHERE orders.station_id=?1 AND orders.`status`<>3 "
					+ "GROUP BY press_info.id")
	Page<Object[]> showpressorder(int sid,Pageable pageable);


	@Query(nativeQuery=true,
			value="SELECT press_info.id,press_info.press_name,frequency.type,COUNT(orders.id)AS sum1,SUM(orders.price)AS sum2 "
					+ "FROM (press_info LEFT JOIN frequency ON frequency.id=press_info.frequency_id)LEFT JOIN orders ON orders.press_id=press_info.id "
					+ "WHERE orders.station_id=?1 AND orders.`status`<>3 AND press_info.press_name=?2 "
					+ "GROUP BY press_info.id")
	Page<Object[]> showonepressorder(int sid,String pName,Pageable pageable);


	@Query(nativeQuery=true,
			value="SELECT press_info.id,press_info.press_name,frequency.type,COUNT(orders.id)AS sum1,SUM(orders.price)AS sum2 "
					+ "FROM (press_info LEFT JOIN frequency ON frequency.id=press_info.frequency_id)LEFT JOIN orders ON orders.press_id=press_info.id "
					+ "WHERE orders.station_id=?1 AND orders.`status`<>3 AND press_info=?2"
					+ "GROUP BY press_info.id")
	Page<Object[]> showonepressorder(int sid,int id,Pageable pageable);

	Page<PressInfo> findById(int id, Pageable pageable);
	@Query(value="SELECT p.press_name, p.id, o.office_name FROM press_info as p LEFT JOIN office_info as o ON p.office_id=o.id where p.id=?1",nativeQuery=true, countQuery="select count(p.id) FROM press_info as p LEFT JOIN office_info as o ON p.office_id=o.id where p.id=?1")
	Page<Object> findByIdO(int id,Pageable pageable);
	@Query(value = "select * from press_info where press_name=?1",nativeQuery = true)
	Page<PressInfo> findByPressName1(String pressName, Pageable pageable);
	@Query(value="SELECT p.press_name, p.id, o.office_name FROM press_info as p LEFT JOIN office_info as o ON p.office_id=o.id where p.press_name=?1",nativeQuery=true, countQuery="select count(p.id) FROM press_info as p LEFT JOIN office_info as o ON p.office_id=o.id where p.press_name=?1")
	Page<Object[]> findByPressNameO(String pressName,Pageable pageable);
	Page<PressInfo> findByFrequencyId(int FrequencyId,Pageable pageable);
	Page<PressInfo> findByOfficeId(int officeId,Pageable pageable);
	Page<PressInfo> findAll(Pageable pageable);
	@Query(value="SELECT p.press_name, p.id, o.office_name FROM press_info as p LEFT JOIN office_info as o ON p.office_id=o.id",nativeQuery=true, countQuery="select count(p.id) FROM press_info as p LEFT JOIN office_info as o ON p.office_id=o.id")
	Page<Object[]> findPressAll(Pageable pageable);

	@Query(value="SELECT * FROM press_info",nativeQuery=true)
	List<PressInfo> findList();

	PressInfo findById(int id);
	PressInfo findByPressName(String pressName);
	@Query(value="select p.press_name from press_info as p",nativeQuery = true)
	List<String>findpressname();


}
