package com.weddingservices.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import com.mysql.jdbc.StringUtils;
import com.weddingservices.enums.RoleEnum;
import com.weddingservices.enums.StateEnum;
import com.weddingservices.exceptions.CustomizeException;
import com.weddingservices.exceptions.InvalidRequestException;
import com.weddingservices.exceptions.resources.JsonMessage;
import com.weddingservices.model.Category;
import com.weddingservices.model.Client;
import com.weddingservices.model.Image;
import com.weddingservices.model.OauthClientDetails;
import com.weddingservices.model.Service;
import com.weddingservices.repositories.CategoryRepository;
import com.weddingservices.repositories.ClientRepository;
import com.weddingservices.repositories.ImageRepository;
import com.weddingservices.repositories.ServiceRepository;
import com.weddingservices.service.ServiceService;
import com.weddingservices.util.MyStringUtils;
import com.weddingservices.util.UserAuthentication;

@Controller
@RestController
@RequestMapping("/service")
@Transactional
public class ServiceController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	UserAuthentication userAuthentication;
	
	@Autowired
	ServiceService serviceService;
	
	@RequestMapping(value="/image" , method=RequestMethod.POST
			, produces = {"application/json;charset=UTF-8"})
	public ResponseEntity<?> createService(MultipartHttpServletRequest request,
            HttpServletResponse response, HttpSession session) {
		String email = "";
		int clientId = 0 ;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			switch (cookie.getName()) {
			case "username":
				email = cookie.getValue();
				break;
			case "client_id":
				clientId = Integer.valueOf(cookie.getValue());
				break;
			default:
				break;
			}
		}
		userAuthentication.userValidate(session);
		
		//----Start Upload file to server-------
		try {
			// Creating the directory to store file
			String rootPath = request.getSession().getServletContext().getRealPath("/resources/images");
			Map<String, String[]> serviceMap = request.getParameterMap();
			String area = MyStringUtils.convertUTF8(serviceMap.get("area"));
			String address = MyStringUtils.convertUTF8(serviceMap.get("address"));
			String description = MyStringUtils.convertUTF8(serviceMap.get("description"));
			String type = MyStringUtils.convertUTF8(serviceMap.get("type"));
			String name = MyStringUtils.convertUTF8(serviceMap.get("name"));
			String phone = MyStringUtils.convertUTF8(serviceMap.get("phone"));
			String facebook = MyStringUtils.convertUTF8(serviceMap.get("facebook"));
			String website = MyStringUtils.convertUTF8(serviceMap.get("website"));
			String tmpLat = MyStringUtils.convertUTF8(serviceMap.get("lat"));
			String tmpLong = MyStringUtils.convertUTF8(serviceMap.get("lng"));
			float lat = 0;
			float lng = 0;
			if (!StringUtils.isNullOrEmpty(tmpLat)) {
				lat = Float.valueOf(tmpLat);
			}
			if (!StringUtils.isNullOrEmpty(tmpLong)) {
				lng = Float.valueOf(tmpLong);
			}
			Service service = new Service();
			Iterator<String> fileNames = request.getFileNames();
			List<MultipartFile> files = new ArrayList<MultipartFile>();
			while (fileNames.hasNext()) {
				String fileName = fileNames.next();
				List<String> splitFile = StringUtils.split(fileName, ".",true);
				if(splitFile.size() > 1) {
					if(!("jpg".equals(splitFile.get(splitFile.size()-1)) 
							||"jpeg".equals(splitFile.get(1))
							||"png".equals(splitFile.get(1)))) {
						return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.BAD_REQUEST.value()
								, "Image type error"),HttpStatus.BAD_REQUEST);
					}
				}
				
				files.add(request.getFile(fileName));
			}
			service.setAddress(address);
			service.setAreaCd(area);
			service.setDescription(description);
			service.setType(type);
			service.setName(name);
			service.setPhone(phone);
			service.setFacebook(facebook);
			service.setWebsite(website);
			service.setClient(new Client(clientId));
			service.setLat(lat);
			service.setLng(lng);
			service.setStatus(StateEnum.CHO_XAC_NHAN);
			String filePath = serviceService.insert(service, files, rootPath, email);
			
			return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
					, filePath),HttpStatus.OK);
		} catch (CustomizeException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not create a Service"));
		}
		//-----End Upload file to server------
		
	}
	
	/**
	 * Create a service for account
	 * @param service ( have account information)
	 * @param bindingResult
	 * @param ucBuilder
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody @Valid Service service
			, BindingResult bindingResult
			, UriComponentsBuilder ucBuilder) {
		
		logger.info("Creating a Service " + service.getName());
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid request", bindingResult);
		}
		
		//check quest have client info
		if (clientRepository.findOne(service.getClient().getId()) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Client with id " + service.getClient().getId() + " not found"));
		}
		
		serviceRepository.save(service);
		serviceRepository.flush();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/service/{id}").buildAndExpand(service.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	
	/**
	 * 
	 * @param id
	 * @param service
	 * @param bindingResult
	 * @return
	 */
	
	@RequestMapping(value = "/update", method=RequestMethod.POST
			, produces = {"application/json;charset=UTF-8"})
	public ResponseEntity<?> updateService(
			MultipartHttpServletRequest request,
            HttpServletResponse response, HttpSession session) {
		boolean changeFlg = false;
		String email = "";
		int clientId = 0 ;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			switch (cookie.getName()) {
			case "username":
				email = cookie.getValue();
				break;
			case "client_id":
				clientId = Integer.valueOf(cookie.getValue());
				break;
			default:
				break;
			}
		}
		userAuthentication.userValidate(session);
		
		//check exist client
		Client client = clientRepository.findOne(clientId);
		if (clientRepository.findOne(clientId) == null) {
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Client with id " + clientId + " not found"));
		}
		//----Start Upload file to server-------
		try {
			// Creating the directory to store file
			String rootPath = request.getSession().getServletContext().getRealPath("/resources/images");
			Map<String, String[]> serviceMap = request.getParameterMap();
			String area = MyStringUtils.convertUTF8(serviceMap.get("area"));
			String address = MyStringUtils.convertUTF8(serviceMap.get("address"));
			String description = MyStringUtils.convertUTF8(serviceMap.get("description"));
			String type = MyStringUtils.convertUTF8(serviceMap.get("type"));
			String name = MyStringUtils.convertUTF8(serviceMap.get("name"));
			String phone = MyStringUtils.convertUTF8(serviceMap.get("phone"));
			String facebook = MyStringUtils.convertUTF8(serviceMap.get("facebook"));
			String website = MyStringUtils.convertUTF8(serviceMap.get("website"));
			String tmpLat = MyStringUtils.convertUTF8(serviceMap.get("lat"));
			String tmpLong = MyStringUtils.convertUTF8(serviceMap.get("lng"));
			String status = MyStringUtils.convertUTF8(serviceMap.get("status"));
			int id = Integer.valueOf(MyStringUtils.convertUTF8(serviceMap.get("id")));
			float lat = 0;
			float lng = 0;
			if (!StringUtils.isNullOrEmpty(tmpLat)) {
				lat = Float.valueOf(tmpLat);
			}
			if (!StringUtils.isNullOrEmpty(tmpLong)) {
				lng = Float.valueOf(tmpLong);
			}
			
			//Check exist service
			Service service = serviceRepository.findOne(id);
			if (service == null) {
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"Service with id " + id + " not found"));
			}
			Iterator<String> fileNames = request.getFileNames();
			List<MultipartFile> files = new ArrayList<MultipartFile>();
			
			while (fileNames.hasNext()) {
				String fileName = fileNames.next();
				List<String> splitFile = StringUtils.split(fileName, ".",true);
				if(splitFile.size() > 1) {
					if(!("jpg".equals(splitFile.get(splitFile.size()-1)) 
							||"jpeg".equals(splitFile.get(1))
							||"png".equals(splitFile.get(1)))) {
						return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.BAD_REQUEST.value()
								, "Image type error"),HttpStatus.BAD_REQUEST);
					}
				}
				
				files.add(request.getFile(fileName));
			}
			if (!StringUtils.isNullOrEmpty(address)) {
				service.setAddress(address);
				changeFlg = true;
			}
			if (!StringUtils.isNullOrEmpty(area)) {
				service.setAreaCd(area);
				changeFlg = true;
			}
			if (!StringUtils.isNullOrEmpty(description)) {
				service.setDescription(description);
				changeFlg = true;
			}
			
			if (!StringUtils.isNullOrEmpty(type)) {
				service.setType(type);
				changeFlg = true;
			}
			
			if (!StringUtils.isNullOrEmpty(name)) {
				service.setName(name);
				changeFlg = true;
			}
			
			if (!StringUtils.isNullOrEmpty(phone)) {
				service.setPhone(phone);
				changeFlg = true;
			}
			
			if (!StringUtils.isNullOrEmpty(facebook)) {
				service.setFacebook(facebook);
				changeFlg = true;
			}
			
			if (!StringUtils.isNullOrEmpty(website)) {
				service.setWebsite(website);
				changeFlg = true;
			}
			
			if (lat != 0) {
				service.setLat(lat);
				changeFlg = true;
			}
			
			if (lng != 0) {
				service.setLng(lng);
				changeFlg = true;
			}
			
			if (client.getOauthClientDetailsList() != null){
				List<OauthClientDetails> objec = client.getOauthClientDetailsList();
				for (OauthClientDetails oauthClientDetail : objec) {
					if (email.equals(oauthClientDetail.getId())) {
						if(RoleEnum.ROLE_ADMIN.toString().equals(oauthClientDetail.getAuthorities())) {
							if (!StringUtils.isNullOrEmpty(status)) {
								service.setStatus(StateEnum.valueOf(status));
								changeFlg = true;
							}
						}
						break;
					}
				}
				
			}
			if (changeFlg) {
				String filePath = serviceService.update(service, files, rootPath, email);
				
				return new ResponseEntity<Service>(service,HttpStatus.OK);
			
			} else {
				return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
						, "Not change"),HttpStatus.OK);
			}
		} catch (CustomizeException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomizeException(e.getMessage(), 
					new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Can not update a Service"));
		}
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deteleService(@PathVariable("id") int id) {
		logger.info("Fectching & Deleting Service with id "+ id);
		if (id == 0 || serviceRepository.findOne(id) == null) {
			logger.info("Unable to detele. Account with id "+ id + "not found");
			throw new CustomizeException("Error ObjectNotExistException"
					,new JsonMessage(HttpStatus.NOT_FOUND.value(),
							"Service with id " + id + " not found"));
		}
		
		serviceRepository.delete(id);
		serviceRepository.flush();
		return new ResponseEntity<JsonMessage>(new JsonMessage(HttpStatus.OK.value()
				, "Deleted a service with id "+id),HttpStatus.OK);
	}
	
	/**
	 * Get service by id
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getService(@PathVariable("id") int id) {
		logger.info("get Account information");
		Service service=  serviceRepository.findOne(id);
		 if (service == null) {
			 logger.info("Unable to detele. Account with id "+ id + "not found");
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"Service with id " + id + " not found"));
        }
        return new ResponseEntity<Service>(service, HttpStatus.OK);
	}

	
	/**
	 * Get categories by id
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/categories", method = RequestMethod.GET)
	public ResponseEntity<?> getCategories(@PathVariable("id") int id) {
		logger.info("get Categories information");
		List<Category> categorys =  categoryRepository.findByService(new Service(id));
		 if (categorys.size() == 0) {
			 logger.info("Category with service id "+ id + "not found");
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"Category with id " + id + " not found"));
        }
        return new ResponseEntity<List<Category>>(categorys, HttpStatus.OK);
	}
	

	/**
	 * Get images by id
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/images", method = RequestMethod.GET)
	public ResponseEntity<?> getImages(@PathVariable("id") int id) {
		logger.info("get Images information");
		List<Image> images =  imageRepository.findByService(new Service(id));
        return new ResponseEntity<List<Image>>(images, HttpStatus.OK);
	}
}
