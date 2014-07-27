package com.gwt.wizard.client.util;

import org.junit.Assert;
import org.junit.Test;

public class EmailValidatorTest
{

    @Test
    public void test()
    {
        Assert.assertFalse(EmailValidator.isEmailValid(""));
        Assert.assertFalse(EmailValidator.isEmailValid(null));
        Assert.assertFalse(EmailValidator.isEmailValid("xx"));
        Assert.assertTrue(EmailValidator.isEmailValid("hall@hallservices.com"));
        Assert.assertTrue(EmailValidator.isEmailValid("hall@hall-services.com"));
    }
}
