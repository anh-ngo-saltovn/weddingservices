package com.weddingservices.form;


import org.springframework.web.multipart.MultipartFile;

import com.weddingservices.model.Service;

public class ServiceForm extends Service{
	
	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
}
