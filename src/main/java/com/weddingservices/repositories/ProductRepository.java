package com.weddingservices.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.Category;
import com.weddingservices.model.Packages;
import com.weddingservices.model.Plan;
import com.weddingservices.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
	public List<Product> findByCategory(Category category);
	public List<Product> findByPackages(Packages packages);
	public List<Product> findByPlan(Plan plan);
	
}
