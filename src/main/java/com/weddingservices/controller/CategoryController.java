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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.weddingservices.exceptions.CustomizeException;
import com.weddingservices.exceptions.InvalidRequestException;
import com.weddingservices.exceptions.JsonMessage;
import com.weddingservices.model.Category;
import com.weddingservices.model.Product;
import com.weddingservices.repositories.CategoryRepository;
import com.weddingservices.repositories.ProductRepository;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/category")
@Transactional
public class CategoryController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	UserAuthentication userAuthentication;
	
	
	/**
	 * Create category 
	 * @param category
	 * @param bindingResult
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody @Valid Category category
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a category " + category.getName());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		try {
			categoryRepository.saveAndFlush(category);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create a category"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/category/{id}").buildAndExpand(category.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody @Valid Category category, BindingResult bindingResult){
		logger.info("Updating category with id "+ id);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		category.setId(id);
		if (categoryRepository.findOne(id) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Category with id " + id + " not found"));
		}
		try {
			categoryRepository.saveAndFlush(category);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update a category"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Updated an category with id "+id),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		logger.info("Fectching & Deleting category with id "+ id);
		if (id == 0 || categoryRepository.findOne(id) == null) {
			logger.info("Unable to detele. category with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Category with id " + id + " not found"));
		}
		try {
			categoryRepository.delete(id);
			categoryRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not delete a category"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted an category with id "+id),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method= RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> select(@PathVariable("id") int id) {
		logger.info("get category information");
		Category category=  categoryRepository.findOne(id);
		 if (category == null) {
			logger.info("Category with id " + id + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Category with id " + id + " not found"));
        }
        return new ResponseEntity<Category>(category, HttpStatus.OK);
	}
	
	
	/**
	 * Get product by category
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/products", method = RequestMethod.GET)
	public ResponseEntity<?> getProducts(@PathVariable("id") int id) {
		logger.info("get products information");
		List<Product> products =  productRepository.findByCategory(new Category(id));
		 if (products.size() == 0) {
			 logger.info("Products with category id "+ id + "not found");
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"Products with category with id " + id + " not found"));
        }
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}
	
}
