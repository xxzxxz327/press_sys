package com.xxzxxz.press.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.xxzxxz.press.model.Attribute;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute,Integer> {
	@Query(value="select a.press_id, p.press_name, a.unsub, a.changeable from attribute as a left join press_info as p on a.press_id = p.id where a.press_id=?1 ",
			countQuery = "select count(a.press_id) from attribute as a left join press_info as p on a.press_id = p.id where a.press_id=?1", nativeQuery = true)
	Page<Object[]> find_press_attribute_by_pId(int pressId, Pageable pageable);
	@Query(value="select a.press_id, p.press_name, a.unsub, a.changeable from attribute as a left join press_info as p on a.press_id = p.id  ",
			countQuery = "select count(a.press_id) from attribute as a left join press_info as p on a.press_id = p.id ", nativeQuery = true)
	Page<Object[]> find_all_press_attribute(Pageable pageable);
	Attribute findByPressId(int id);
}
