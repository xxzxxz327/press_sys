package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Consumer;
import com.xxzxxz.press.model.vo.ConsumerVo;
import com.xxzxxz.press.model.vo.Consumer_age;
import com.xxzxxz.press.model.vo.Consumer_frequency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ConsumerRepository extends JpaRepository<Consumer,Integer> {
    @Query("select c from Consumer c")
    Page<Consumer> findList(Pageable pageable);
    Consumer findByName(String name);
    void deleteById(int id);

    @Query(value = "select c.id,c.name from consumer as c where c.id=?1",nativeQuery = true)
    List<Object[]> findNameById(int id);

    @Query(value = "select * from consumer where id=?1",nativeQuery = true)
    Page<Consumer> findListById(int id, Pageable pageable);

    @Query(value = "select c.id,c.name from consumer as c",nativeQuery = true)
    List<Object[]> findNames();

    //性别
    @Query("select c.sex as sex,count(c.id) as count from Consumer c group by c.sex")
    List<ConsumerVo> ConsumerSex();


    //语言
    @Query("select c.language as language,count(c.id) as count from Consumer c group by c.language")
    List<ConsumerVo> ConsumerLan();


    //职业
    @Query("select c.occupation as occupation,count(c.id) as count from Consumer c group by c.occupation")
    List<ConsumerVo> ConsumerOccupation();


    //省份
    @Query("select c.province as province,count(c.id) as count from Consumer c group by c.province")
    List<ConsumerVo> ConsumerProvince();


    //受教育程度
    @Query("select c.degree as degree,count(c.id) as count from Consumer c group by c.degree")
    List<ConsumerVo> ConsumerDegree();


    //类别
    @Query("select c.level as level,count(c.id) as count from Consumer c group by c.level order by level")
    List<ConsumerVo> ConsumerType();


    //年龄段
    @Query(value = "select a.ageratio as ageratio,count(*) as count from (select  case when age>=0 and age<20 then '20以下' " +
            " when age>=20 and age<40 then '20-40' " +
            " when age>=40 and age<60 then '40-60' " +
            " when age>=60 then '60以上' " +
            " END AS ageratio from consumer )a " +
            " group by ageratio",nativeQuery = true)
    List<Consumer_age> ConsumerAge();


    //订期
    @Query(value = "select c.duration as duration,sum(c.count) as count,sum(c.sum) as sum from(" +
            "select interval (duration,0,3,5,13,25,53,1000) as duration," +
            "             sum(copies) as count,count(distinct consumer_id) as sum" +
            "             from orders group by duration ) as c group by duration order by duration asc;" ,nativeQuery = true)
    List<Consumer_frequency>Frequency();


    Page<Consumer> findById(int id,Pageable pageable);
    Page<Consumer> findByName(String name,Pageable pageable);
    Page<Consumer> findAll(Pageable pageable);

    Consumer findById(int id);

    @Query(value="select o.id,c.id as cid,c.name,p.id as pid,p.press_name from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and c.id=?1 order by o.id",countQuery="select count(o.id) from orders as o,consumer as c,press_info as p where o.consumer_id=c.id and o.press_id=p.id and c.id=?1 order by o.id",nativeQuery=true)
    Page<Object[]> findconordersitemByconId(int conId,Pageable pageable);

    @Query(value="select  c.name ,o.id , o.price , o.status , o.pay_type , o.xia_time from orders as o left join consumer as c on o.consumer_id =c.id where c.level=3 order by c.id ",countQuery ="select count(o.id) from orders as o left join consumer as c on o.consumer_id=c.id order by o.id asc",nativeQuery = true)
    Page<Object[]> findallconsumerorder(Pageable pageable);
    @Query(value="select  c.name ,o.id , o.price , o.status , o.pay_type , o.xia_time from orders as o left join consumer as c on o.consumer_id =c.id  where  c.name=?1 and c.level=3 order by o.id asc",countQuery = "select count(o.id) from orders as o left join consumer as c on o.consumer_id =c.id  where c.id=?1 and c.level=3 order by o.id asc",nativeQuery = true)
    Page<Object[]>findbigconsumerbycid(String id, Pageable pageable);
}
