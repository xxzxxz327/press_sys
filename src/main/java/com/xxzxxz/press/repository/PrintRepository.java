package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Advice;

import com.xxzxxz.press.model.Print;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PrintRepository extends JpaRepository<Print,Integer> {
   @Query("select p from Print p")
    List<Print> findList();

    @Query("select p from Print p")
    Page<Print> findList(Pageable pageable);

    Print findByName(String name);
    Print findById( int id);
    Print save (Print print);

    @Query(value = "select * from print where id=?1",countQuery = "select count(id) from print where id=?1 ",nativeQuery = true)
    Page<Print> findById1(int id,Pageable pageable);

    void deleteById(int id) ;
}
