package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Orders;
import com.xxzxxz.press.model.vo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface OrdersRepository extends JpaRepository<Orders,Integer> {

    @Query(value = "select c.id,c.name from consumer as c",nativeQuery = true)
    List<Object[]> findConsumerNames();

    @Query("select o from Orders o")
    Page<Orders> findList(Pageable pageable);

    @Query(value = "select o.id,c.name,p.press_name,o.status,o.order_type,o.copies,o.price,o.unsub,o.changeable,o.end_date from (orders as o left join consumer as c on o.consumer_id=c.id)left join press_info as p on o.press_id=p.id where o.station_id=?1",countName = "select count(id) from orders where station_id=?1",
            countQuery = "select count(o.id) from (orders as o left join consumer as c on o.consumer_id=c.id)left join press_info as p on o.press_id=p.id where o.station_id=?1",nativeQuery = true)
    Page<Object[]> findListByStationId(int id,Pageable pageable);

    @Query(value = "select o.id,c.name,p.press_name,o.status,o.order_type,o.copies,o.price from (orders as o left join consumer as c on o.consumer_id=c.id)left join press_info as p on o.press_id=p.id where o.status=0",
            countQuery = "select count(o.id) from (orders as o left join consumer as c on o.consumer_id=c.id)left join press_info as p on o.press_id=p.id where o.status=0",nativeQuery = true)
    Page<Object[]> findNotPay(Pageable pageable);

    @Query(value = "select o.id,c.name,p.press_name,o.status,o.order_type,o.copies,o.price,o.unsub,o.changeable,o.end_date,o.price_longer from (orders as o left join consumer as c on o.consumer_id=c.id)left join press_info as p on o.press_id=p.id where c.id=?1 and o.station_id=?2",
            countQuery = "select count(o.id) from (orders as o left join consumer as c on o.consumer_id=c.id)left join press_info as p on o.press_id=p.id where c.id=?1 and o.station_id=?2",nativeQuery = true)
    Page<Object[]> findOrdersByConsumerId(int id,int stationId,Pageable pageable);

    @Query(value = "select * from orders where consumer_id=?1",nativeQuery = true)
    List<Orders> findOrdersByConsumerId(int id);

    @Query(value="select o.id,c.name,p.press_name,o.status,o.order_type,o.copies,o.price,o.unsub,o.changeable,o.end_date,o.price_longer from (orders as o left join consumer as c on o.consumer_id=c.id)left join press_info as p on o.press_id=p.id where c.id=?1 and o.status=?2 and o.station_id=?3",
            countQuery = "select count(o.id) from (orders as o left join consumer as c on o.consumer_id=c.id)left join press_info as p on o.press_id=p.id where c.id=?1 and o.status=?2,o.station_id=?3",nativeQuery = true)
    Page<Object[]> findByConsumerIdAndStatus(int id,int status,int stationId,Pageable pageable);

    @Query(value = "select o.id,c.name,p.press_name,o.status,o.order_type,o.copies,o.price,o.unsub,o.changeable,o.end_date,o.price_longer from (orders as o left join consumer as c on o.consumer_id=c.id)left join press_info as p on o.press_id=p.id where status=?1 and o.station_id=?2",
            countQuery = "select count(o.id) from (orders as o left join consumer as c on o.consumer_id=c.id)left join press_info as p on o.press_id=p.id where status=?1 and o.station_id=?2",nativeQuery = true)
    Page<Object[]> findByStatus(int status,int station_id,Pageable pageable);
    void deleteById(int id);

    @Query(value = "select * from orders where end_date<?1",
            nativeQuery = true)
    Page<Orders> findListByEndDate(Date date,Pageable pageable);

    //年统计
    @Query(value = "select 1 month,0 count from orders  right join station  on orders.station_id=station.id where not exists(select month(pay_date) as month,sum(copies) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where YEAR(pay_date)=year(now()) and status not in (0,3) " +
            " group by month order by month ASC) " +
            "union all " +
            "select month(pay_date) as month,sum(copies) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where YEAR(pay_date)=year(now()) and status not in (0,3)" +
            " group by month order by month ASC",nativeQuery = true)
    List<Order_monthcount> YearMap();



    //分站月统计
    @Query(value = "select 1 month,0 count from orders  right join station  on orders.station_id=station.id where not exists(select month(pay_date) as month,sum(copies) as count " +
            " from orders right join station on orders.station_id=station.id " +
            " where year(pay_date)=year(now()) and station.station_name=?1 and status not in (0,3) " +
            " group by month order by month ASC) " +
            "union all " +
            "select month(pay_date) as month,sum(copies) as count " +
            " from orders right join station on orders.station_id=station.id " +
            " where year(pay_date)=year(now()) and station.station_name=?1 and status not in (0,3)" +
            " group by month order by month ASC",nativeQuery = true)
    List<Order_monthcount> MonthCount(String station_name);



    //分站年统计
    @Query(value = "select 2019 year,0 count from orders  right join station  on orders.station_id=station.id where not exists(select year(xia_time) as year,sum(price) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where station.station_name=?1 and status not in (0,3) group by year order by year DESC)" +
            " union all " +
            " select year(xia_time) as year,sum(price) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where station.station_name=?1 and status not in (0,3) group by year order by year DESC",nativeQuery = true)
    List<Order_yearcount> YearPrice(String station_name);


    //总统计
    @Query(value = "select 0 stationId, 0 stationName,0 count from orders  right join station  on orders.station_id=station.id where not exists(select station.id as stationId,station.station_name as stationName,sum(copies) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where YEAR (pay_date)=YEAR (now()) and status not in (0,3) " +
            "  group by station.id)" +
            " union all " +
            "select station.id as stationId,station.station_name as stationName,sum(copies) as count" +
            " from orders  right join station  on orders.station_id=station.id " +
            " where YEAR (pay_date)=YEAR (now()) and status not in (0,3) group by stationId",nativeQuery = true)
    List<Object> TotalCount();



    //日志总统计
    //分站日志总统计
    @Query(value = "select 0 stationId, 0 stationName,0 count from orders  right join station  on orders.station_id=station.id where not exists(select station.id as stationId,station.station_name as stationName,sum(copies) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where pay_date=?1 and status not in (0,3) " +
            " group by station.id)" +
            " union all " +
            " select station.id as stationId,station.station_name as stationName,sum(copies) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where pay_date=?1 and status not in (0,3) " +
            " group by station.id",nativeQuery = true)
    List<Object> TotalDayCount(String date);


    //各分站销量日志
    @Query(value = "select ifnull(sum(copies),0) as count,ifnull(sum(price),0) as price from orders  right join station  on orders.station_id=station.id\n" +
            " where pay_date=?2 and status not in (0,3) and station.station_name=?1",nativeQuery = true)
    List<Order_stationDayCount> StationDayCount(String station_name,String logtime);


    //各分站日续订量日志
    @Query(value = "select ifnull(sum(copies),0) as copies,ifnull(sum(price_longer),0) as price\n" +
            " from orders  right join station  on orders.station_id=station.id\n" +
            " where pay_date=?2 and status=6 and station.station_name=?1 ",nativeQuery = true)
    List<Order_stationdaytotalpost> StationDayTotalPost(String station_name,String logtime);



    //每日汇总
    @Query( value = " select ifnull(sum(copies),0) as count,ifnull(sum(price),0) as sum from orders  right join station  on orders.station_id=station.id\n" +
            " where to_days(pay_date)=TO_DAYS(now()) and status not in (0,3) ",nativeQuery = true)
    List<Day> DayCount();



    //每日续订汇总
    @Query(value = " select ifnull(sum(copies),0) as count,ifnull(sum(price_longer),0) as sum " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where to_days(pay_date)=TO_DAYS(now()) and status=6 ",nativeQuery = true)
    List<Day> DayTotalPost();




    //总价格
    @Query(value = "select 0 stationId, 0 stationName,0.0 sum from orders  right join station  on orders.station_id=station.id where not exists(select station.id as stationId,station.station_name as stationName,sum(price) as sum " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where YEAR (pay_date)= YEAR (now()) and status not in (0,3) " +
            " group by station.id)" +
            " union all" +
            " select station.id as stationId,station.station_name as stationName,sum(price) as sum " +
            "  from orders  right join station  on orders.station_id=station.id " +
            " where YEAR (pay_date)= YEAR (now()) and status not in (0,3) " +
            "  group by station.id ",nativeQuery = true)
    List<Object> TotalPrice();



    //分站月销金额
    @Query(value = "select 1 month,0.0 count from orders  right join station  on orders.station_id=station.id where not exists(select month(pay_date) as month,sum(price) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where year(pay_date)=year(now()) and station.station_name=?1 and status not in (0,3) "+
            " group by month order by month ASC) " +
            " union all " +
            " select month(pay_date) as month,sum(price) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            "  where year(pay_date)=year(now()) and station.station_name=?1 and status not in (0,3) " +
            " group by month order by month ASC",nativeQuery = true)
    List<Order_monthcount> MonthPrice(String station_name);



    //分站年销量
    @Query(value = "select 2019 year,0 count from orders  right join station  on orders.station_id=station.id where not exists(select year(xia_time) as year,sum(copies) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where station.station_name=?1 and status not in (0,3) group by year order by year DESC)" +
            " union all " +
            " select year(xia_time) as year,sum(copies) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where station.station_name=?1 and status not in (0,3) group by year order by year DESC",nativeQuery = true)
    List<Order_yearcount> YearCount(String station_name);



    //订阅订购对比
    @Query(value = "select 0 stationId, 0 stationName,0 count,0.0 price from orders  right join station  on orders.station_id=station.id where not exists(select station.id as stationId,station.station_name as stationName,sum(copies) as count,sum(price) as price " +
            " from orders  right join station  on orders.station_id=station.id  where year(pay_date)=year(now()) and status not in (0,3))" +
            " union all " +
            " select station.id as stationId,station.station_name as stationName,sum(copies) as count,sum(price) as price " +
            " from orders  right join station  on orders.station_id=station.id  where year(pay_date)=year(now()) and status not in (0,3) ",nativeQuery = true)
    List<Object> Contrast();



    //历年销量
    @Query(value = "select 2019 year,0 count from orders  right join station  on orders.station_id=station.id where not exists(select year(xia_time) as year,sum(copies) as count " +
            " from orders  right join station  on orders.station_id=station.id where status not in (0,3) group by year order by year DESC)" +
            " union all " +
            " select year(xia_time) as year,sum(copies) as count " +
            " from orders  right join station  on orders.station_id=station.id where status not in (0,3) group by year order by year DESC ",nativeQuery = true)
    List<Order_yearcount> HistoryCount();



    //月结算
//总统计
    @Query(value = "(select 0 stationId, 0 stationName,0 count from orders  right join station on orders.station_id=station.id where not exists(select station.id as stationId,station.station_name as stationName,sum(copies) as count " +
            " from orders  right join station  on orders.station_id=station.id " +
            " where month(pay_date)=month(now()) and status not in (0,3)) " +
            " group by stationId)" +
            " union all" +
            " (select station.id as stationId,station.station_name as stationName,sum(copies) as count " +
            " from orders right join station  on orders.station_id=station.id " +
            " where month(pay_date)=month(now()) and status not in (0,3) )",nativeQuery = true)
    List<Object> TotalMonthCount();



    //各分站月销量
    @Query(value = "select 0 count,0.0 sum from orders  right join station  on orders.station_id=station.id where not exists(select sum(copies) as count,sum(price) as sum from orders  right join station  on orders.station_id=station.id " +
            " where month(pay_date)=month(now()) and status not in (0,3) and station.station_name=?1)" +
            " union all " +
            " select sum(copies) as count,sum(price) as sum from orders  right join station  on orders.station_id=station.id " +
            " where month(pay_date)=month(now()) and status not in (0,3) and station.station_name=?1 " ,nativeQuery = true)
    List<Day> StationMonthCount(String station_name);



    //各分站月续订量
    @Query(value = "select 0 count,0.0 sum from orders  right join station  on orders.station_id=station.id where not exists(select sum(copies) as count,sum(price_longer) as sum " +
            " from orders  right join station  on station_id=station.id " +
            " where month(pay_date)=month(now()) and status=6 and station.station_name=?1 " +
            " group by station.id)" +
            " union all " +
            " select sum(copies) as count,sum(price_longer) as sum " +
            " from orders  right join station  on station_id=station.id " +
            " where month(pay_date)=month(now()) and status=6 and station.station_name=?1 " +
            " group by station.id ",nativeQuery = true)
    List<Day> StationMonthTotalPost(String station_name);



    //掉单原因
    @Query(value = "select 0 unsubReason,0 count from orders join change_reason on orders.unsub_reason=change_reason.id  where not exists(select change_reason.reason_name as unsubReason,count(orders.id) as count " +
            " from orders join change_reason on orders.unsub_reason=change_reason.id  " +
            " where orders.status=3 group by unsubReason)" +
            " union all " +
            " select change_reason.reason_name as unsubReason,count(orders.id) as count " +
            " from orders join change_reason on orders.unsub_reason=change_reason.id" +
            " where orders.status=3 group by unsubReason ",nativeQuery = true)
    List<Object>unsubreason();


    //投递到期
    @Query(value = "select 0 stationId, 0 stationName,0 count from dual where not exists(select station.id as stationId,station.station_name as stationName,count(orders.id) as count " +
            " from orders  right join station  on orders.station_id=station.id  " +
            " where TO_DAYS(end_date)=TO_DAYS(now()) group by station.id)" +
            " union all " +
            " select station.id as stationId,station.station_name as stationName,count(orders.id) as count " +
            " from orders  right join station  on orders.station_id=station.id  " +
            " where TO_DAYS(end_date)=TO_DAYS(now()) group by station.id ",nativeQuery = true)
    List<Object>EndedDelivery();


    //以下是querycuorder使用
    @Query(value="select o.id,o.consumer_id as consumerId,c.name,o.press_id,p.press_name,o.copies,o.xia_time from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and o.consumer_id=?1 order by consumerId",countQuery="select count(o.consumer_id) from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and o.consumer_id=?1 order by o.consumer_id",nativeQuery = true)
    Page<Object[]> findByConsumerIdShow(int consumerId,Pageable pageable);

    @Query(value="select o.id,o.consumer_id as consumerId,c.name,o.press_id,p.press_name,o.copies,o.xia_time from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id order by consumerId",countQuery="select count(o.consumer_id) from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id order by o.consumer_id",nativeQuery = true)
    Page<Object[]> findAllShow(Pageable pageable);

    @Query(value="select o.id,o.consumer_id as consumerId,c.name,o.press_id,p.press_name,o.copies,o.xia_time from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and o.xia_time between ?1 and ?2 order by consumerId",countQuery="select count(o.consumer_id) from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and o.xia_time between ?1 and ?2 order by o.consumer_id",nativeQuery = true)
    Page<Object[]> findByTimeShow(String startTime,String endTime,Pageable pageable);

    @Query(value="select o.id,o.consumer_id as consumerId,c.name,o.press_id,p.press_name,o.copies,o.xia_time from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and o.consumer_id=?1 and o.xia_time between ?2 and ?3 order by consumerId",countQuery="select count(o.consumer_id) from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and o.consumer_id=?1 and o.xia_time between ?2 and ?3 order by o.consumer_id",nativeQuery = true)
    Page<Object[]> findByConsumerIdAndTime(int consumerId,String startTime,String endTime,Pageable pageable);


    //以下是queryorders使用
    @Query(value="select o.id,c.name,p.press_name,o.copies,o.consumer_id as consumerId from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and o.id=?1 order by consumerId asc",countQuery="select count(o.consumer_id) from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and o.id=?1 order by consumer_id asc",nativeQuery = true)
    Page<Object[]> findOrdersByOrderId(int orderId,Pageable pageable);

    @Query(value="select o.id,c.name,p.press_name,o.copies,o.consumer_id as consumerId from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and c.name=?1 order by consumerId asc",countQuery="select count(o.consumer_id) from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and c.name=?1 order by consumer_id asc",nativeQuery = true)
    Page<Object[]> findOrdersByConsumerName(String consumerName,Pageable pageable);

    @Query(value="select o.id,c.name,p.press_name,o.copies,o.consumer_id as consumerId from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and p.press_name=?1 order by consumerId asc",countQuery="select count(o.consumer_id) from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and p.press_name=?1 order by consumer_id asc",nativeQuery = true)
    Page<Object[]> findOrderByPressName(String pressName,Pageable pageable);

    @Query(value="select o.id,c.name,p.press_name,o.copies,o.consumer_id as consumerId from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and p.press_name=?1 and c.name=?2 order by consumerId asc",countQuery="select count(o.consumer_id) from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and p.press_name=?1 and c.name=?2 order by o.consumer_id asc",nativeQuery = true)
    Page<Object[]> findOrderByPressNameAndConsumerName(String pressName,String consumerName,Pageable pageable);

    @Query(value="select o.id,c.name,p.press_name,o.copies,o.consumer_id as consumerId from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id order by o.id asc",countQuery="select count(o.id) from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id order by o.id asc",nativeQuery = true)
    Page<Object[]> findOrderAll(Pageable pageable);


    //以下是querymonthcharts使用


    @Query(value="select s.id,"+
            "(select count(o.id)  from (select * from orders where orders.xia_time between ?1 and ?2 ) as o where  o.station_id=s.id and o.status>=0 and o.status<=2   group by o.station_id),"+
            "(select count(o1.id)  from (select * from orders where orders.xia_time between ?1 and ?2 ) as o1 where  o1.status=5 and o1.station_id=s.id group by o1.station_id) ,"+
            "(select count(o2.id)  from (select * from orders where orders.xia_time between ?1 and ?2 ) as o2 where   o2.status=4 and o2.station_id=s.id group by o2.station_id),"+
            "(select count(o3.id) from (select * from orders where orders.xia_time between ?1 and ?2 ) as o3 where  o3.status=3 and o3.station_id=s.id group by o3.station_id),"+
            "s.station_name from station as s where s.id>5 order by s.id"
            ,countQuery="select count(id) from station where station.id>5 order by id",nativeQuery=true)
    Page<Object[]> findMCByTime(String startTime,String endTime,Pageable pageable);

    @Query(value="select o.id ,s.station_name,o.price,o.status,o.xia_time,c.name,o.station_id  from (orders as o left join station as s on o.station_id=s.id) left join consumer as c on o.consumer_id=c.id order by o.id asc",countQuery = "select count(o.id) from orders as o left join station as s on o.station_id=s.id order by o.id asc",nativeQuery = true)
    Page<Object[]> findallorderstationname(Pageable pageable);
    @Query(value="select o.id ,s.station_name,o.price,o.status,o.xia_time,c.name,o.station_id  from (orders as o left join station as s on o.station_id=s.id) left join consumer as c on o.consumer_id=c.id where s.station_name=?1 order by o.id asc",countQuery = "select count(o.id) from orders as o left join station as s on o.station_id=s.id order by o.id asc",nativeQuery = true)
    Page<Object[]>findAllByStationId(String id, Pageable pageable);

    @Query(value="select s.station_name from station as s",nativeQuery = true)
    List<String>findstationname();



    //分配投递段
    @Query
            (nativeQuery=true,value="select o.id,c.name,c.phone,o.address,p.press_name,d.deliverpart_id,d.id as did "
                    + "from (orders as o left join deliver as d on d.order_id=o.id)  "
                    + "left join consumer as c on o.consumer_id=c.id left join press_info as p on o.press_id=p.id "
                    + "where o.station_id=?1 "
                    + "order by o.id",
                    countQuery="select count(o.id) from orders as o where o.station_id=?1")
    Page<Object[]> findAllorder(int sid,Pageable pageable);
    //	@Query
//	(nativeQuery=true,value="select o.id,c.name,c.phone,o.address,p.press_name,d.deliverpart_id,d.id as did from (orders as o left join deliver as d on d.order_id=o.id)  left join consumer as c on o.consumer_id=c.id left join press_info as p on o.press_id=p.id where d.deliverpart_id is null order by o.id",countQuery="select count(o.id) from orders as o left join deliver as d on d.order_id=o.id where d.deliverpart_id is null")
//	Page<Object[]> findallnullorder(Pageable pageable);
    @Query
            (nativeQuery=true,value="select o.id,c.name,c.phone,o.address,p.press_name,d.deliverpart_id,d.id as did "
                    + "from (orders as o left join deliver as d on d.order_id=o.id) "
                    + "left join consumer as c on o.consumer_id=c.id left join press_info as p on o.press_id=p.id "
                    + "where c.name = ?1 and o.station_id=?2 order by o.id",
                    countQuery="select count(o.id) from orders as o where c.name = ?1 and o.station_id=?2")
    Page<Object[]> findorderBycName(String cname,int sid,Pageable pageable);

    Orders findById(int id);


    //统计查询用
    @Query
            (nativeQuery=true,
                    value="SELECT sum1,sum2"
                            + " FROM (SELECT SUM(orders.price) AS sum1"
                            + " FROM orders"
                            + " WHERE station_id=?1 AND orders.xia_time BETWEEN ?2 and ?3 AND `status`=1) as a,"
                            + " (SELECT SUM(orders.price) AS sum2"
                            + " FROM orders"
                            + " WHERE station_id=?1 AND orders.xia_time BETWEEN ?2 and ?3 AND `status`=3) AS b")
    List<Object[]> findtongjiorder(int sId,Date sDate,Date eDate);
    //日常业务查询
    @Query(nativeQuery=true,
            value="SELECT orders.id,orders.price,staff.staff_name,consumer.`name`,orders.`status` "
                    + "FROM (orders LEFT JOIN staff ON orders.solicit_id=staff.id)LEFT JOIN consumer on consumer.id=orders.consumer_id "
                    + "WHERE orders.station_id=?1 AND (orders.`status`=1 or orders.`status`=2)And consumer.name=?2")
    Page<Object[]> findoneorderpay(int sid,String cName,Pageable pageable);

    @Query(nativeQuery=true,
            value="SELECT orders.id,orders.price,staff.staff_name,consumer.`name`,orders.`status` "
                    + "FROM (orders LEFT JOIN staff ON orders.solicit_id=staff.id)LEFT JOIN consumer on consumer.id=orders.consumer_id "
                    + "WHERE orders.station_id=?1 AND (orders.`status`=1 or orders.`status`=2)")
    Page<Object[]> findorderpay(int sid,Pageable pageable);


    //打印催款单
    @Query
            (nativeQuery=true,value="select o.id,o.price,c.name,c.phone,c.address,p.press_name,o.copies,o.xia_time,o.start_date,o.end_date "
                    + "from (orders as o left join consumer as c on o.consumer_id=c.id) "
                    + "left join press_info as p on o.press_id=p.id "
                    + "where o.station_id=?1 and o.status=0 "
                    + "order by o.id",
                    countQuery="select count(o.id) from orders as o where o.station_id=?1 and o.status=0")
    Page<Object[]> findAllrepay(int sid,Pageable pageable);

    @Query
            (nativeQuery=true,value="select o.id,o.price,c.name,c.phone,c.address,p.press_name,o.copies,o.xia_time,o.start_date,o.end_date "
                    + "from (orders as o left join consumer as c on o.consumer_id=c.id) "
                    + "left join press_info as p on o.press_id=p.id "
                    + "where o.station_id=?2 and o.status=0 and c.name=?1 ",
                    countQuery="select count(o.id)"
                            + "from (orders as o left join consumer as c on o.consumer_id=c.id) "
                            + "left join press_info as p on o.press_id=p.id "
                            + "where o.station_id=?2 and o.status=0 and c.name=?1")
    Page<Object[]> findrepayBycName(String cName,int sid,Pageable pageable);

    @Query
            (nativeQuery=true,value="select o.id,o.price,c.name,c.phone,c.address,p.press_name,o.copies,o.xia_time,o.start_date,o.end_date "
                    + "from (orders as o left join consumer as c on o.consumer_id=c.id) "
                    + "left join press_info as p on o.press_id=p.id "
                    + "where o.station_id=?2 and o.status=0 and o.id=?1 ",
                    countQuery="select count(o.id)"
                            + "from (orders as o left join consumer as c on o.consumer_id=c.id) "
                            + "left join press_info as p on o.press_id=p.id "
                            + "where o.station_id=?2 and o.status=0 and o.id=?1")
    Page<Object[]> findrepayByid(int id,int sid,Pageable pageable);

    //姓名模糊输入框
    @Query(nativeQuery=true,value="select consumer.name from consumer")
    List<Object[]> showallconsumerName();


}
