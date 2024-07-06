package com.example.demo.sdk.req;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.Test;

class SignUpReqTest {

    static final Validator v = Validation.buildDefaultValidatorFactory().getValidator();

    SignUpReq forValidateEmail(String email) {
        return SignUpReq.builder().name("ValidUser123").password("Valid1@Password").email(email).build();
    }    

    SignUpReq forValidatePassword(String password) {
        return SignUpReq.builder().name("ValidUser123").password(password).email("valid.email@example.com").build();
    }

    SignUpReq forValidateName(String name) {
        return SignUpReq.builder().name(name).password("Valid1@Password").email("valid.email@example.com").build();
    }

    @Test
    void testValidEmail() {
        var req = forValidateEmail("valid.email@example.com");
        assertTrue(v.validateProperty(req, "email").isEmpty(), "Valid email should pass without errors.");
    }

    @Test
    void testInvalidEmailFormat() {
        var req = forValidateEmail("invalid-email");
        assertFalse(v.validateProperty(req, "email").isEmpty(), "Invalid email format should fail.");
    }

    @Test
    void testEmptyEmail() {
        var req = forValidateEmail("");
        assertFalse(v.validateProperty(req, "email").isEmpty(), "Empty email should fail validation.");
    }

    @Test
    void testValidPassword() {
        var req = forValidatePassword("Valid1@Password");
        assertTrue(v.validate(req).isEmpty(), "Valid password should pass without errors.");
    }

    @Test
    void testPasswordTooShort() {
        var req = forValidatePassword("Sho1@");
        assertFalse(v.validateProperty(req, "password").isEmpty(), "Password too short should fail.");
    }

    @Test
    void testPasswordTooLong() {
        var req = forValidatePassword("VeryLong1@Password1234567890");
        assertFalse(v.validateProperty(req, "password").isEmpty(), "Password too long should fail.");
    }

    @Test
    void testPasswordMissingUppercase() {
        var req = forValidatePassword("valid1@password");
        assertFalse(v.validateProperty(req, "password").isEmpty(), "Password missing uppercase should fail.");
    }

    @Test
    void testPasswordMissingLowercase() {
        var req = forValidatePassword("VALID1@PASSWORD");
        assertFalse(v.validateProperty(req, "password").isEmpty(), "Password missing lowercase should fail.");
    }

    @Test
    void testPasswordMissingNumber() {
        var req = forValidatePassword("Valid@Password");
        assertFalse(v.validateProperty(req, "password").isEmpty(), "Password missing number should fail.");
    }

    @Test
    void testPasswordMissingSpecialCharacter() {
        var req = forValidatePassword("Valid1Password");
        assertFalse(v.validateProperty(req, "password").isEmpty(), "Password missing special character should fail.");
    }

    @Test
    void testEmptyPassword() {
        var req = forValidatePassword("");
        assertFalse(v.validateProperty(req, "password").isEmpty(), "Empty password should fail validation.");
    }


    @Test
    void testValidName() {
        var req = forValidateName("ValidUser123");
        assertTrue(v.validate(req).isEmpty(), "Valid name should pass without errors.");
    }

    @Test
    void testNameTooShort() {
        var req = forValidateName("Usr");
        assertFalse(v.validateProperty(req, "name").isEmpty(), "Name too short should fail.");
    }

    @Test
    void testNameTooLong() {
        var req = forValidateName("VeryLongName1234567890");
        assertFalse(v.validateProperty(req, "name").isEmpty(), "Name too long should fail.");
    }

    @Test
    void testNameWithInvalidCharacters() {
        var req = forValidateName("User@Name!");
        assertFalse(v.validateProperty(req, "name").isEmpty(), "Name with invalid characters should fail.");
    }

    @Test
    void testEmptyName() {
        var req = forValidateName("");
        assertFalse(v.validateProperty(req, "name").isEmpty(), "Empty name should fail validation.");
    }

}
