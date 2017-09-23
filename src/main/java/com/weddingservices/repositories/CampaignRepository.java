package com.weddingservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.CampaignEvent;

@Repository
public interface CampaignRepository extends JpaRepository<CampaignEvent, Integer>{
	
}
