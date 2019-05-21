package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Complain;
import com.xxzxxz.press.model.Consumer;
import com.xxzxxz.press.model.vo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ComplainRepository extends JpaRepository<Complain,Integer> {

    @Query(value = "select c.id,con.name,p.press_name,c.content,c.status,c.station_id,ct.name as type_name from (((complain as c left join consumer as con on con.id=c.consumer_id) left join orders as o on c.order_id=o.id) left join press_info as p on o.press_id=p.id) left join complain_type as ct on c.type=ct.id where c.station_id=?1",
    countQuery = "select count(c.id) from (((complain as c left join consumer as con on con.id=c.consumer_id) left join orders as o on c.order_id=o.id) left join press_info as p on o.press_id=p.id) left join complain_type as ct on c.type=ct.id where c.station_id=?1",
    nativeQuery = true)
    Page<Object[]> findListByStationId(int stationId,Pageable pageable);
    @Query(value = "select c.id,con.name,p.press_name,c.content,c.status,c.station_id,ct.name as type_name from (((complain as c left join consumer as con on con.id=c.consumer_id) left join orders as o on c.order_id=o.id) left join press_info as p on o.press_id=p.id) left join complain_type as ct on c.type=ct.id where c.station_id=?1 and c.status=?2",
            countQuery = "select count(c.id) from (((complain as c left join consumer as con on con.id=c.consumer_id) left join orders as o on c.order_id=o.id) left join press_info as p on o.press_id=p.id) left join complain_type as ct on c.type=ct.id where c.station_id=?1 and c.status=?2",
            nativeQuery = true)
    Page<Object[]> findListByStationIdAndStatus(int stationId,int status,Pageable pageable);
    void deleteById(int id);
    @Query(value = "select c.id,con.name,p.press_name,c.content,c.status,c.station_id,ct.name as type_name from (((complain as c left join consumer as con on con.id=c.consumer_id) left join orders as o on c.order_id=o.id) left join press_info as p on o.press_id=p.id) left join complain_type as ct on c.type=ct.id where c.id=?1",
    nativeQuery = true)
    List<Object[]> findById1(int id);
    @Query(value="select * from complain where id=?1",nativeQuery = true)
    Complain findById2(int id);

    //内容
    @Query("select c.content from Complain c where c.type=?1")
    List<String>ComplainContent(int type);


    //类型
    @Query("select c.type as type,count(c.id) as count from Complain c group by c.type")
    List<ComplainResultVo>ComplainCountByType();


    @Query(value="select c.id,con.id as conId,con.name as conName,ctype.name as ctypeName,c.content,c.status,con.phone,c.order_id from complain as c,consumer as con,complain_type as ctype where c.consumer_id=con.id and c.type=ctype.id and c.id=?1 order by c.id asc",countQuery="select count(c.id) from complain as c,consumer as con,complain_type as ctype where c.consumer_id=con.id and c.type=ctype.id and c.id=?1 order by c.id asc",nativeQuery=true)
    Page<Object[]> findBycomplainId(int complainId,Pageable pageable);

    @Query(value="select c.id,con.id as conId,con.name as conName,ctype.name as ctypeName,c.content,c.status,con.phone,c.order_id from complain as c,consumer as con,complain_type as ctype where c.consumer_id=con.id and c.type=ctype.id and con.id=?1 order by c.id asc",countQuery="select count(c.id) from complain as c,consumer as con,complain_type as ctype where c.consumer_id=con.id and c.type=ctype.id and con.id=?1 order by c.id asc",nativeQuery=true)
    Page<Object[]> findByconsumerId(int consumerId,Pageable pageable);

    @Query(value="select c.id,con.id as conId,con.name as conName,ctype.name as ctypeName,c.content,c.status,con.phone,c.order_id from complain as c,consumer as con,complain_type as ctype where c.consumer_id=con.id and c.type=ctype.id  order by c.id asc",countQuery="select count(c.id) from complain as c,consumer as con,complain_type as ctype where c.consumer_id=con.id and c.type=ctype.id order by c.id asc",nativeQuery=true)
    Page<Object[]> findAllComplain(Pageable pageable);

    Complain findById(int id);
}
