package com.weddingservices.util;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.stereotype.Service;

import com.weddingservices.exceptions.CustomizeException;
import com.weddingservices.exceptions.resources.JsonMessage;
import com.weddingservices.model.OauthClientDetails;

@Service
public class UserAuthentication {
	
	public UserAuthentication(){
		
	}
	
	protected PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public String encodePassword(String rawPassword){
		String password= passwordEncoder.encode(rawPassword);
		return password;
	}
	
	public boolean matches(OauthClientDetails account, String rawPassword){
		return passwordEncoder.matches(rawPassword, account.getClient_secret());
	}
	
	/**
	 * 
	 * @param session
	 * @param id
	 */
	public void userValidate(HttpSession session){
		SecurityContextImpl securityContextImpl =  (SecurityContextImpl)session.getAttribute("SPRING_SECURITY_CONTEXT");
		try {
			if (securityContextImpl == null || securityContextImpl.getAuthentication() == null 
					|| securityContextImpl.getAuthentication().getPrincipal() == null) {
				OAuth2AccessDeniedException accessDeniedException = new OAuth2AccessDeniedException();
				throw new CustomizeException("Error "+ accessDeniedException.getSummary()
						,new JsonMessage(HttpStatus.FORBIDDEN.value(),
								accessDeniedException.getMessage()));
			}
		} catch (CustomizeException e) {
			throw e;
		} catch (Exception e) {
			throw new CustomizeException("Error "+ e.getCause()
				,new JsonMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					e.getMessage()));
		}
	}
}
