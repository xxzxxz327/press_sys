package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Deliver;
import com.xxzxxz.press.model.Reci;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DeliverRepository extends JpaRepository<Deliver,Integer> {
    @Query(value="select s.id as stationId,s.station_name as stationName,d.finish_date,SUM(o.copies) from deliver as d,orders as o,station as s where d.order_id=o.id and o.station_id=s.id and o.station_id=?1 and d.status=1 and year(d.finish_date)=?2 and month(d.finish_date)=?3 group by d.finish_date  order by  d.finish_date asc",countQuery = "select count(d.finish_date) from deliver as d,orders as o,station as s where d.order_id=o.id and o.station_id=s.id and o.station_id=?1 and d.status=1 and year(d.finish_date)=?2 and month(d.finish_date)=?3 group by d.finish_date  order by  d.finish_date asc",nativeQuery=true)
    Page<Object[]> findByStationIdAndTimeInCenter(int stationId, int year, int month, Pageable pageable);

    //优先度
    @Query
            (nativeQuery=true,value="select d.id,c.name,c.address,p.press_name,d.deliverpart_id,d.priority from (deliver as d left join orders as o on d.order_id=o.id)  left join consumer as c on o.consumer_id=c.id left join press_info as p on o.press_id=p.id where o.station_id=?1 order by d.id",countQuery="select count(d.id) from deliver as d")
    Page<Object[]> findAllpriority(int sid,Pageable pageable);
    @Query
            (nativeQuery=true,value="select d.id,c.name,c.address,p.press_name,d.deliverpart_id,d.priority from (deliver as d left join orders as o on d.order_id=o.id)  left join consumer as c on o.consumer_id=c.id left join press_info as p on o.press_id=p.id where o.id = ?1 and o.station_id=?2 order by o.id")
    Page<Object[]> findpriorityByid(int id,int sid,Pageable pageable);
    @Query
            (nativeQuery=true,
                    value="select d.id,c.name,c.address,p.press_name,d.deliverpart_id,d.priority from (deliver as d left join orders as o on d.order_id=o.id)  left join consumer as c on o.consumer_id=c.id left join press_info as p on o.press_id=p.id where c.name = ?1 and o.station_id=?2 order by o.id",
                    countQuery="select count(d.id) from (deliver as d left join orders as o on d.order_id=o.id)  left join consumer as c on o.consumer_id=c.id left join press_info as p on o.press_id=p.id where c.name = ?1 and o.station_id=?2 order by o.id")
    Page<Object[]> findpriorityBycName(String cName,int sid,Pageable pageable);

    //完成情况

    @Query
            (nativeQuery=true,value="select d.id,d.order_id,d.deliverpart_id,o.address,d.`status`,d.finish_date from deliver as d left join orders as o on d.order_id=o.id where d.id=?1 and o.station_id=?2")
    Page<Object[]> findoneByid(int id,int sid,Pageable pageable);

    @Query
            (nativeQuery=true,value="select d.id,d.order_id,c.name,o.address,d.`status`,d.finish_date "
                    + "from (deliver as d left join orders as o on d.order_id=o.id)left join consumer as c on c.id=o.consumer_id "
                    + "where c.name=?1 and o.station_id=?2")
    Page<Object[]> findoneBycName(String address,int sid,Pageable pageable);
    @Query
            (nativeQuery=true,value="select d.id,d.order_id,c.name,o.address,d.`status`,d.finish_date "
                    + "from (deliver as d left join orders as o on d.order_id=o.id)left join consumer as c on c.id=o.consumer_id "
                    + "where o.station_id=?1")
    Page<Object[]> findAllforcom(int sid,Pageable pageable);

    //

    @Query(nativeQuery=true,value="select * from deliver as d where d.order_id=?1")
    Deliver findByorderId(Integer id);

    @Query(nativeQuery=true,value="select * from deliver")
    List<Deliver> showAll();

    //投递查询

    @Query(nativeQuery=true,value="select deliver.id,consumer.name,staff.staff_name,deliver.priority,deliver.status,deliver.finish_date "
            + " FROM ((((deliver LEFT JOIN deliver_part ON deliver.deliverpart_id=deliver_part.id)"
            + "LEFT JOIN staff on staff.id=deliver_part.principal_id)"
            + "LEFT JOIN department on department.id=staff.depart_id)"
            + "left join orders on orders.id=deliver.order_id)"
            + "left join consumer on consumer.id=orders.consumer_id"
            + " WHERE department.station_id=?1")
    Page<Object[]> showAll(int sid,Pageable pageable);

    @Query(nativeQuery=true,value="select deliver.* "
            + " FROM ((deliver LEFT JOIN deliver_part ON deliver.deliverpart_id=deliver_part.id)"
            + "LEFT JOIN staff on staff.id=deliver_part.principal_id)"
            + "LEFT JOIN department on department.id=staff.depart_id"
            + " WHERE department.station_id=?1 AND deliver.id=?2")
    Page<Deliver> showOneByid(int sid,int id,Pageable pageable);


    @Query(nativeQuery=true,value="select deliver.id,consumer.name,staff.staff_name,deliver.priority,deliver.status,deliver.finish_date "
            + " FROM ((((deliver LEFT JOIN deliver_part ON deliver.deliverpart_id=deliver_part.id)"
            + "LEFT JOIN staff on staff.id=deliver_part.principal_id)"
            + "LEFT JOIN department on department.id=staff.depart_id)"
            + "left join orders on orders.id=deliver.order_id)"
            + "left join consumer on consumer.id=orders.consumer_id"
            + " WHERE department.station_id=?1 AND staff.staff_name=?2")
    Page<Object[]> showOneBysName(int sid,String sName,Pageable pageable);


    //

    Deliver findById(int id);

    //统计查询用
    @Query
            (nativeQuery=true,
                    value="SELECT sum0,sum1,sum2,sum3,sum4"
                            + " FROM (SELECT COUNT(deliver.id)as sum2"
                            + " FROM deliver LEFT JOIN orders ON deliver.order_id=orders.id"
                            + " WHERE orders.station_id=?1 AND orders.xia_time BETWEEN ?2 and ?3 AND deliver.`status`=1)as a,"
                            + " (SELECT COUNT(deliver.id)as sum1"
                            + " FROM deliver LEFT JOIN orders ON deliver.order_id=orders.id"
                            + " WHERE orders.station_id=?1 AND orders.xia_time BETWEEN ?2 and ?3 AND deliver.`status`=0)as b,"
                            + " (SELECT COUNT(deliver.id)as sum3"
                            + " FROM deliver LEFT JOIN orders ON deliver.order_id=orders.id"
                            + " WHERE orders.station_id=?1 AND orders.xia_time BETWEEN ?2 and ?3 AND deliver.`status`=2)as c,"
                            + " (SELECT COUNT(deliver.id)as sum4"
                            + " FROM deliver LEFT JOIN orders ON deliver.order_id=orders.id"
                            + " WHERE orders.station_id=?1 AND orders.xia_time BETWEEN ?2 and ?3 AND deliver.`status`=3)as d,"
                            + " (SELECT COUNT(deliver.id)as sum0"
                            + " FROM deliver LEFT JOIN orders ON deliver.order_id=orders.id"
                            + " WHERE orders.station_id=?1 AND orders.xia_time BETWEEN ?2 and ?3 )as s")
    List<Object[]> findtongjideliver(int sId, Date sDate, Date eDate);

    @Query(value="select d.id as deliverId,o.id as orderId,o.address from deliver as d,orders as o where d.order_id=o.id  and d.status=4 and d.id=?1 order by d.id asc",countQuery="select count(d.id) from deliver as d,orders as o where d.order_id=o.id  and d.status=4 and d.id=?1 order by d.id asc",nativeQuery=true)
    Page<Object[]> findByDeliverIdInCenter(int deliverId,Pageable pageable);

    @Query(value="select d.id as deliverId,o.id as orderId,o.address from deliver as d,orders as o where d.order_id=o.id and d.status=4  order by d.id asc",countQuery="select count(d.id) from deliver as d,orders as o where d.order_id=o.id  and d.status=4 order by d.id asc",nativeQuery=true)
    Page<Object[]> findDeliverInCenter(Pageable pageable);

    @Query(value="select d.id as deliverId,o.id as orderId,o.address from deliver as d,orders as o where d.order_id=o.id  and d.status=4 and d.id=?1 order by d.id asc",nativeQuery=true)
    List<Object[]> findByDeliverIdInEdit(int deliverId);

}
