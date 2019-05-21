package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Complaintype;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ComplaintypeRepository extends JpaRepository<Complaintype,Integer>{
	
	Page<Complaintype> findAll(Pageable pageable);
	
	Complaintype findById(int id);
	

}
