package com.xxzxxz.press.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xxzxxz.press.model.Questionitem;

public interface QuestionitemRepository extends JpaRepository<Questionitem,Integer>{

	
	List<Questionitem> findByQuestionIdOrderByOrderidAsc(int questionId);
	
	Questionitem findById(int id);
}
