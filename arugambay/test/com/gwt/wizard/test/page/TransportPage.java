package com.gwt.wizard.test.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class TransportPage
{
    public WizardPage wizardPage;
    public WebElement datetransport;

    public Select forwardPickupBox;
    public Select forwardTimeBox;
    public Select returnTimeBox;
    public Select returnPickupBox;
    public WebElement rb_oneway;
    public WebElement rb_return;
    public Select totalPassengersBox;
    public Select passengersWithRollatorBox;
    public Select passengersWithFoldableWheelchairBox;
    public Select passengersWithheelchairTransportBox;

    public TransportPage(WebDriver driver)
    {
        wizardPage = new WizardPage(driver);
        datetransport = driver.findElement(By.id("gwt-debug-transport_date_id"));
        forwardPickupBox = new Select(driver.findElement(By.id("gwt-debug-transport_forward_pickup_id")));
        forwardTimeBox = new Select(driver.findElement(By.id("gwt-debug-transport_forward_time_id")));
        returnTimeBox = new Select(driver.findElement(By.id("gwt-debug-transport_return_time_id")));
        returnPickupBox = new Select(driver.findElement(By.id("gwt-debug-transport_return_pickup_id")));
        rb_oneway = driver.findElement(By.id("gwt-debug-transport_rb_oneway_id"));
        rb_return = driver.findElement(By.id("gwt-debug-transport_rb_return_id"));
        totalPassengersBox = new Select(driver.findElement(By.id("gwt-debug-transport_pax_id")));
        passengersWithRollatorBox = new Select(driver.findElement(By.id("gwt-debug-transport_pax_rollator_id")));
        passengersWithFoldableWheelchairBox = new Select(driver.findElement(By.id("gwt-debug-transport_pax_foldable_id")));
        passengersWithheelchairTransportBox = new Select(driver.findElement(By.id("gwt-debug-transport_pax_wheelchair_id")));

    }

}
