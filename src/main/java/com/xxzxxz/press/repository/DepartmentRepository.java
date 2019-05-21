package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Advice;
import com.xxzxxz.press.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface DepartmentRepository extends JpaRepository<Department,Integer> {
    List<Department> findByStationId(int stationId);
    Department findById(int id);
    @Query(value = "select * from department where id!=?1",nativeQuery = true)
    List<Department> findByIdExceptNow(int departId);
}
