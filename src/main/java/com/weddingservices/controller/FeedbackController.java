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
import com.weddingservices.model.FeedBack;
import com.weddingservices.repositories.FeedbackRepository;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/feedback")
@Transactional
public class FeedbackController {

	private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
	
	@Autowired
	FeedbackRepository feedbackRepository;
	
	@Autowired
	UserAuthentication userAuthentication;
	
	
	/**
	 * 
	 * @param feedback
	 * @param bindingResult
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody @Valid FeedBack feedback
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a feedback " + feedback.getId());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		try {
			
			feedbackRepository.save(feedback);
			feedbackRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not feedback a plan"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/feedback/{id}").buildAndExpand(feedback.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param id
	 * @param Feedback
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") int id
			, @RequestBody @Valid FeedBack feedback
			, BindingResult bindingResult){
		logger.info("Updating feedback with id "+ id);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		feedback.setId(id);
		if (feedbackRepository.findOne(id) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Feedback with id " + id + " not found"));
		}
		try {
			feedbackRepository.saveAndFlush(feedback);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update a feedback"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Updated a feedback with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		logger.info("Fectching & Deleting feedback with id "+ id);
		if (id == 0 || feedbackRepository.findOne(id) == null) {
			logger.info("Unable to detele. feedback with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"feedback with id " + id + " not found"));
		}
		try {
			feedbackRepository.delete(id);
			feedbackRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not feedback a plan"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted a feedback with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public ResponseEntity<?> select(@PathVariable("id") int id) {
		logger.info("get feedback information");
		FeedBack feedback=  feedbackRepository.findOne(id);
		 if (feedback == null) {
			logger.info("feedback with id " + id + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"feedback with id " + id + " not found"));
        }
        return new ResponseEntity<FeedBack>(feedback, HttpStatus.OK);
	}
	

}
