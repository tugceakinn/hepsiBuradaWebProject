package com.saha.hepsiburada.hook;

import com.saha.slnarch.common.helper.StringHelper;
import com.saha.slnarch.common.helper.SystemPropertyHelper;
import com.saha.slnarch.core.helper.ConfigurationHelper;
import com.saha.slnarch.di.page.InjectablePageTestImpl;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class hookImpl extends InjectablePageTestImpl {
    public hookImpl() {
        super();
        inject();
    }

    @BeforeScenario
    public void beforeScenario() {
        logger.debug("Before Scenario");
        ChromeOptions options = new ChromeOptions();
        getDriver().manage().window().fullscreen();
        getDriver().navigate().to(ConfigurationHelper.INSTANCE.getBaseUrl());
        options.addArguments("--disable-notifications");
    }

    @AfterScenario
    public void afterScenario() {
        logger.debug("After Scenario");
        if(getDriver()!=null) {
            getDriver().quit();
        }
    }

    @Override public Capabilities getCapabilities() {
        DesiredCapabilities capabilities = null;
        if (!StringHelper.isEmpty(System.getenv("key"))) {
            SystemPropertyHelper.setProperty("key", System.getenv("key"));
            capabilities = new DesiredCapabilities();
            capabilities.setCapability("key", System.getenv("key"));
        }
        return capabilities;
    }
}
