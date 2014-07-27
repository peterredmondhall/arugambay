package com.gwt.wizard.test;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckBookingTest
{
    private WebDriver driver;
    private final String baseUrl = "http://127.0.0.1:8888";
    private final String chromeDriverPath = "D:/chromedriver_win32/chromedriver.exe";
    private final String gwtChromeDevCrxPath = "D:/chromedriver_win32/extension_1_0_11357.crx";

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
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testBookingTestCase() throws Exception
    {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        driver.get(baseUrl + "/dashboard.html");
        driver.findElement(By.cssSelector("button.loginBtn")).click();
        driver.findElement(By.id("isAdmin")).click();
        driver.findElement(By.name("action")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[2]/div")));
        Assert.assertEquals("8:00", driver.findElement(By.xpath("//td[6]/div")).getText());
        Assert.assertEquals("9:00", driver.findElement(By.xpath("//td[8]/div")).getText());
        Assert.assertEquals("Companion_Selenium_Test", driver.findElement(By.xpath("//td[9]/div")).getText());
        Assert.assertEquals("Companion_Selenium_TestEmail", driver.findElement(By.xpath("//td[10]/div")).getText());
        Assert.assertEquals("Organizer_Selenium_Test", driver.findElement(By.xpath("//td[11]/div")).getText());
        Assert.assertEquals("Organizer_Selenium_Test_Email", driver.findElement(By.xpath("//td[12]/div")).getText());
        Assert.assertEquals("6", driver.findElement(By.xpath("//td[13]/div")).getText());
        Assert.assertEquals("4", driver.findElement(By.xpath("//td[14]/div")).getText());
        Assert.assertEquals("14", driver.findElement(By.xpath("//td[15]/div")).getText());
        Assert.assertEquals("9", driver.findElement(By.xpath("//td[16]/div")).getText());
        Assert.assertEquals("ABC", driver.findElement(By.xpath("//td[17]/div")).getText());
        driver.close();
    }
}
