package com.weddingservices.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.Image;
import com.weddingservices.model.Product;
import com.weddingservices.model.Service;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer>{
	
	public List<Image> findByProduct(Product prodcut);
	public List<Image> findByService(Service service);
	
}
