package com.xxzxxz.press.repository;


import com.xxzxxz.press.model.transcount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TranscountRepository extends JpaRepository<transcount,Integer>{
    @Query("select t from transcount t")

     void deleteById(int id);
     transcount findById(int id);
     @Query(value ="SELECT t.id,t.base,t.jiazhang FROM trans_count as t order by t.id",nativeQuery = true)
     Page<Object[]> getalltranscount(Pageable pageable);
}
