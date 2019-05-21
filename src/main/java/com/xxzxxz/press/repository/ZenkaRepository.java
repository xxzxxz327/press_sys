package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Consumer;
import com.xxzxxz.press.model.Zenka;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ZenkaRepository extends JpaRepository<Zenka,Integer> {
    @Query(value = "select * from zenka where zenka.id != '-1'",nativeQuery = true)
    List<Zenka> findList();
    Zenka findById(int id);
    void deleteById(int id);
    Page<Zenka> findById(int id,Pageable pageable);
    @Query(value = "select * from zenka where duration=?1 and flag=?2",nativeQuery = true)
    Page<Zenka> findByDurationAndFlag(int duration,int flag,Pageable pageable);
    @Query(value = "select * from zenka where zenka.id != '-1'",nativeQuery = true)
    Page<Zenka> findAll(Pageable pageable);
    @Query(value = "select * from zenka where zenka.id!=-1 and flag=?1",nativeQuery = true)
    Page<Zenka> findListByFlag(int flag,Pageable pageable);

    @Query(value = "select * from zenka where flag=0 and id!=-1",nativeQuery = true)
    List<Zenka> findListByFlagUnuse();
}
