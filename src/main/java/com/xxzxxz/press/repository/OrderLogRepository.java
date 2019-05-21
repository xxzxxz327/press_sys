package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Department;
import com.xxzxxz.press.model.OrderLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OrderLogRepository extends JpaRepository<OrderLog,Integer> {
    @Query(value = "select ol.id,ol.order_id,ol.operation,ol.principal_id,s.staff_name,ol.date from order_log as ol left join staff as s on ol.principal_id=s.id where ol.principal_id=?1 and ol.operation=?2",
            countQuery = "select count(ol.id) from order_log as ol left join staff as s on ol.principal_id=s.id where ol.principal_id=?1 and ol.operation=?2",
            nativeQuery = true)
    Page<Object[]> findListByPrincipalIdAndStatus(int principalId, int operation, Pageable pageable);

    @Query(value = "select ol.id,ol.order_id,ol.operation,ol.principal_id,s.staff_name,ol.date from order_log as ol left join staff as s on ol.principal_id=s.id where ol.principal_id=?1",
            countQuery = "select count(ol.id) from order_log as ol left join staff as s on ol.principal_id=s.id where ol.principal_id=?1",
            nativeQuery = true)
    Page<Object[]> findListByPrincipalId(int principalId,Pageable pageable);

    @Query(value = "select ol.id,ol.order_id,ol.operation,ol.principal_id,s.staff_name,ol.date from order_log as ol left join staff as s on ol.principal_id=s.id where ol.operation=?1",
            countQuery = "select count(ol.id) from order_log as ol left join staff as s on ol.principal_id=s.id where ol.operation=?1",
            nativeQuery = true)
    Page<Object[]> findListByStatus(int operation,Pageable pageable);

    @Query(value = "select ol.id,ol.order_id,ol.operation,ol.principal_id,s.staff_name,ol.date from order_log as ol left join staff as s on ol.principal_id=s.id",
            countQuery = "select count(ol.id) from order_log as ol left join staff as s on ol.principal_id=s.id",
            nativeQuery = true)
    Page<Object[]> findList(Pageable pageable);


}
