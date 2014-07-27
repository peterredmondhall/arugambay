package com.gwt.wizard.test;

import java.io.File;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.gwt.wizard.test.page.TransportPage;

public class CreateBookingTest
{
    private WebDriver driver;
    private final String baseUrl = "http://127.0.0.1:8888";
    private final String chromeDriverPath = "e:/chromedriver/chromedriver.exe";
    private final String gwtChromeDevCrxPath = "e:/chromedriver/extension_1_0_11357.crx";

    @Before
    public void setUp() throws Exception
    {
        File chromeDriverFile = new File(chromeDriverPath);
        File gwtChromeDevCrxFile = new File(gwtChromeDevCrxPath);
        ChromeOptions options = new ChromeOptions();
        options.addExtensions(gwtChromeDevCrxFile);
        DesiredCapabilities desired = DesiredCapabilities.chrome();
        desired.setCapability("chrome.switches", Arrays.asList("--start-maximized"));
        desired.setCapability(ChromeOptions.CAPABILITY, options); // add the gwt dev plugin
        ChromeDriverService chromeService = new ChromeDriverService.Builder().usingAnyFreePort().usingDriverExecutable(chromeDriverFile).build();
        chromeService.start();
        driver = new RemoteWebDriver(chromeService.getUrl(), desired);
    }

    @Test
    public void testBookingTestCase() throws Exception
    {

        driver.get(baseUrl + "/index.html");
        TransportPage transportPage = new TransportPage(driver);

        transportPage.datetransport.click();
        driver.findElement(By.xpath("//tr[6]/td[6]/div")).click();

        transportPage.forwardTimeBox.selectByVisibleText("8:00");
        transportPage.returnTimeBox.selectByVisibleText("9:00");
        transportPage.totalPassengersBox.selectByVisibleText("10");
        transportPage.passengersWithRollatorBox.selectByVisibleText("8");
        transportPage.passengersWithFoldableWheelchairBox.selectByVisibleText("2");
        transportPage.passengersWithheelchairTransportBox.selectByVisibleText("0");

        transportPage.wizardPage.buttonNext.click();

//        driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
        driver.findElement(By.cssSelector("input.gwt-TextBox")).clear();
        driver.findElement(By.cssSelector("input.gwt-TextBox")).sendKeys("Organizer_Selenium_Test");
        driver.findElement(By.xpath("(//input[@type='text'])[3]")).clear();
        driver.findElement(By.xpath("(//input[@type='text'])[3]")).sendKeys("Organizer_Selenium_Test_Email");
        driver.findElement(By.xpath("(//input[@type='text'])[4]")).clear();
        driver.findElement(By.xpath("(//input[@type='text'])[4]")).sendKeys("Companion_Selenium_Test");
        driver.findElement(By.xpath("(//input[@type='text'])[5]")).clear();
        driver.findElement(By.xpath("(//input[@type='text'])[5]")).sendKeys("Companion_Selenium_TestEmail");
        driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
        driver.findElement(By.cssSelector("textarea.gwt-TextArea")).clear();
        driver.findElement(By.cssSelector("textarea.gwt-TextArea")).sendKeys("ABC");
        driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
        driver.findElement(By.xpath("(//button[@type='button'])[2]")).click();
        driver.findElement(By.xpath("(//button[@type='button'])[3]")).click();
        driver.close();
    }
}
