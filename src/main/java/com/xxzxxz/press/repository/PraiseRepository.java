package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Complain;
import com.xxzxxz.press.model.Praise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PraiseRepository extends JpaRepository<Praise,Integer> {
    @Query(value = "select pr.id,con.name,p.press_name,pr.content,pr.status,pr.station_id from ((praise as pr left join consumer as con on con.id=pr.consumer_id) left join orders as o on pr.order_id=o.id) left join press_info as p on o.press_id=p.id where pr.station_id=?1",
            countQuery = "select count(pr.id) from ((praise as pr left join consumer as con on con.id=pr.consumer_id) left join orders as o on pr.order_id=o.id) left join press_info as p on o.press_id=p.id where pr.station_id=?1",
            nativeQuery = true)
    Page<Object[]> findListByStationId(int stationId, Pageable pageable);
    @Query(value = "select pr.id,con.name,p.press_name,pr.content,pr.status,pr.station_id from ((praise as pr left join consumer as con on con.id=pr.consumer_id) left join orders as o on pr.order_id=o.id) left join press_info as p on o.press_id=p.id where pr.station_id=?1 and pr.status=?2",
            countQuery = "select count(pr.id) from ((praise as pr left join consumer as con on con.id=pr.consumer_id) left join orders as o on pr.order_id=o.id) left join press_info as p on o.press_id=p.id where pr.station_id=?1 and pr.status=?2",
            nativeQuery = true)
    Page<Object[]> findListByStationIdAndStatus(int stationId,int status, Pageable pageable);
    @Query(value = "select pr.id,con.name,p.press_name,pr.content,pr.status,pr.station_id from ((praise as pr left join consumer as con on con.id=pr.consumer_id) left join orders as o on pr.order_id=o.id) left join press_info as p on o.press_id=p.id where pr.id=?1",
    nativeQuery = true)
    List<Object[]> findById(int id);
    @Query(value = "select * from praise where id=?1",nativeQuery = true)
    Praise findById2(int id);

    @Query(value="select p.id,c.id as cid,c.name,p.content,p.status,c.phone,p.order_id from praise as p,consumer as c where p.consumer_id=c.id and p.id=?1 order by p.id asc",countQuery="select count(p.id) from praise as p,consumer as c where p.consumer_id=c.id and p.id=?1 order by p.id asc",nativeQuery=true)
    Page<Object[]> findByPraiseId(int praiseId,Pageable pageable);

    @Query(value="select p.id,c.id as cid,c.name,p.content,p.status,c.phone,p.order_id from praise as p,consumer as c where p.consumer_id=c.id and c.id=?1 order by p.id asc",countQuery="select count(p.id) from praise as p,consumer as c where p.consumer_id=c.id and c.id=?1 order by p.id asc",nativeQuery=true)
    Page<Object[]> findByConsumerId(int consumerId,Pageable pageable);

    @Query(value="select p.id,c.id as cid,c.name,p.content,p.status,c.phone,p.order_id from praise as p,consumer as c where p.consumer_id=c.id order by p.id asc",countQuery="select count(p.id) from praise as p,consumer as c where p.consumer_id=c.id order by p.id asc",nativeQuery=true)
    Page<Object[]> findAllPraise(Pageable pageable);
}
