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
import com.weddingservices.model.Image;
import com.weddingservices.model.Product;
import com.weddingservices.repositories.CategoryRepository;
import com.weddingservices.repositories.ImageRepository;
import com.weddingservices.repositories.ProductRepository;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/product")
@Transactional
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
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
	public ResponseEntity<Void> add(@RequestBody @Valid Product product
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a product " + product.getName());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		try {
			
			if (categoryRepository.findOne(product.getCategory().getId()) == null) {
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"Category with id " + product.getCategory().getId() + " not found, please create category"));
			}
			productRepository.saveAndFlush(product);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create a product"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/product/{id}").buildAndExpand(product.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody @Valid Product product, BindingResult bindingResult){
		logger.info("Updating product with id "+ id);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		product.setId(id);
		if (productRepository.findOne(id) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Product with id " + id + " not found"));
		}
		if (categoryRepository.findOne(product.getCategory().getId()) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Category with id " + product.getCategory().getId() + " not found, please create category"));
		}
		try {
			productRepository.saveAndFlush(product);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update a product"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Updated an product with id "+id),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		logger.info("Fectching & Deleting prodcut with id "+ id);
		if (id == 0 || productRepository.findOne(id) == null) {
			logger.info("Unable to detele. product with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Product with id " + id + " not found"));
		}
		try {
			productRepository.delete(id);
			productRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not delete a product"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted an product with id "+id),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method= RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> select(@PathVariable("id") int id) {
		logger.info("get product information");
		Product product=  productRepository.findOne(id);
		 if (product == null) {
			logger.info("Product with id " + id + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Product with id " + id + " not found"));
        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
	}

	/**
	 * Get Images by product
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/images", method = RequestMethod.GET)
	public ResponseEntity<?> getProducts(@PathVariable("id") int id) {
		logger.info("get images information");
		List<Image> images =  imageRepository.findByProduct(new Product(id));
		 if (images.size() == 0) {
			 logger.info("image with product id "+ id + "not found");
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"image with product with id " + id + " not found"));
        }
        return new ResponseEntity<List<Image>>(images, HttpStatus.OK);
	}
	
}
