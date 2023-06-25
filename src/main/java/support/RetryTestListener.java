package support;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class RetryTestListener extends TestListenerAdapter {
	
	@Override
	public void onTestFailure(ITestResult result) {
		if (result.getMethod().getRetryAnalyzer(result).retry(result)) {
			result.setStatus(ITestResult.SKIP);
		}
	}

}
