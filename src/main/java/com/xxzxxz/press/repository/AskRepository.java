package com.xxzxxz.press.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.xxzxxz.press.model.Ask;

public interface AskRepository extends JpaRepository<Ask,Integer>{
	
	Page<Ask> findAll(Pageable pageable);
	
	Ask findById(int id);

}
