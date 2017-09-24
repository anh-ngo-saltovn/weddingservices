package com.weddingservices.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.weddingservices.exceptions.CustomizeException;
import com.weddingservices.exceptions.JsonMessage;
import com.weddingservices.model.Image;
import com.weddingservices.repositories.ClientRepository;
import com.weddingservices.repositories.ImageRepository;
import com.weddingservices.repositories.ServiceRepository;
import com.weddingservices.util.FileUtils;

@Service
public class ServiceService {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceService.class);
	
	@Autowired
	ServiceRepository serviceRepository;
	
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
	ClientRepository clientRepository;
	
	public String insert(com.weddingservices.model.Service service, List<MultipartFile> files
			, String rootPath, String email) throws Exception {
		String filePath = "";
		try {
			//check quest have client info
			if (clientRepository.findOne(service.getClient().getId()) == null) {
				throw new CustomizeException("Error ObjectNotExistException"
						,new JsonMessage(HttpStatus.NOT_FOUND.value(),
								"Client with id " + service.getClient().getId() + " not found"));
			}
			Date currentDate = new Date(System.currentTimeMillis());
			ArrayList<Image> imageList = new ArrayList<Image>();
			for (MultipartFile multipartFile : files) {
				filePath = FileUtils.createFile(multipartFile, rootPath, String.valueOf(service.getClient().getId()));
				if ("logo".equals(multipartFile.getName())) {
					service.setLogo(filePath);
				} else {
					Image image  = new Image();
					image.setImagePath(filePath);
					
					image.setCreateDt(currentDate);
					image.setUpdateDt(currentDate);
					image.setService(service);
					imageList.add(image);
				}
			}
			service.setCreateDt(currentDate);
			service.setUpdateDt(currentDate);
			service.setImageList(imageList);
			serviceRepository.saveAndFlush(service);
			imageRepository.save(imageList);
			imageRepository.flush();
			
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			logger.error("Cause: " + e.getCause());
			throw e;
		}
		
		return filePath;
	}
	
	public String update(com.weddingservices.model.Service service, List<MultipartFile> files
			, String rootPath, String email) throws Exception {
		String filePath = "";
		Date currentDate = new Date(System.currentTimeMillis());
		try {
			List<Image> imageList = new ArrayList<Image>();
			for (MultipartFile multipartFile : files) {
				filePath = FileUtils.createFile(multipartFile, rootPath, String.valueOf(service.getClient().getId()));
				if ("logo".equals(multipartFile.getName())) {
					service.setLogo(filePath);
				} else {
					Image image  = new Image();
					image.setImagePath(filePath);
					image.setCreateDt(currentDate);
					image.setUpdateDt(currentDate);
					image.setService(service);
					imageList.add(image);
				}
			}
			service.setCreateDt(currentDate);
			service.setUpdateDt(currentDate);
			
			if (imageList.size() > 0 ){
				service.setImageList(imageList);
				imageRepository.save(imageList);
				imageRepository.flush();
			}
			serviceRepository.saveAndFlush(service);
			
		} catch (Exception e) {
			logger.error("Error: " + e.getMessage());
			logger.error("Cause: " + e.getCause());
			throw e;
		}
		
		return filePath;
	}
}
