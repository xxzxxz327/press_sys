package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Advice;
import com.xxzxxz.press.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MessageRepository extends JpaRepository<Message,Integer> {

    @Query(value = "select m.id,m.from_id,m.to_id,m.content,m.time,m.status,m.title,d.depart_name from message as m left join department as d on m.from_id=d.id where status='0' and to_id=?1",nativeQuery = true)
    List<Object[]> findListByToIdUnRead(int to_id);
    @Query(value = "select m.id,m.from_id,m.to_id,m.content,m.time,m.status,m.title,d.depart_name from message as m left join department as d on m.from_id=d.id where status='1' and to_id=?1",nativeQuery = true)
    List<Object[]> findListByToIdRead(int to_id);
    Message findById(int id);

    void deleteById(int id);

}
