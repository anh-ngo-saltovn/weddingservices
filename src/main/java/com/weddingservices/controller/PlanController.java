package com.weddingservices.controller;

import java.util.List;

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
import com.weddingservices.exceptions.JsonMessage;
import com.weddingservices.model.Couple;
import com.weddingservices.model.Packages;
import com.weddingservices.model.Plan;
import com.weddingservices.model.Product;
import com.weddingservices.repositories.PackageRepository;
import com.weddingservices.repositories.PlanRepository;
import com.weddingservices.repositories.ProductRepository;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/plan")
@Transactional
public class PlanController {

	private static final Logger logger = LoggerFactory.getLogger(PlanController.class);
	
	@Autowired
	PlanRepository planRepository;
	
	@Autowired
	PackageRepository packageRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	
	@Autowired
	UserAuthentication userAuthentication;
	
	
	/**
	 * 
	 * @param plan
	 * @param bindingResult
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody @Valid Plan plan
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a plan " + plan.getName());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		if (planRepository.findOne(plan.getId()) != null) {
			logger.warn("A plan with id: "+plan.getId()+" already exist");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"plan with id " + plan.getId() + " not found"));
		} 
		try {
			
			planRepository.save(plan);
			planRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create a plan"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/plan/{id}").buildAndExpand(plan.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param id
	 * @param plan
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") int id
			, @RequestBody @Valid Plan plan
			, BindingResult bindingResult){
		logger.info("Updating plan with id "+ id);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		plan.setId(id);
		if (planRepository.findOne(id) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Plan with id " + id + " not found"));
		}
		try {
			planRepository.saveAndFlush(plan);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update an plan"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Updated a plan with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		logger.info("Fectching & Deleting package with id "+ id);
		if (id == 0 || planRepository.findOne(id) == null) {
			logger.info("Unable to detele. plan with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Plan with id " + id + " not found"));
		}
		try {
			planRepository.delete(id);
			planRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not delete a plan"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted a plan with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public ResponseEntity<?> select(@PathVariable("id") int id) {
		logger.info("get plan information");
		Plan plan=  planRepository.findOne(id);
		 if (plan == null) {
			logger.info("Plan with id " + id + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Plan with id " + id + " not found"));
        }
        return new ResponseEntity<Plan>(plan, HttpStatus.OK);
	}
	
	/**
	 * Get product by plan
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/products", method = RequestMethod.GET)
	public ResponseEntity<?> getProducts(@PathVariable("id") int id, @RequestBody Couple couple) {
		logger.info("get products information");
		List<Product> products =  productRepository.findByPlan(new Plan(id,couple));
		 if (products.size() == 0) {
			 logger.info("Products with plan id "+ id + "not found");
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"Products with plan with id " + id + " not found"));
        }
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}
	
	/**
	 * Get product by plan
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/packages", method = RequestMethod.GET)
	public ResponseEntity<?> getPackages(@PathVariable("id") int id, @RequestBody Couple couple) {
		logger.info("get Packages information");
		List<Packages> packages =  packageRepository.findByPlan(new Plan(id,couple));
		 if (packages.size() == 0) {
			 logger.info("Packages with plan id "+ id + "not found");
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"Packages with plan with id " + id + " not found"));
        }
        return new ResponseEntity<List<Packages>>(packages, HttpStatus.OK);
	}

}
