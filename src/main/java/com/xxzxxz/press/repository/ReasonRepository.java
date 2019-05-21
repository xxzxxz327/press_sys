package com.xxzxxz.press.repository;

import com.xxzxxz.press.model.ChangeReason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReasonRepository extends JpaRepository<ChangeReason,Integer> {
	Page<ChangeReason> findById(int id, Pageable pageable);
	Page<ChangeReason> findByReasonName(String reasonName, Pageable pageable);
	Page<ChangeReason> findAll(Pageable pageable);
	
	ChangeReason findById(int id);
	ChangeReason findByReasonName(String reasonName);
}
