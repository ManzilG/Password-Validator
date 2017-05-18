package com.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class PasswordRepository {
	List<String> passwords;
	
	public List<String> getPasswords() {
		return passwords;
	}

		public void setPasswords(List<String> passwords) {
		this.passwords = passwords;
	}


		//Adding validated passwords got from service to store so that last 3 passwords could be checked
	public void recordPasswords(String password){
		passwords.add(password);
	}
	
	

}
