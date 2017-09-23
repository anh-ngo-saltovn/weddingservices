package com.weddingservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.FeedBack;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedBack, Integer>{
	
}
