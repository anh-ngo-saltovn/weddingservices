package com.weddingservices.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.weddingservices.exceptions.InvalidRequestException;
import com.weddingservices.exceptions.CustomizeException;
import com.weddingservices.exceptions.resources.ErrorResource;
import com.weddingservices.exceptions.resources.JsonMessage;
import com.weddingservices.model.Client;
import com.weddingservices.model.OauthClientDetails;
import com.weddingservices.model.Service;
import com.weddingservices.repositories.ClientRepository;
import com.weddingservices.repositories.OAuthClientDetailRepository;
import com.weddingservices.repositories.ServiceRepository;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/client")
@Transactional
public class ClientController {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	OAuthClientDetailRepository oOAuthClientDetailRepository;
	
	@Autowired
	UserAuthentication userAuthentication;
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@RequestMapping(value = "/{id}", method= RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getClient(@PathVariable (value="id") int id) {
			logger.info("get Client information");
			Client client=  clientRepository.findOne(id);
			 if (client == null) {
				logger.info("Client with id " + id + " not found");
				
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"Accounts of client with id " + id + " not found"));
	        }
			 return new ResponseEntity<Client>(client, HttpStatus.OK);
        
	}
	
	/**
	 * Find Accounts of a client
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/accounts", method= RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> findAccountOfClient(@PathVariable (value="id") int id
			, HttpSession session) {
		logger.info("get Account information");
		
		//Validation user of client
		userAuthentication.userValidate(session);
		List<OauthClientDetails> accounts=  oOAuthClientDetailRepository.findByClient(new Client(id));
		if (accounts.size() == 0) {
			logger.info("Accounts of client with id " + id + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Accounts of client with id " + id + " not found"));
        }
        return new ResponseEntity<List<OauthClientDetails>>(accounts,HttpStatus.OK);
	}
	
	/**
	 * Get services of client
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/services", method= RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> findServicesOfClient(@PathVariable (value="id") int id
			, HttpSession session, HttpServletRequest request) {
		logger.info("get Account information");
		userAuthentication.userValidate(session);
		List<Service> services=  serviceRepository.findServicesByClient(new Client(id));
		if (services.size() == 0) {
			logger.info("Services of client with id " + id + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Accounts of client with id " + id + " not found"));
        }
        return new ResponseEntity<List<Service>>(services,HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param client
	 * @param bindingResult
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody @Valid Client client  
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a Client " + client.getName());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		try {
			clientRepository.save(client);
			clientRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create a client"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/client/{id}").buildAndExpand(client.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody @Valid Client client, 
			BindingResult bindingResult, HttpSession session, HttpServletRequest request){
		logger.info("Updating Client with id "+ id);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		//Validation user of client
		userAuthentication.userValidate(session);
		
		if (clientRepository.findOne(id) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Client with id " + id + " not found"));
		}
		
		client.setId(id);
		try {
			clientRepository.saveAndFlush(client);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update a client"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Updated a client with id "+id),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		logger.info("Fectching & Deleting Client with id "+ id);
		if (id == 0 || clientRepository.findOne(id) == null) {
			logger.info("Unable to detele. Client with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Deleting a client with id " + id + " not found"));
		}
		try {
			clientRepository.delete(id);
			clientRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not delete client"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted a client with id "+id),HttpStatus.OK);
	}
	
}
