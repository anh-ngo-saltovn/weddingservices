package com.weddingservices.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.weddingservices.service.ServiceService;

public class FileUtils {
	private static final Logger logger = LoggerFactory.getLogger(ServiceService.class);
	
	public static String createFile(MultipartFile file, String rootPath, String id) throws Exception {
		try {
			if (!file.isEmpty()) {
				String external = StringUtils.split(file.getOriginalFilename(), ".")[1];
				StringBuilder builder = new StringBuilder();
				builder.append(RandomStringUtils.randomAlphabetic(12));
				builder.append(".");
				builder.append(external);
				byte[] bytes = file.getBytes();
				String name = builder.toString();
				File dir = new File(rootPath + File.separator + id);
				if (!dir.exists())
					dir.mkdirs();
				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath()
						+ File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				logger.info("File is created "+ serverFile.getAbsolutePath());
				return id + File.separator + name;
			} else {
				return "";
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
