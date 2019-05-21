package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Consumer;
import com.xxzxxz.press.model.OrdersBackup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OrdersBackupRepository extends JpaRepository<OrdersBackup,Integer> {
    OrdersBackup findById(int id);
    void deleteById(int id);
}
