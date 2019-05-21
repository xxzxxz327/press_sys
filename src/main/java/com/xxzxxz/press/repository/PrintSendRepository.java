package com.xxzxxz.press.repository;

import java.util.Date;
import java.util.List;

import com.xxzxxz.press.model.PrintSend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.xxzxxz.press.model.PrintSend;



public interface PrintSendRepository extends JpaRepository<PrintSend,Integer> {
	@Query("select ps from PrintSend ps")
	Page<PrintSend>findList(Pageable pageable);

	PrintSend findById(int id);

	PrintSend save(PrintSend printSend);

	void deleteById(int id) ;

	@Query(value="select * from print_send as p where p.date=?1",nativeQuery=true)
	List<PrintSend> findYBByTime(Date date);

}
