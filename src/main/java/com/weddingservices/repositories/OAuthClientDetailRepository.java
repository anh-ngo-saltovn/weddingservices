package com.weddingservices.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.weddingservices.model.Client;
import com.weddingservices.model.Couple;
import com.weddingservices.model.OauthClientDetails;

@Repository
public interface OAuthClientDetailRepository extends JpaRepository<OauthClientDetails, String>{
	
	
	public OauthClientDetails findAccountByClient_Id(String client_id);
	public List<OauthClientDetails> findByClient(Client client);
	public OauthClientDetails findByCouple(Couple couple);
	
}
