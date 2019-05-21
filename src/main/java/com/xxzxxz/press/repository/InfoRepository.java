package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Info;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InfoRepository extends JpaRepository<Info,Integer>{
	
	Info findById(int id);
}
