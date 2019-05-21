package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Orders;
import com.xxzxxz.press.model.PressInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PressInfoRepository extends JpaRepository<PressInfo,Integer> {
    @Query("select o from PressInfo o")
    Page<PressInfo> findList(Pageable pageable);
    PressInfo findById(int id);
    void deleteById(int id);

    @Query("select  p from PressInfo p")
    List<PressInfo> findList();
}
