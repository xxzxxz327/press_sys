package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Transinfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransinfoRepository extends JpaRepository<Transinfo,Integer>{

	Page<Transinfo> findById(int id, Pageable pageable);
	Page<Transinfo> findByName(String name, Pageable pageable);
	Page<Transinfo> findAll(Pageable pageable);
	
	Transinfo findById(int id);
	Transinfo findByName(String name);
}
