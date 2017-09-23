package com.weddingservices.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.Category;
import com.weddingservices.model.Service;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{
	public List<Category> findByService(Service service);
	
}
