package com.xxzxxz.press.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.xxzxxz.press.model.Area;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area,Integer>{

	Page<Area> findById(int id, Pageable pageable);
	Page<Area> findByAreaName(String name, Pageable pageable);
	Page<Area> findAll(Pageable pageable);

	Area findById(int id);
	Area findByAreaName(String name);

	@Query("select a from Area a")
	List<Area> showAll();

}
