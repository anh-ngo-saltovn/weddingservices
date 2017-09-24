package com.weddingservices.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import com.weddingservices.exceptions.CustomizeException;
import com.weddingservices.exceptions.InvalidRequestException;
import com.weddingservices.exceptions.JsonMessage;
import com.weddingservices.model.Image;
import com.weddingservices.repositories.ImageRepository;
import com.weddingservices.repositories.ProductRepository;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/image")
@Transactional
public class ImageController {

	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	UserAuthentication userAuthentication;
	
	
	/**
	 * Upload a image to server
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/upload" ,method=RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<?> upload(MultipartHttpServletRequest request,
            HttpServletResponse response, HttpSession session) {
		String email = "";
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("username".equals(cookie.getName())) {
				email = cookie.getValue();
				break;
			}
		}
		userAuthentication.userValidate(session);
		MultipartFile file = request.getFile("file");
		
		String name = file.getOriginalFilename();
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
//				String rootPath = System.getProperty("catalina.home");
//				File dir = new File(rootPath + File.separator + "tmpFiles");
//				if (!dir.exists())
//					dir.mkdirs();
				String rootPath = request.getSession().getServletContext().getRealPath("/resources/images");
				File dir = new File(rootPath + File.separator + email);
				if (!dir.exists())
					dir.mkdirs();
				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
						
				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());
				
				return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
						, "Updated a image"),HttpStatus.OK);
			} catch (Exception e) {
				throw new CustomizeException(e.getMessage(), 
						new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
								"Can not create a image"));
			}
		} else {
			throw new CustomizeException("", 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create a image"));
		}

		
	}
	
	@RequestMapping(value="/uploads" ,method=RequestMethod.POST, produces = {"application/json"})
	public ResponseEntity<?> uploads(MultipartHttpServletRequest request,
            HttpServletResponse response, HttpSession session) {
		String email = "";
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("username".equals(cookie.getName())) {
				email = cookie.getValue();
				break;
			}
		}
		userAuthentication.userValidate(session);
		MultipartFile file = request.getFile("file");
		
		String name = file.getOriginalFilename();
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				String rootPath = request.getSession().getServletContext().getRealPath("/resources/images");
				File dir = new File(rootPath + File.separator + email);
				if (!dir.exists())
					dir.mkdirs();
				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
						
				logger.info("Server File Location="
						+ serverFile.getAbsolutePath());
				
				return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
						, "Updated a image"),HttpStatus.OK);
			} catch (Exception e) {
				throw new CustomizeException(e.getMessage(), 
						new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
								"Can not create a image"));
			}
		} else {
			throw new CustomizeException("", 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create a image"));
		}
	}
	/**
	 * 
	 * @param image
	 * @param bindingResult
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody @Valid Image image
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a image " + image.getImagePath());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		try {
			imageRepository.saveAndFlush(image);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create a image"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/image/{id}").buildAndExpand(image.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @param id
	 * @param image
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody @Valid Image image
			, BindingResult bindingResult){
		logger.info("Updating image with id "+ id);
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		image.setId(id);
		if (imageRepository.findOne(id) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Image with id " + id + " not found"));
		}
		try {
			imageRepository.saveAndFlush(image);
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update a image"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Updated an image with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") int id) {
		logger.info("Fectching & Deleting image with id "+ id);
		if (id == 0 || imageRepository.findOne(id) == null) {
			logger.info("Unable to detele. image with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Image with id " + id + " not found"));
		}
		try {
			imageRepository.delete(id);
			imageRepository.flush();
		} catch (RuntimeException e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not delete a image"));
		}
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted an category with id "+id),HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method= RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> select(@PathVariable("id") int id) {
		logger.info("get image information");
		Image image= imageRepository.findOne(id);
		 if (image == null) {
			logger.info("Image with id " + id + " not found");
			
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Image with id " + id + " not found"));
        }
        return new ResponseEntity<Image>(image, HttpStatus.OK);
	}
	
}
