package com.xxzxxz.press.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xxzxxz.press.model.Question;

public interface QuestionRepository extends JpaRepository<Question,Integer>{

	List<Question> findByAskIdOrderByOrderidAsc(int askId);
	
	Question findById(int id);
	
}
