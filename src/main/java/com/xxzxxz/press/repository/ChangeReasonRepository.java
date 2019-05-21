package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.ChangeReason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ChangeReasonRepository extends JpaRepository<ChangeReason,Integer> {
    @Query(value = "select * from change_reason where status=?1",nativeQuery = true)
    List<ChangeReason> findListByStatus(int status);
    ChangeReason findById(int id);
    void deleteById(int id);
}
