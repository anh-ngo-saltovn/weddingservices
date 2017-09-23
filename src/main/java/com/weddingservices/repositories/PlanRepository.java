package com.weddingservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer>{
}
