package com.test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.controller.PasswordController;
import com.service.PasswordService;

public class PasswordValidatorTest {
	private PasswordService passwordService;
	
	@Before
    public void getServiceClassInstance() {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		passwordService = (PasswordService) context.getBean("service");

    }
	
	@Test
    public void testContainsOnlyLowercaseLetters() {
		Set<String> errors = passwordService.validatePassword("abcde");
        assertFalse(errors.contains(passwordService.CASE_ERROR));
    }
	
	@Test
    public void testContainsUppercaseLetters() {
        Set<String> errors = passwordService.validatePassword("aBcde");
       assertTrue(errors.contains(passwordService.CASE_ERROR));
    }
	
	@Test
    public void testLengthLessThanFive() {
        Set<String> errors = passwordService.validatePassword("1234");
       assertTrue(errors.contains(passwordService.LENGTH_ERROR));
    }
	@Test
    public void testLengthMoreThanTwelve() {
        Set<String> errors = passwordService.validatePassword("12345675abcdefg");
       assertTrue(errors.contains(passwordService.LENGTH_ERROR));
    }
	@Test
    public void lengthOkay() {
        Set<String> errors = passwordService.validatePassword("12345675");
       assertFalse(errors.contains(passwordService.LENGTH_ERROR));
    }
	
	
	@Test
    public void testAllOkay() {
        Set<String> errors = passwordService.validatePassword("manzil123");
       assertTrue(errors.isEmpty());
    }
	
	@Test
    public void containsOnlyLetterAndDigit() {
        Set<String> errors = passwordService.validatePassword("m3");
       assertFalse(errors.contains(passwordService.LETTERDIGIT_ERROR));
    }
	
	@Test
    public void containsOnlyLetter() {
        Set<String> errors = passwordService.validatePassword("abcdefg");
       assertThat(errors,hasItem(passwordService.LETTERDIGIT_ERROR));
    }
	
	@Test
    public void containsOnlyDigit() {
        Set<String> errors = passwordService.validatePassword("1234567");
       assertTrue(errors.contains(passwordService.LETTERDIGIT_ERROR));
    }
	
	@Test
    public void SequenceNotViolated() {
        Set<String> errors = passwordService.validatePassword("ab1234");
        assertFalse(errors.contains(passwordService.ERROR_PASSWORD_SEQUENCE_REPEATED));
    }

	@Test
    public void SequenceRepeated() {
        Set<String> errors = passwordService.validatePassword("abcabc12");
        assertTrue(errors.contains(passwordService.ERROR_PASSWORD_SEQUENCE_REPEATED));
    }
	
	@Test
    public void recentPasswords() {
		passwordService.getPasswordRepository().recordPasswords("manzil1");
		passwordService.getPasswordRepository().recordPasswords("manzil2");
		passwordService.getPasswordRepository().recordPasswords("manzil3");
        Set<String> errors = passwordService.validatePassword("manzil1");
        assertTrue(errors.contains(passwordService.ERROR_RECENTPASSWORD));
    }
	
	@Test
    public void recentPasswordsInMiddle() {
		passwordService.getPasswordRepository().recordPasswords("manzil1");
		passwordService.getPasswordRepository().recordPasswords("manzil234");
		passwordService.getPasswordRepository().recordPasswords("manzil3");
        Set<String> errors = passwordService.validatePassword("manzil234");
        assertTrue(errors.contains(passwordService.ERROR_RECENTPASSWORD));
    }
	
	@Test
    public void recentPasswordsAll() {
		passwordService.getPasswordRepository().recordPasswords("manzil1");
		passwordService.getPasswordRepository().recordPasswords("manzil23");
		passwordService.getPasswordRepository().recordPasswords("manzil3");
        Set<String> errors = passwordService.validatePassword("manzil2345");
        assertFalse(errors.contains(passwordService.ERROR_RECENTPASSWORD));
    }


}
