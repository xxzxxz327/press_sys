package com.xxzxxz.press.repository;

import java.util.List;

import com.xxzxxz.press.model.Reci;
import com.xxzxxz.press.model.ReciKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReciRepository extends JpaRepository<Reci, ReciKey>{
	
	@Query(value="select * from reci_num as r where r.station_id=?1 and year(r.date)=?2 and month(r.date)=?3 order by date asc",nativeQuery=true)
	Page<Reci> findByStationIdAndTime(int stationId, int year, int month, Pageable pageable);
	
	@Query(value="select * from reci_num as r where r.station_id=?1 and r.date=?2",nativeQuery=true)
	Page<Reci> findOneByStationIdAndTime(int stationId, String time, Pageable pageable);
	
	@Query(value="select * from reci_num as r where r.station_id=?1 order by r.date asc",nativeQuery=true)
	Page<Reci> findListByStationId(int stationId, Pageable pageable);
	
	@Query(value="select * from reci_num as r where r.date=?1 order by r.station_id asc",nativeQuery=true)
	Page<Reci> findListByTime(String time, Pageable pageable);
	
	@Query(value="select * from reci_num as r  order by r.station_id asc,r.date asc",nativeQuery=true)
	Page<Reci> findListAll(Pageable pageable);
	
	//以下是editreci时使用
	@Query(value="select * from reci_num as r where r.station_id=?1 and r.date=?2",nativeQuery=true)
	Reci findRByStationIdAndTime(int stationId, String time);
	
	//以下是Total要报使用
	@Query(value="select * from reci_num as r where r.date=?1",nativeQuery=true)
	List<Reci> findYBByTime(String time);

	@Query(value="select * from reci_num as r where r.station_id=?1 ",nativeQuery=true)
	Page<Reci> findByStationId(int stationId,Pageable pageable);

}
