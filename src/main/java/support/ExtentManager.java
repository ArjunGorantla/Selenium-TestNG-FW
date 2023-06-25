package support;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	public static ExtentReports extent = new ExtentReports();
	public final File CONF = new File("./extentreport.xml");
	public static ThreadLocal<ExtentTest> extentTestThread = new ThreadLocal<>();
	protected DriverManager webDriver = new DriverManager();
	protected Logger log = LogManager.getLogger(Logger.class.getName());
	int stepNum = 0;
	final String TCNAME = "TestCaseName";
	final String KEYSEPARATOR = "=";
	final String DELIMITER = "|";
	final String STEP = "Step";
	final String STATUS = "Status";
	final String MESSAGE = "LogMessage";
	protected int error = 0;
	private static ExtentManager instance = new ExtentManager();
	
	public ExtentManager getInstance() {
		return instance;
	}
	
	public static ExtentTest getTestCase() {
		return extentTestThread.get();
	}
	
	public static void setTestCase(ExtentTest test) {
		extentTestThread.set(test);
	}

	public ExtentReports createExtentReport() throws IOException, ParseException {
		ExtentSparkReporter spark = new ExtentSparkReporter("./target/index.html");
		extent.attachReporter(spark);
		spark.config(ExtentSparkReporterConfig.builder().build());
		spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle("Test Report");
		spark.config().setReportName("AUT Test Report");
		spark.loadXMLConfig(CONF);
		return extent;
	}

	ExtentTest test;
	
	public void testReport(String stepStatus, String comments) throws IOException {
		try {
			if (stepStatus.equalsIgnoreCase("END")) {
				test = null;
				setTestCase(test);
			} else if (getTestCase() == null) {
				try {
					test = createExtentReport().createTest(webDriver.getTestCaseName()).assignAuthor("Arjun").
							assignDevice("Web Browser");
					setTestCase(test);
					webDriver.setStepNum(1);
				} catch (java.text.ParseException pE) {
					throw new RuntimeException();
				}
			}
			if (test!=null) {
				stepReport(test, stepStatus, comments);
			}
		} catch (NullPointerException nE) {
			throw new NullPointerException("Test case name not defined");
		}
	}

	private void stepReport(ExtentTest test, String stepStatus, String comments) {
		if (stepStatus.equalsIgnoreCase("PASS")) {
			test.pass(comments, MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshot()).build());
			log.info(formatLog(stepStatus, comments));
			webDriver.setStepNum(webDriver.getStepNum()+1);
		} else if (stepStatus.equalsIgnoreCase("FAIL")) {
			test.fail(comments, MediaEntityBuilder.createScreenCaptureFromBase64String(getScreenshot()).build());
			log.info(formatLog(stepStatus, comments));
			webDriver.setStepNum(webDriver.getStepNum()+1);
			error++;
		} else if (stepStatus.equalsIgnoreCase("INFO")) {
			test.info(comments);
			log.info(formatLog(stepStatus, comments));
			webDriver.setStepNum(webDriver.getStepNum()+1);
		} else if (stepStatus.equalsIgnoreCase("SKIP")) {
			test.skip(comments);
			log.info(formatLog(stepStatus, comments));
			webDriver.setStepNum(webDriver.getStepNum()+1);
		}
	}
	
	public String getScreenshot() {
		return ((TakesScreenshot)webDriver.getDriver()).getScreenshotAs(OutputType.BASE64);
	}
	
	private String formatLog(String status, String comments){
		 StringBuilder formattedString = new StringBuilder(TCNAME);
		 formattedString.append(KEYSEPARATOR);
		 formattedString.append(webDriver.getTestCaseName());
		 formattedString.append(DELIMITER);
		 formattedString.append(STEP);
		 formattedString.append(KEYSEPARATOR);
		 formattedString.append(String.valueOf(webDriver.getStepNum()));
		 formattedString.append(DELIMITER);
		 formattedString.append(STATUS);
		 formattedString.append(KEYSEPARATOR);
		 formattedString.append(status.toUpperCase());
		 formattedString.append(DELIMITER);
		 formattedString.append(MESSAGE);
		 formattedString.append(KEYSEPARATOR);
		 formattedString.append(comments);
		 formattedString.append(DELIMITER);
		 
		 return formattedString.toString();
	 }

}
