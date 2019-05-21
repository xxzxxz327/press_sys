package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.Area;
import com.xxzxxz.press.model.Merit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeritDicRepository extends JpaRepository<Merit,Integer>{

	Page<Merit> findById(int id, Pageable pageable);
	Page<Merit> findAll(Pageable pageable);

	Merit findById(int id);

	@Query("select a from Merit a")
	List<Merit> showAll();

}
