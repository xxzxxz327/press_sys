package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Consumer;
import com.xxzxxz.press.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;


public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {
    @Query(value="select i.id,i.station_id,p.press_name,c.name,o.price,i.status,i.type from (invoice as i left join \n" +
            "(orders as o left join consumer as c on o.consumer_id=c.id) on o.id=i.order_id) left join press_info as p on p.id=o.press_id",
            countQuery="select count(i.id)from (invoice as i left join \n" +
                    "(orders as o left join consumer as c on o.consumer_id=c.id) on o.id=i.order_id) left join press_info as p on p.id=o.press_id order by i.id desc",nativeQuery=true)
    Page<Object[]> findList(Pageable pageable);
    @Query(value="select i.id,i.station_id,p.press_name,c.name,o.price,i.status,i.type from (invoice as i left join (orders as o left join consumer as c on o.consumer_id=c.id) on o.id=i.order_id) left join press_info as p on p.id=o.press_id where i.station_id=?1 and i.status=?2 order by i.id desc",
            countQuery="select count(i.id) from (invoice as i left join (orders as o left join consumer as c on o.consumer_id=c.id) on o.id=i.order_id) left join press_info as p on p.id=o.press_id where i.status=?1 and i.status=?2",nativeQuery=true)
    Page<Object[]> findListByStationIdAndStatus(int stationId, int status, Pageable pageable);

    @Query(value="select i.id,i.station_id,p.press_name,c.name,o.price,i.status,i.type from (invoice as i left join (orders as o left join consumer as c on o.consumer_id=c.id) on o.id=i.order_id) left join press_info as p on p.id=o.press_id where i.station_id=?1 order by i.id desc",
            countQuery="select count(i.id) from (invoice as i left join (orders as o left join consumer as c on o.consumer_id=c.id) on o.id=i.order_id) left join press_info as p on p.id=o.press_id where i.status=?1",nativeQuery=true)
    Page<Object[]> findListByStationId(int stationId, Pageable pageable);
    void deleteById(int id);
    Invoice findById(int id);

    /*@Query("select i from Invoice i")
    Page<Invoice> findList1(Pageable pageable);*/

    @Query(value="select i.id , i.type , i.status , s.station_name , i.order_id from invoice as i left join station as s on i.station_id = s.id where i.id =?1",nativeQuery = true)
    Page<Object[]> findById(int id, Pageable pageable);
    @Query(value="select i.id ,i.status,c.name,o.price,c.phone from (invoice as i left join orders as o on i.order_id = o.id ) left join consumer as c   on o.consumer_id = c.id  where i.id =?1",nativeQuery = true)
    List<Object[]> findById1(int id);



    @Query(value="select s.station_name ,sum(case when i.type = 0 then 1 else 0 end),sum(case when i.type = 1 then 1 else 0 end),sum(case when i.type = 2 then 1 else 0 end) from invoice as i left join station as s on i.station_id=s.id where i.station_id is not null group by i.station_id",countQuery = "select count(s.id) from invoice as i left join station as s on i.station_id=s.id order by s.id asc",nativeQuery = true)
    Page<Object[]> findallstationinvoice(Pageable pageable);

    @Query(value="select count(i.id) from invoice as i where i.type =?1 and i.station_id is null ",nativeQuery = true)
    int findinvoicenum(int type);

    @Query(value="select  * from invoice as i where i.type =?1 and i.station_id is null limit 0,1",nativeQuery = true)
    Invoice getoneinvoice(int type);
    @Query(value="select s.station_name ,sum(case when i.type=0   then 1 else 0 end),sum(case when i.type=1 then 1 else 0 end),sum(case when i.type=2 then 1 else 0 end) from invoice as i right join station as s on i.station_id=s.id  group by s.id",
            countQuery = "select count(s.station_name) from invoice as i right join station as s on i.station_id=s.id  group by s.id",
            nativeQuery = true)
    Page<Object[]> getstationinvoice(Pageable pageable);
    @Query(value="select i.id,i.type,i.station_id,s.station_name from invoice as i left join station  as s on i.station_id = s.id where i.status=4",nativeQuery = true)
    Page<Object[]> getbackinvoice(Pageable pageable);

    @Query
            (nativeQuery=true,
                    value="SELECT invoice.type,sum(case WHEN invoice.`status`=0 THEN 1 ELSE 0 END)as sum0,"
                            + "sum(case WHEN invoice.`status`=1 THEN 1 ELSE 0 END)as sum1,"
                            + "sum(case WHEN invoice.`status`=2 THEN 1 ELSE 0 END)as sum2,"
                            + "sum(case WHEN invoice.`status`=3 THEN 1 ELSE 0 END)as sum3,"
                            + " sum(case WHEN invoice.`status`=4 THEN 1 ELSE 0 END)as sum4,count(*) AS sum5"
                            + " FROM invoice"
                            + " WHERE invoice.station_id=?1"
                            + " GROUP BY invoice.type")
    List<Object[]> findShowInvoice(int sId);
}
