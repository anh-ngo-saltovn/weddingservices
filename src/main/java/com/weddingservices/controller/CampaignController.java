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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.weddingservices.exceptions.CustomizeException;
import com.weddingservices.exceptions.InvalidRequestException;
import com.weddingservices.exceptions.resources.JsonMessage;
import com.weddingservices.model.CampaignEvent;
import com.weddingservices.repositories.CampaignRepository;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/campaign")
@Transactional
public class CampaignController {

	private static final Logger logger = LoggerFactory.getLogger(CampaignController.class);
	
	@Autowired
	CampaignRepository campaignRepository;
	
	@Autowired
	UserAuthentication userAuthentication;
	
	
	/**
	 * 
	 * @param campaign
	 * @param bindingResult
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody @Valid CampaignEvent campaignEvent
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a campaign " + campaignEvent.getId());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		try {
			
			campaignRepository.save(campaignEvent);
			campaignRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not campaign a plan"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/campaign/{id}").buildAndExpand(campaignEvent.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param id
	 * @param campaign
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") int id
			, @RequestBody @Valid CampaignEvent campaignEvent
			, BindingResult bindingResult){
		logger.info("Updating campaign with id "+ id);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		campaignEvent.setId(id);
		if (campaignRepository.findOne(id) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"campaign with id " + id + " not found"));
		}
		try {
			campaignRepository.saveAndFlush(campaignEvent);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update a campaign"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Updated a campaign with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		logger.info("Fectching & Deleting campaign with id "+ id);
		if (id == 0 || campaignRepository.findOne(id) == null) {
			logger.info("Unable to detele. campaign with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"campaign with id " + id + " not found"));
		}
		try {
			campaignRepository.delete(id);
			campaignRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not campaign a plan"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted a campaign with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public ResponseEntity<?> select(@PathVariable("id") int id) {
		logger.info("get campaign information");
		CampaignEvent campaign =  campaignRepository.findOne(id);
		 if (campaign == null) {
			logger.info("campaign with id " + id + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"campaign with id " + id + " not found"));
        }
        return new ResponseEntity<CampaignEvent>(campaign, HttpStatus.OK);
	}
	

}
