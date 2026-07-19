package utilities;

// Import TestNG listener interfaces and result classes
import org.testng.*;

// Import Extent Reports classes
import com.aventstack.extentreports.*;

// Import BaseClass to access the current WebDriver instance
import base.BaseClass;

public class TestListener implements ITestListener {

    // Create a single ExtentReports instance
    // This report object is shared throughout the test execution
    private static ExtentReports extent =
            ExtentManager.getInstance();

    // =====================================================
    // Executes before every @Test method starts
    // =====================================================
    @Override
    public void onTestStart(ITestResult result) {

        // Create a new test entry in the Extent Report
        // using the current test method name
        ExtentTest test =
                extent.createTest(
                        result.getMethod().getMethodName());

        // Store the ExtentTest object in ThreadLocal
        // for parallel execution support
        ExtentTestManager.setTest(test);
    }

    // =====================================================
    // Executes when a test passes successfully
    // =====================================================
    @Override
    public void onTestSuccess(ITestResult result) {

        // Log PASS status in the Extent Report
        ExtentTestManager.getTest().pass("Test Passed");
    }

    // =====================================================
    // Executes when a test fails
    // =====================================================
    @Override
    public void onTestFailure(ITestResult result) {

        // Log the failure along with the exception details
        ExtentTestManager.getTest().fail(result.getThrowable());

        // Get the current test class instance
        BaseClass base =
                (BaseClass) result.getInstance();

        // Capture screenshot using the current WebDriver
        String path =
                ScreenshotUtils.takeScreenshot(
                        base.getDriver(),
                        result.getMethod().getMethodName());

        try {

            // Attach the captured screenshot
            // to the Extent Report
            ExtentTestManager.getTest()
                    .addScreenCaptureFromPath(path);

        } catch (Exception ignored) {

            // Ignore screenshot attachment errors
            // to avoid interrupting report generation
        }
    }

    // =====================================================
    // Executes when a test is skipped
    // =====================================================
    @Override
    public void onTestSkipped(ITestResult result) {

        // Log SKIP status in the report
        ExtentTestManager.getTest().skip("Test Skipped");
    }

    // =====================================================
    // Executes after all tests in the suite finish
    // =====================================================
    @Override
    public void onFinish(ITestContext context) {

        // Write all test results to the HTML report
        extent.flush();

        // Remove the ThreadLocal ExtentTest object
        // to prevent memory leaks
        ExtentTestManager.unload();
    }
}
