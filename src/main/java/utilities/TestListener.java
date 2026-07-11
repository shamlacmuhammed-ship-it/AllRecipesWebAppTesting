package utilities;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import base.BaseClass;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("▶️ Starting execution: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {

        System.out.println("❌ Test Failed: " + result.getName());

        BaseClass base = (BaseClass) result.getInstance();

        WebDriver driver = base.getDriver();

        if (driver != null) {
            ScreenshotUtils.takeScreenshot(driver, result.getName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⏭️ Test Skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("🚀 Test Suite Started");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("🏁 Test Suite Finished");
    }
}