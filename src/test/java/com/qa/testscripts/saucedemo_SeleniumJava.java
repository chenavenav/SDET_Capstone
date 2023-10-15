package com.qa.testscripts;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class saucedemo_SeleniumJava {
	WebDriver driver;

	/* To capture screen shots */

	public void captureScreenShot(String filename) throws IOException {
		String screenshot = "ScreenShot" + filename + ".png";
		TakesScreenshot ts = (TakesScreenshot) driver;
		File driverSrcImg = ts.getScreenshotAs(OutputType.FILE);
		File driverTargetImg = new File(System.getProperty("user.dir") + "/Reports/Screenshots/" + screenshot);
		FileUtils.copyFile(driverSrcImg, driverTargetImg);
	}

	/* Initializing URL */

	@BeforeSuite

	void startURL() {
		WebDriverManager.edgedriver().setup();

		driver = new EdgeDriver();

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get("https://www.saucedemo.com/");

	}

	@Test(priority = 0)
	void validate_title() {

		/* verifying the Title using Assertions */
		String title = driver.getTitle();
		String expectedtitle = "Swag Labs";
		Assert.assertEquals("sauce demo login page launched", expectedtitle, title);
	}

	/* verifying login page */

	@Parameters({ "Username", "Password" })
	@Test(priority = 1)
	void loginwithvalid(String Username, String Password) throws IOException {
		WebElement usernametxtbox = driver.findElement(By.id("user-name"));
		WebElement passwordtxtbox = driver.findElement(By.id("password"));
		WebElement loginbutton = driver.findElement(By.id("login-button"));

		usernametxtbox.sendKeys(Username);
		passwordtxtbox.sendKeys(Password);
		loginbutton.click();

		/*
		 * Verifying that User is navigated to right page using TestNG Assertions for
		 * Positive Case
		 */
		String ActualResult = driver.getCurrentUrl();
		String ExpectedResult = "https://www.saucedemo.com/inventory.html";
		Assert.assertEquals(ExpectedResult, ActualResult);
		captureScreenShot("PossitiveTest");
		driver.findElement(By.id("react-burger-menu-btn")).click();
		driver.findElement(By.id("logout_sidebar_link")).click();

	}

	@Parameters({ "InvalidUsername", "InvalidPassword" })
	@Test(priority = 2)
	void loginwithinvalid(String InvalidUsername, String InvalidPassword) throws IOException {

		WebElement usernametxtbox = driver.findElement(By.id("user-name"));
		WebElement passwordtxtbox = driver.findElement(By.id("password"));
		WebElement loginbutton = driver.findElement(By.id("login-button"));

		usernametxtbox.sendKeys(InvalidUsername);
		passwordtxtbox.sendKeys(InvalidPassword);
		loginbutton.click();
		captureScreenShot("NegativeTest");

		/* Verifying the Error displayed using TestNG Assertions for Negative Case */
		String error_text = driver.findElement(By.xpath("//h3[@data-test=\"error\"]")).getText();
		Assert.assertEquals("Epic sadface: Username and password do not match any user in this service", error_text);
	}

	@AfterSuite
	void closewindow() {
		driver.close();
	}

}
