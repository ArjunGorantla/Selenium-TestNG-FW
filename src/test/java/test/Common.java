package test;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import support.Excel;
import support.ExtentManager;
import support.RetryAnalyzer;
import support.RetryTestListener;

/**
 * @author Arjun
 */

import support.ReusableMethods;

@Listeners (RetryTestListener.class)
public class Common extends ReusableMethods {
	
	public Excel xl = new Excel(".\\src\\test\\resources\\RunManager.xlsx");
	public final String TESTDATA = "TestData";
	public final String RUN_MANAGER = "RunManager";
	
	public void launchBrowser(String url, String browser) throws Exception {
		testReport("info","Starting test case - " + webDriver.getTestCaseName() + " in " + browser + " browser");
		webDriver.setBrowser(browser, url);
		testReport("info","Launched browser successfully");
	}
	
	@Test(retryAnalyzer = RetryAnalyzer.class)
	public void test01() throws Exception {
		webDriver.setTestCaseName("LaunchEdge");
		launchBrowser("https://www.google.com/", "Edge");
		System.out.println(webDriver.getDriver().getCurrentUrl());
		webDriver.getDriver().findElement(By.xpath("//textarea[@class='gLFyf']")).
			sendKeys("GMAIL", Keys.ENTER);
		testReport( "pass", "Search successful");
	}
	
	@Test(retryAnalyzer = RetryAnalyzer.class)
	public void test02() throws Exception {
		webDriver.setTestCaseName("LaunchChrome");
		launchBrowser("https://www.google.com/", "Chrome");
		System.out.println(webDriver.getDriver().getCurrentUrl());
		webDriver.getDriver().findElement(By.xpath("//textarea[@class='gLFyf']")).
			sendKeys("GMAIL", Keys.ENTER);
		testReport( "pass", "Search successful");
	}
	
	@AfterMethod (alwaysRun = true)
	public void quitBrowser() throws IOException {
		testReport("info","Ending test case - "+ webDriver.getTestCaseName());
		testReport("end",null);
		webDriver.closeDriver();
	}
	
	@AfterSuite (alwaysRun = true) 
	public void closeReport(){
		ExtentManager.extent.flush();
		if (error!=0) {
			Assert.fail();
		}
	}

}
