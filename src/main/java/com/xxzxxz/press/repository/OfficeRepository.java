package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.OfficeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OfficeRepository extends JpaRepository<OfficeInfo,Integer>{
	Page<OfficeInfo> findById(int id, Pageable pageable);
	Page<OfficeInfo> findByOfficeName(String officeName, Pageable pageable);
	Page<OfficeInfo> findAll(Pageable pageable);
	
	OfficeInfo findById(int id);
	OfficeInfo findByOfficeName(String officeName);

	@Query(value="SELECT * FROM office_info",nativeQuery=true)
	List<OfficeInfo> findList();


}
