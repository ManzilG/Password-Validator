package com.controller;

import java.util.Set;

import com.service.PasswordService;

public class PasswordController {
	private PasswordService passwordService;

	public PasswordService getPasswordService() {
		return passwordService;
	}

	public void setPasswordService(PasswordService passwordService) {
		this.passwordService = passwordService;
	}
	
	//Delegating the request to service to validate the password
	public Set<String> validatePassword(String password){
		return passwordService.validatePassword(password);
		
	}
	
}
