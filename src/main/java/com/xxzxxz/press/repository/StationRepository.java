package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.ChangeReason;
import com.xxzxxz.press.model.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface StationRepository extends JpaRepository<Station,Integer> {
   @Query(value = "select * from station where id!=?1",nativeQuery = true)
   List<Station> findListExceptNow(int stationId);

   @Query(value="select s.id,s.station_name,a.area_name,staff.staff_name from station as s,area as a,staff where s.principal_id=staff.id and s.area_id=a.id and s.id=?1 order by s.id asc",countQuery="select count(s.id) from station as s,area as a,staff where s.principal_id=staff.id and s.area_id=a.id and s.id=?1 order by s.id asc",nativeQuery=true)
   Page<Object[]> findById(int id, Pageable pageable);
   @Query(value="select s.id,s.station_name,a.area_name,staff.staff_name from station as s,area as a,staff where s.principal_id=staff.id and s.area_id=a.id and s.station_name=?1 order by s.id asc",countQuery="select count(s.id) from station as s,area as a,staff where s.principal_id=staff.id and s.area_id=a.id and s.station_name=?1 order by s.id asc",nativeQuery=true)
   Page<Object[]> findByName(String name,Pageable pageable);

   @Query(value="select s.id,s.station_name,a.area_name,staff.staff_name from station as s,area as a,staff where s.principal_id=staff.id and s.area_id=a.id order by s.id asc",countQuery="select count(s.id) from station as s,area as a,staff where s.principal_id=staff.id and s.area_id=a.id  order by s.id asc",nativeQuery=true)
   Page<Object[]> findAllStation(Pageable pageable);

   Station findById(int id);
   Station findByStationName(String stationName);


   @Query(value="select o.id ,s.station_name,d.status,o.station_id ,o.address ,p.press_name from(( orders as o left join station as s on o.station_id=s.id  )left join press_info as p on o.press_id=p.id )left join deliver as d on o.id=d.order_id order by o.id asc",countQuery = "select count(o.id) from orders as o left join station as s on o.station_id=s.id order by o.id asc",nativeQuery = true)
   Page<Object[]> findallstationstu(Pageable pageable);
   @Query(value="select o.id ,s.station_name,d.status,o.station_id ,o.address ,p.press_name from(( orders as o left join station as s on o.station_id=s.id  )left join press_info as p on o.press_id=p.id )left join deliver as d on o.id=d.order_id where s.station_name=?1 order by o.id asc",countQuery = "select count(o.id) from orders as o left join station as s on o.station_id=s.id order by o.id asc",nativeQuery = true)
   Page<Object[]>findAllByStationId(String id, Pageable pageable);

   @Query("select s from Station s")
   List<Station> showAll();
   @Query(nativeQuery=true,countQuery="select count(r.station_id) from reci_num as r where r.station_id=?1",value="select r.station_id,r.date,r.need_num,r.real_num from reci_num as r where r.station_id=?1")
   Page<Station> findById1(int id,Pageable pageable);

   @Query(value = "select * from station where id>5 and id!=?1",nativeQuery = true)
   List<Station> findListStationExceptNow(int stationId);
   @Query(value="select s.station_name from station as s",nativeQuery = true)
   List<String>findstationname();

   @Query(value = "select a.area_name from station as s left join area as a on s.area_id=a.id",nativeQuery = true)
   List<Object> findAreaAddress();
   @Query(value = "select s.id from station as s left join area as a on s.area_id=a.id",nativeQuery = true)
   List<String> findStationIdAddress();
}
