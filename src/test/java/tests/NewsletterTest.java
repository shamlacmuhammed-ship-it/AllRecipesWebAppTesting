package tests;

import base.BaseClass;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.NewsletterPage;
import utilities.ExcelUtils;

// Test class for Newsletter Subscription functionality
public class NewsletterTest extends BaseClass {

    // ==========================================
    // Data Provider
    // Reads test data from Excel
    // ==========================================
    @DataProvider(name = "newsletterData")
    public Object[][] getNewsletterData() {

        return ExcelUtils.getTestData(
                "src/main/resources/testdata/TestData.xlsx",
                "NewsletterData",
                2);
    }

    // ==========================================
    // Data-Driven Newsletter Subscription Test
    // ==========================================
    @Test(dataProvider = "newsletterData")
    public void testNewsletterSubscriptionDataDriven(String emailAddress,
                                                     String expectedMessage) {

        // Create Home Page object
        HomePage homePage = new HomePage(driver);

        // Navigate to Newsletter page
        NewsletterPage newsletterPage =
                homePage.clickNewslettersMenuLink();

        // ==========================================
        // Submit email based on test scenario
        // ==========================================

        if (expectedMessage.equalsIgnoreCase("success")) {

            // Use original email for long-email test case
            if (emailAddress.length() > 35) {

                newsletterPage.submitEmailWithAutoScroll(emailAddress);

            } else {

                // Generate unique email to avoid duplicate subscription
                String uniqueEmail =
                        "user"
                                + System.currentTimeMillis()
                                + "@gmail.com";

                newsletterPage.submitEmailWithAutoScroll(uniqueEmail);
            }

        } else {

            // Submit invalid or duplicate email from Excel
            newsletterPage.submitEmailWithAutoScroll(emailAddress);
        }

        // ==========================================
        // Display execution details in console
        // ==========================================

        System.out.println("\n==========================================");
        System.out.println("Verification for : " + emailAddress);
        System.out.println("==========================================");

        // ==========================================
        // Verify Successful Subscription
        // ==========================================

        if (expectedMessage.equalsIgnoreCase("success")) {

            String capturedSuccess =
                    newsletterPage.getSuccessMessageText();

            Assert.assertFalse(
                    capturedSuccess.isEmpty(),
                    "Subscription failed.");

            System.out.println(
                    "Success Message : "
                            + capturedSuccess);

        }

        // ==========================================
        // Verify Duplicate Email Scenario
        // ==========================================

        else if (expectedMessage.equalsIgnoreCase(
                "Email already subscribed")) {

            String duplicateBannerText =
                    newsletterPage.getSuccessMessageText();

            System.out.println(
                    "Actual Message : "
                            + duplicateBannerText);

            System.out.println(
                    "Expected Message : "
                            + expectedMessage);

            // Detect application bug
            if (duplicateBannerText.contains("Success!")
                    || duplicateBannerText.contains(
                    "Thanks for signing up")) {

                Assert.fail(
                        "BUG: Duplicate email accepted by application.");

            } else {

                Assert.assertTrue(

                        duplicateBannerText
                                .toLowerCase()
                                .contains("already")

                                ||

                                duplicateBannerText
                                        .equalsIgnoreCase(expectedMessage),

                        "Duplicate warning not displayed.");
            }
        }

        // ==========================================
        // Verify Invalid Email Scenario
        // ==========================================

        else if (expectedMessage.equalsIgnoreCase("invalid")) {

            String errorMessage =
                    newsletterPage.getErrorMessageText();

            Assert.assertFalse(
                    errorMessage.isEmpty(),
                    "Validation message not displayed.");

            System.out.println(
                    "Validation Message : "
                            + errorMessage);
        }

        // Refresh page before next DataProvider iteration
        driver.navigate().refresh();
    }
}
