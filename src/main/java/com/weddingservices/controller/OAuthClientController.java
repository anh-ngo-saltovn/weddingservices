package com.weddingservices.controller;

import javax.servlet.http.HttpSession;
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
import com.weddingservices.model.OauthClientDetails;
import com.weddingservices.repositories.OAuthClientDetailRepository;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/account")
@Transactional
public class OAuthClientController {

	private static final Logger logger = LoggerFactory.getLogger(OAuthClientController.class);
	
	@Autowired
	OAuthClientDetailRepository oAuthClientDetailRepository;
	
	@Autowired
	UserAuthentication userAuthentication;
	
	
	/**
	 * 
	 * @param oauthClientDetail
	 * @param bindingResult
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody @Valid OauthClientDetails oauthClientDetail
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a Account " + oauthClientDetail.getId());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		if (oAuthClientDetailRepository.findOne(oauthClientDetail.getId()) != null) {
			logger.warn("A account with email: "+oauthClientDetail.getId()+" already exist");
			throw new CustomizeException("Error ObjectExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Account with id " + oauthClientDetail.getId() + " already exist"));
		} 
		try {
			//endcode password
			oauthClientDetail.setClient_secret(userAuthentication.encodePassword(oauthClientDetail.getClient_secret()));
			
			oAuthClientDetailRepository.save(oauthClientDetail);
			oAuthClientDetailRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create an account"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/account?email={email}").buildAndExpand(oauthClientDetail.getClient_secret()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param id
	 * @param account
	 * @param bindingResult
	 * @return
	 */
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> updateAccount(@RequestParam("id") String id, @RequestBody @Valid OauthClientDetails oauthClientDetail
			, BindingResult bindingResult, HttpSession session){
		logger.info("Updating Account with id "+ id);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		userAuthentication.userValidate(session);
		oauthClientDetail.setId(id);
		if (oAuthClientDetailRepository.findOne(id) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Account with id " + id + " not found"));
		}
		try {
			//endcode password
			oauthClientDetail.setClient_secret(userAuthentication.encodePassword(oauthClientDetail.getClient_secret()));
			oAuthClientDetailRepository.saveAndFlush(oauthClientDetail);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update an account"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Updated an account with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAccount(@RequestParam("id") String id) {
		logger.info("Fectching & Deleting Account with id "+ id);
		if (id == "" || oAuthClientDetailRepository.findOne(id) == null) {
			logger.info("Unable to detele. Account with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Account with id " + id + " not found"));
		}
		try {
			oAuthClientDetailRepository.delete(id);
			oAuthClientDetailRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not delete an account"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted an acount with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	@RequestMapping( method= RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> account(@RequestParam (value="email") String email,HttpSession session) {
		logger.info("get Account information");
		userAuthentication.userValidate(session);
		OauthClientDetails oauthClientDetail=  oAuthClientDetailRepository.findOne(email);
		 if (oauthClientDetail == null) {
			logger.info("Account with email " + email + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Account with email " + email + " not found"));
        }
        return new ResponseEntity<OauthClientDetails>(oauthClientDetail, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param email
	 * @return
	 */
	@RequestMapping(value="/logout", method= RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> logout(HttpSession session) {
		logger.info("Clear session");
		session.invalidate();
        return new ResponseEntity<String>("", HttpStatus.OK);
	}

}
