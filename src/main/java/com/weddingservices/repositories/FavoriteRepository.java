package com.weddingservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.Favorite;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer>{
	
}
