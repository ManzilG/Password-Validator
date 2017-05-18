package com.service;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.repository.PasswordRepository;

public class PasswordService {
	private PasswordRepository passwordRepository;
	public static final String LENGTH_ERROR = "Password must be between 5 and 12 characters in length.";
	public static final String CASE_ERROR = "Password must only contain lowercase letters.";
	public static final String LETTERDIGIT_ERROR = "Password must contain both a letter and a digit.";
	public static final String ERROR_PASSWORD_SEQUENCE_REPEATED = "Password must not contain any sequence of characters immediately followed by the same sequence.";
	public static final String ERROR_RECENTPASSWORD = "Password must not be the last three of your password";

	private Pattern checkCase = Pattern.compile("[A-Z]");
	private Pattern checkLetterAndDigit = Pattern.compile("[a-z]+\\d+|\\d+[a-z]+");
	private Pattern checkSequenceRepetition = Pattern.compile("(\\w{2,})\\1");

	public PasswordRepository getPasswordRepository() {
		return passwordRepository;
	}

	public void setPasswordRepository(PasswordRepository passwordRepository) {
		this.passwordRepository = passwordRepository;
	}

	//Checking all the rules through this method
	public Set<String> validatePassword(String password) {
		Set<String> errors = new HashSet<String>();
		checkLength(errors, password);
		checkCase(errors, password);
		checkSequenceRepetition(errors, password);
		checkLetterAndDigit(errors, password);
		checkRecentPasswords(errors, password);
		
		//If no errors are found adding the validated password to the passwordslist in repository fur further checking and reference
		if (errors.isEmpty()) {
			passwordRepository.recordPasswords(password);
		}
		//System.out.println(passwordRepository.getPasswords());
		return errors;
	}

	public void checkLength(Set<String> errors, String password) {
		if (password.length() < 5 || password.length() > 12) {
			errors.add(LENGTH_ERROR);
		}

	}

	public void checkCase(Set<String> errors, String password) {
		Matcher matcher = checkCase.matcher(password);
		if (matcher.find()) {
			errors.add(CASE_ERROR);
		}

	}

	public void checkSequenceRepetition(Set<String> errors, String password) {
		Matcher matcher = checkSequenceRepetition.matcher(password);
		if (matcher.find()) {
			errors.add(ERROR_PASSWORD_SEQUENCE_REPEATED);
		}

	}

	public void checkLetterAndDigit(Set<String> errors, String password) {
		Matcher matcher = checkLetterAndDigit.matcher(password);
		if (!matcher.find()) {
			errors.add(LETTERDIGIT_ERROR);
		}
	}

	public void checkRecentPasswords(Set<String> errors, String password) {
		int count = 0;
		List<String> passwords = passwordRepository.getPasswords();
		//System.out.println(passwords);
		
		//Checking last 3 items in list that are recenly added using listiterator
		ListIterator<String> li=passwords.listIterator(passwords.size());
		
		while(li.hasPrevious() && count<=3){
			//System.out.println(passwords);
			String check=li.previous();
			//System.out.println(check);
			if(check.equals(password)){
				errors.add(ERROR_RECENTPASSWORD);
				break;
			}
			count++;
		}
	}

}
