package utilities;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import base.BaseClass;

// TestNG Listener class used to monitor test execution events
public class TestListener implements ITestListener {

    // ==========================
    // Test Execution Events
    // ==========================

    // Executes before every test method starts
    @Override
    public void onTestStart(ITestResult result) {

        System.out.println("▶️ Starting execution: " + result.getName());
    }

    // Executes when a test case passes successfully
    @Override
    public void onTestSuccess(ITestResult result) {

        System.out.println("✅ Test Passed: " + result.getName());
    }

    // Executes automatically when a test case fails
    @Override
    public void onTestFailure(ITestResult result) {

        System.out.println("❌ Test Failed: " + result.getName());

        // Get the current test class instance
        BaseClass base = (BaseClass) result.getInstance();

        // Retrieve the current WebDriver instance
        WebDriver driver = base.getDriver();

        // Capture screenshot only if the browser is available
        if (driver != null) {

            ScreenshotUtils.takeScreenshot(driver, result.getName());
        }
    }

    // Executes when a test case is skipped
    @Override
    public void onTestSkipped(ITestResult result) {

        System.out.println("⏭️ Test Skipped: " + result.getName());
    }

    // ==========================
    // Test Suite Events
    // ==========================

    // Executes before the entire Test Suite starts
    @Override
    public void onStart(ITestContext context) {

        System.out.println("🚀 Test Suite Started");
    }

    // Executes after the entire Test Suite finishes
    @Override
    public void onFinish(ITestContext context) {

        System.out.println("🏁 Test Suite Finished");
    }
}
