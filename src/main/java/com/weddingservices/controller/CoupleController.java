package com.weddingservices.controller;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.weddingservices.exceptions.CustomizeException;
import com.weddingservices.exceptions.InvalidRequestException;
import com.weddingservices.exceptions.resources.JsonMessage;
import com.weddingservices.model.Couple;
import com.weddingservices.model.OauthClientDetails;
import com.weddingservices.repositories.CoupleRepository;
import com.weddingservices.repositories.OAuthClientDetailRepository;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/couple")
@Transactional
public class CoupleController {

	private static final Logger logger = LoggerFactory.getLogger(CoupleController.class);
	
	@Autowired
	CoupleRepository coupleRepository;
	
	@Autowired
	OAuthClientDetailRepository oAuthClientDetailRepository;
	
	@Autowired
	UserAuthentication userAuthentication;
	
	/**
	 * Create new couple account, insert to 2 table Account and Couple
	 * @param couple
	 * @param bindingResult
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody @Valid Couple couple
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a Couple " + couple.getEmail());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		if (coupleRepository.findCoupleByEmail(couple.getEmail()) != null) {
			logger.warn("A couple account with email: "+couple.getEmail()+" already exist");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"A couple account with id " + couple.getEmail() + " not found"));
		} 
		try {
			//endcode password
			couple.getOauthClientDetail().setClient_secret(
					userAuthentication.encodePassword(couple.getOauthClientDetail().getClient_secret()));
			
			//set Role
			//couple.getOauthClientDetail().setRole(new Role(RoleEnum.COUPLE.getValue(), ""));
			
			//set Email
			couple.getOauthClientDetail().setId(couple.getEmail());
			
			//create an account for couple
			OauthClientDetails savedAccount = oAuthClientDetailRepository.save(couple.getOauthClientDetail());
			
			//create couple information
			Couple savedCouple = coupleRepository.saveAndFlush(couple);
			
			//update couple infor to account 
			savedAccount.setCouple(savedCouple);
			oAuthClientDetailRepository.save(savedAccount);
			oAuthClientDetailRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create an account for couple"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/couple?email={email}").buildAndExpand(couple.getEmail()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	
	/**
	 * Update Couple information(Account and Couple info), to 2 table (Account, Couple)
	 * @param id
	 * @param couple
	 * @param bindingResult
	 * @return
	 * @throws RuntimeException
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCouple(@PathVariable("id") int id, @RequestBody @Valid Couple couple, BindingResult bindingResult) throws RuntimeException{
		logger.info("Updating couple account with id "+ id);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		if (coupleRepository.findOne(id) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Couple with id " + id + " not found"));
		}
		
		OauthClientDetails acc = oAuthClientDetailRepository.findOne(couple.getOauthClientDetail().getId());
		if (acc == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Account with id " + id + " not found"));
		}
		try {
			couple.setId(id);
			acc.setCouple(couple);
			acc.setId(couple.getEmail());;
			acc.setClient_secret(userAuthentication.encodePassword(couple.getOauthClientDetail().getClient_secret()));
			//acc.setRole(new Role(RoleEnum.COUPLE.getValue(), ""));
			
			//update an account for couple
			oAuthClientDetailRepository.save(acc);
			
			//update couple information
			couple.setOauthClientDetail(acc);
			coupleRepository.save(couple);
			
			oAuthClientDetailRepository.flush();
			coupleRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update an account for couple"));
		}
		
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Updated an Couple with id "+id),HttpStatus.OK);
	}
	
	/**
	 * Delete Couple 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCouple(@PathVariable("id") int id) {
		logger.info("Fectching & Deleting Account with id "+ id);
		Couple deteleCouple = coupleRepository.findOne(id);
		if (deteleCouple == null) {
			logger.info("Unable to detele a couple with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Couple with id " + id + " not found"));
		}
		try {
			oAuthClientDetailRepository.delete(deteleCouple.getOauthClientDetail());
			coupleRepository.delete(id);
			coupleRepository.flush();
			oAuthClientDetailRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not detele an account for couple"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted a Couple with id "+id),HttpStatus.OK);
	}
	
	@RequestMapping( method= RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> couple(@RequestParam (value="email") String email) {
		logger.info("get Couple information");
		Couple couple=  coupleRepository.findCoupleByEmail(email);
		 if (couple == null) {
			logger.info("Couple with email " + email + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Couple with email " + email + " not found"));
        }
        return new ResponseEntity<Couple>(couple, HttpStatus.OK);
	}

}
