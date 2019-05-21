package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Advice;
import com.xxzxxz.press.model.Praise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AdviceRepository extends JpaRepository<Advice,Integer> {
    @Query(value = "select a.id,con.name,p.press_name,a.content,a.status,a.station_id from ((advice as a left join consumer as con on con.id=a.consumer_id) left join orders as o on a.order_id=o.id) left join press_info as p on o.press_id=p.id where a.station_id=?1",
            countQuery = "select count(a.id) from ((advice as a left join consumer as con on con.id=a.consumer_id) left join orders as o on a.order_id=o.id) left join press_info as p on o.press_id=p.id where a.station_id=?1",
            nativeQuery = true)
    Page<Object[]> findListByStationId(int stationId, Pageable pageable);
    @Query(value = "select a.id,con.name,p.press_name,a.content,a.status,a.station_id from ((advice as a left join consumer as con on con.id=a.consumer_id) left join orders as o on a.order_id=o.id) left join press_info as p on o.press_id=p.id where a.station_id=?1 and a.status=?2",
            countQuery = "select count(a.id) from ((advice as a left join consumer as con on con.id=a.consumer_id) left join orders as o on a.order_id=o.id) left join press_info as p on o.press_id=p.id where a.station_id=?1 and a.status=?2",
            nativeQuery = true)
    Page<Object[]> findListByStationIdAndStatus(int stationId,int status, Pageable pageable);
    @Query(value = "select a.id,con.name,p.press_name,a.content,a.status,a.station_id from ((advice as a left join consumer as con on con.id=a.consumer_id) left join orders as o on a.order_id=o.id) left join press_info as p on o.press_id=p.id where a.id=?1",
    nativeQuery = true)
    List<Object[]> findById1(int id);
    @Query(value="select * from advice where id=?1",nativeQuery = true)
    Advice findById2(int id);

    @Query(value="select a.id,c.id as cid,c.name,a.content,a.status,c.phone,a.order_id from advice as a,consumer as c where a.consumer_id=c.id and a.id=?1 order by a.id asc",countQuery="select count(a.id) from advice as a,consumer as c where a.consumer_id=c.id and a.id=?1 order by a.id asc",nativeQuery=true)
    Page<Object[]> findByAdviceId(int adviceId,Pageable pageable);

    @Query(value="select a.id,c.id as cid,c.name,a.content,a.status,c.phone,a.order_id from advice as a,consumer as c where a.consumer_id=c.id and c.id=?1 order by a.id asc",countQuery="select count(a.id) from advice as a,consumer as c where a.consumer_id=c.id and c.id=?1 order by a.id asc",nativeQuery=true)
    Page<Object[]> findByConsumerId(int consumerId,Pageable pageable);

    @Query(value="select a.id,c.id as cid,c.name,a.content,a.status,c.phone,a.order_id from advice as a,consumer as c where a.consumer_id=c.id order by a.id asc",countQuery="select count(a.id) from advice as a,consumer as c where a.consumer_id=c.id order by a.id asc",nativeQuery=true)
    Page<Object[]> findAllAdvice(Pageable pageable);

    Advice findById(int id);
}
