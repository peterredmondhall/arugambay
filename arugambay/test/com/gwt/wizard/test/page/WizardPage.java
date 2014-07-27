package com.gwt.wizard.test.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WizardPage
{
    public WebElement buttonNext;

    public WizardPage(WebDriver driver)
    {
        buttonNext = driver.findElement(By.id("gwt-debug-button_next"));
    }

}
