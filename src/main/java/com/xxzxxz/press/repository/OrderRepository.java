package com.xxzxxz.press.repository;


import com.xxzxxz.press.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface OrderRepository  extends JpaRepository<Orders,Integer> {

    @Query(value="select o.id ,s.station_name,o.price,o.status,o.xia_time,c.name,o.station_id  from (orders as o left join station as s on o.station_id=s.id) left join consumer as c on o.consumer_id=c.id order by o.id asc",countQuery = "select count(o.id) from orders as o left join station as s on o.station_id=s.id order by o.id asc",nativeQuery = true)
    Page<Object[]> findallorderstationname(Pageable pageable);
    @Query(value="select o.id ,s.station_name,o.price,o.status,o.xia_time,c.name,o.station_id  from (orders as o left join station as s on o.station_id=s.id) left join consumer as c on o.consumer_id=c.id where o.station_id=?1 order by o.id asc",countQuery = "select count(o.id) from orders as o left join station as s on o.station_id=s.id order by o.id asc",nativeQuery = true)
    Page<Object[]>findAllByStationId(Integer id, Pageable pageable);
}
