package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.NewsletterPage;
import utilities.ExcelUtils;

public class NewsletterTest extends BaseClass {

    @DataProvider(name = "newsletterData")
    public Object[][] getNewsletterData() {
        return ExcelUtils.getTestData("src/main/resources/testdata/TestData.xlsx", "NewsletterData", 2);
    }

    @Test(dataProvider = "newsletterData")
    public void testNewsletterSubscriptionDataDriven(String emailAddress, String expectedMessage) {
        HomePage homePage = new HomePage(driver);
        
        // 1. Navigate to the Newsletter page via the homepage link
        NewsletterPage newsletterPage = homePage.clickNewslettersMenuLink();
        
        // 2. Resolve structural test execution paths based on expected message types
        if (expectedMessage.equalsIgnoreCase("success")) {
            // Check if it's the long email scenario (TC_NS_006); if so, use the exact data parameter
            if (emailAddress.length() > 35) {
                newsletterPage.submitEmailWithAutoScroll(emailAddress);
            } else {
                // Generates a dynamic unique email address to ensure a clean subscription pass for short emails
                String uniqueEmail = "user" + System.currentTimeMillis() + "@gmail.com";
                newsletterPage.submitEmailWithAutoScroll(uniqueEmail);
            }
        } else {
            // Uses the designated static string (e.g., existing user or invalid text) from Excel
            newsletterPage.submitEmailWithAutoScroll(emailAddress);
        }

        // 3. Dynamic verification pass extracting live elements and formatting log outputs
        System.out.println("\n==================================================");
        System.out.println("📋 VERIFICATION RESULTS FOR INPUT: " + emailAddress);
        System.out.println("==================================================");

        if (expectedMessage.equalsIgnoreCase("success")) {
            String capturedSuccess = newsletterPage.getSuccessMessageText();
            Assert.assertFalse(capturedSuccess.isEmpty(), "Subscription process failed! Banner text was empty.");
            System.out.println("SUCCESS: \"" + capturedSuccess + "\"\n");
            
        } else if (expectedMessage.equalsIgnoreCase("Email already subscribed")) {
            // Retrieve whatever confirmation feedback text actually renders on the screen
            String duplicateBannerText = newsletterPage.getSuccessMessageText();
            
            System.out.println("ℹ️ DUPLICATE SUB CLASSIFICATION DETECTED");
            System.out.println("💬 Actual UI Feedback Received : \"" + duplicateBannerText.replace("\n", " ") + "\"");
            System.out.println("🎯 Expected UI Feedback Marker   : \"" + expectedMessage + "\"");

            // STRICT EXPLICIT BUG DETECTION FLAG: 
            // If the application displays a false success message for an existing account, fail the test immediately
            if (duplicateBannerText.contains("Thanks for signing up") || duplicateBannerText.contains("Success!")) {
                System.out.println("❌ BUG DETECTED: Application accepted a duplicate email address and displayed a false success banner!");
                Assert.fail("FAIL: Expected warning '" + expectedMessage + "' but the application displayed a false success banner: '" + duplicateBannerText.replace("\n", " ") + "'");
            } else {
                // Regular verification assertion pass if the application behaves correctly
                Assert.assertTrue(duplicateBannerText.toLowerCase().contains("already") || duplicateBannerText.equalsIgnoreCase(expectedMessage),
                    "Duplicate email submission failed to display an appropriate warning message!");
                System.out.println("SUCCESSFULLY VERIFIED DUPLICATE CONTROL FLOW.");
            }

        } else if (expectedMessage.equalsIgnoreCase("invalid")) {
            String errorMsg = newsletterPage.getErrorMessageText();
            Assert.assertFalse(errorMsg.isEmpty(), "Validation error message was not displayed!");
            System.out.println("INVALID DATA CHECK: \"" + errorMsg + "\"");
        }
        System.out.println("==================================================\n");
        
        // Clear layout overlay view bounds so subsequent execution threads run cleanly
        driver.navigate().refresh();
    }   
}
 
 