package com.xxzxxz.press.repository;


import com.xxzxxz.press.model.Orders;
import com.xxzxxz.press.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BalanceRepository  extends JpaRepository<Orders,Integer> {

    @Query(value="select o.station_id ,s.station_name,sum(o.price) as price1,sum(case when  o.status=1 then o.price else 0 end) as price2 from orders as o left join station s on o.station_id=s.id group by o.station_id ",countQuery = "select count(s.id) from orders as o left join station as s on o.station_id=s.id order by o.id asc",nativeQuery = true)
    Page<Object[]> findallstationbalance(Pageable pageable);
    @Query(value="select o.station_id ,s.station_name,sum(o.price) as price1,sum(case when  o.status=1 then o.price else 0 end) as price2 from orders as o left join station s on o.station_id=s.id   where s.station_name=?1 ",countQuery = "select count(s.id) from orders as o left join station as s on o.station_id=s.id order by o.id asc",nativeQuery = true)
    Page<Object[]>findAllByStationId(String id, Pageable pageable);
    @Query(value="select p.office_id ,off.office_name, sum(o.price) as price1,sum(case when  o.status=1 then o.price else 0 end) as price2 from (orders as o left join press_info as p on o.press_id=p.id ) left join office_info as off on p.office_id = off.id  group by p.office_id order by p.office_id",countQuery = "select count(p.id) from orders as o left join press_info as p on o.press_id=p.id order by o.id asc",nativeQuery = true)
    Page<Object[]> findallofficebalance(Pageable pageable);
    @Query(value="select p.office_id ,off.office_name, sum(o.price) as price1,sum(case when  o.status=1 then o.price else 0 end) as price2 from (orders as o left join press_info as p on o.press_id=p.id ) left join office_info as off on p.office_id = off.id   where off.office_name=?1 order by p.office_id",countQuery = "select count(p.id) from orders as o left join press_info as p on o.press_id=p.id order by o.id asc",nativeQuery = true)
    Page<Object[]> findAllByofficeId(String id, Pageable pageable);
    @Query(value="select off.office_name from office_info as off",nativeQuery = true)
    List<String> findofficename();
    @Query(value="select s.station_name from station as s",nativeQuery = true)
    List<String>findstationname();
}
