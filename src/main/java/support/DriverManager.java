package support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arjun
 */

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {
	
	public DriverManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
	protected ThreadLocal<String> testCaseName = new ThreadLocal<String>();
	protected ThreadLocal<Integer> stepNum = new ThreadLocal<Integer>();
	
	private static DriverManager instance = new DriverManager();

	/**
	 * Method Description : getInstance
	 *
	 * @return
	 */
	public static DriverManager getInstance() {
		return instance;
	}
	
	public void setBrowser(String browser, String url) throws InterruptedException {
		if (browser.equalsIgnoreCase("CHROME")) {
			chrome();
		} else if (browser.equalsIgnoreCase("EDGE")) {
			edge();
		} else {
			throw new RuntimeException("Undefined browser");
		}
		getDriver().manage().window().maximize();
		getDriver().get(url);
		Thread.sleep(2500);
	}
	
	public void chrome() {
		WebDriverManager.chromedriver().setup();
		setDriver(new ChromeDriver());
	}
	
	public void edge() {
		WebDriverManager.edgedriver().setup();
		setDriver(new EdgeDriver());
	}
	
	public void setDriver(WebDriver driver) {
		getInstance().webDriver.set(driver);
	}
	
	public WebDriver getDriver() {
		return getInstance().webDriver.get();
	}
	
	public void closeDriver() {
		getInstance().webDriver.get().quit();
		getInstance().webDriver.remove();
	}
	
	public void setTestCaseName(String testCase) {
		getInstance().testCaseName.set(testCase);
	}
	
	public String getTestCaseName() {
		return getInstance().testCaseName.get();
	}
	
	public void setStepNum(int stepNumber) {
		getInstance().stepNum.set(stepNumber);
	}
	
	public int getStepNum() {
		return getInstance().stepNum.get();
	}

}
