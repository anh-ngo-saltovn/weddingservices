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
import com.weddingservices.exceptions.resources.JsonMessage;
import com.weddingservices.model.Packages;
import com.weddingservices.model.Product;
import com.weddingservices.repositories.PackageRepository;
import com.weddingservices.repositories.ProductRepository;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/package")
@Transactional
public class PackageController {

	private static final Logger logger = LoggerFactory.getLogger(PackageController.class);
	
	@Autowired
	PackageRepository packageRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	
	@Autowired
	UserAuthentication userAuthentication;
	
	
	/**
	 * 
	 * @param package
	 * @param bindingResult
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody @Valid Packages package1
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a package " + package1.getName());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		if (packageRepository.findOne(package1.getId()) != null) {
			logger.warn("A package with id: "+package1.getId()+" already exist");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"package with id " + package1.getId() + " not found"));
		} 
		try {
			
			packageRepository.save(package1);
			packageRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create a package"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/package/{id}").buildAndExpand(package1.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param id
	 * @param package1
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") int id
			, @RequestBody @Valid Packages package1
			, BindingResult bindingResult){
		logger.info("Updating package with id "+ id);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		package1.setId(id);
		if (packageRepository.findOne(id) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Package with id " + id + " not found"));
		}
		try {
			packageRepository.saveAndFlush(package1);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update an package"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Updated a package with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		logger.info("Fectching & Deleting package with id "+ id);
		if (id == 0 || packageRepository.findOne(id) == null) {
			logger.info("Unable to detele. package with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Package with id " + id + " not found"));
		}
		try {
			packageRepository.delete(id);
			packageRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not delete a packge"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted a packge with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public ResponseEntity<?> select(@PathVariable("id") int id) {
		logger.info("get package information");
		Packages packages=  packageRepository.findOne(id);
		 if (packages == null) {
			logger.info("Package with id " + id + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Package with id " + id + " not found"));
        }
        return new ResponseEntity<Packages>(packages, HttpStatus.OK);
	}
	
	/**
	 * Get product by package
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/products", method = RequestMethod.GET)
	public ResponseEntity<?> getProducts(@PathVariable("id") int id) {
		logger.info("get products information");
		List<Product> products =  productRepository.findByPackages(new Packages(id));
		 if (products.size() == 0) {
			 logger.info("Products with package id "+ id + "not found");
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"Products with package with id " + id + " not found"));
        }
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}
	

}
