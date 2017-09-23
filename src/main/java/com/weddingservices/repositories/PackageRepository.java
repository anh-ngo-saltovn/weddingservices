package com.weddingservices.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.Packages;
import com.weddingservices.model.Plan;

@Repository
public interface PackageRepository extends JpaRepository<Packages, Integer>{
	public List<Packages> findByPlan(Plan plan);
}
