package support;


/**
 * @author Arjun
 */

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ReusableMethods extends ExtentManager {
	
	public void explicitWait(By obj) {
		WebDriverWait wait = new WebDriverWait(webDriver.getDriver(), Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(obj));
	}
	
	public void click(By obj, String objDesc) throws IOException {
		try {
			explicitWait(obj);
			webDriver.getDriver().findElement(obj).click();
			testReport("pass", objDesc+" clicked successfully");
		} catch (Exception e) {
			testReport("fail", objDesc+" not clicked successfully - "+e.getMessage());
			error++;
			throw new RuntimeException("Could not click "+objDesc+" - "+e.getMessage());
		}
	}
	
	public void sendKeys(By obj, String strObj, String objDesc) throws IOException {
		WebElement element = webDriver.getDriver().findElement(obj);
		try {
			explicitWait(obj);
			element.clear();
			element.sendKeys(strObj.trim());
			testReport("pass", strObj+" entered successfully in "+objDesc);
		} catch (Exception e) {
			testReport("fail", strObj+" is not entered in "+objDesc+" - "+e.getMessage());
			error++;
			throw new RuntimeException("Could not enter "+strObj+" in "+objDesc+" - "+e.getMessage());
		}
	}
	
	public boolean isDisplayed(By obj, String objDesc) throws IOException {
		boolean displayed = false;
		try {
			explicitWait(obj);
			displayed = webDriver.getDriver().findElement(obj).isDisplayed();
			if (displayed) {
				testReport("pass", objDesc+" is displayed successfully");
			} else {
				testReport("fail", objDesc+" is not displayed");
			}
			return displayed;
		} catch (Exception e) {
			testReport("fail", objDesc+" is not displayed - " + e.getMessage());
			error++;
			throw new RuntimeException("Could not click "+objDesc+" - "+e.getMessage());
			//return displayed;
		}
	}

}
