package com.xxzxxz.press.repository;

import java.util.Date;
import java.util.List;

import com.xxzxxz.press.model.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;






public interface PageInfoRepository extends JpaRepository<PageInfo,Integer> {
	@Query("select p from PageInfo p")
	Page<PageInfo>findList(Pageable pageable);

	@Query(value = "select * from page_info where date=date(now())",nativeQuery = true)
	Page<PageInfo>findListToday(Pageable pageable);
	PageInfo findById(int id);
	PageInfo save(PageInfo pageInfo);

	void deleteById(int id) ;


}
