package com.xxzxxz.press.repository;

import java.util.List;

import com.xxzxxz.press.model.Pack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;






public interface PackRepository extends JpaRepository<Pack,Integer> {
	@Query("select p from Pack p")
	Page<Pack>findList(Pageable pageable);
	Pack findById(int id);
	Pack save(Pack pack);

	void deleteById(int id) ;
	
}
