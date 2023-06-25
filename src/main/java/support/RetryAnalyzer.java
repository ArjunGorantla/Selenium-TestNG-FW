package support;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
	
	int retryCounter = 0;
	int maxRetryLimit;
	
	public boolean retry(ITestResult result) {
		try {
			maxRetryLimit = Integer.parseInt(System.getProperty("surefire.rerunFailingTestsCount"));
		} catch (Exception e) {
			maxRetryLimit = 1;
		}
		if (!result.isSuccess() && retryCounter < maxRetryLimit) {
			retryCounter++;
			return true;
		}
		return false;
	}

}
