package com.weddingservices.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.Client;
import com.weddingservices.model.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>{
	
	public List<Service> findServicesByClient(Client client);
}
