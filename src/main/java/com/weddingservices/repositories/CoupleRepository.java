package com.weddingservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.Couple;

@Repository
public interface CoupleRepository extends JpaRepository<Couple, Integer>{
	public Couple findCoupleByEmail(String email);
}
