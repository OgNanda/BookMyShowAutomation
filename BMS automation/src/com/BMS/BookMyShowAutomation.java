package com.BMS;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookMyShowAutomation {

    public static void main(String[] args) throws AWTException, InterruptedException {

        String BMS_Url = "https://in.bookmyshow.com/explore/home/";
        String Mail_Url = "https://yopmail.com";
        String MailId = "nksp@yopmail.com";
        String Expected_Result = "Hi, Guest";

        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Home\\Downloads\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Navigate to BookMyShow
        driver.get(BMS_Url);
        driver.manage().deleteAllCookies();
        driver.switchTo().activeElement().sendKeys("Coimbatore");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@class='bwc__sc-1iyhybo-9 fMpEag']"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[.='Sign in']"))).click();

        // Use JavaScriptExecutor to click the "Continue with Email" button
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='bwc__sc-dh558f-14 fPrBPf' and text()='Continue with Email']"))));
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("emailId"))).sendKeys(MailId);

        // Use JavaScriptExecutor to click the "Submit" button
        js.executeScript("arguments[0].click();", wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='bwc__sc-dh558f-37 hmbiuL']"))));

        // Open a new tab and navigate to Yopmail
        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_CONTROL);
        r.keyPress(KeyEvent.VK_T);
        r.keyRelease(KeyEvent.VK_T);
        r.keyRelease(KeyEvent.VK_CONTROL);

        Thread.sleep(500);
        Set<String> Windows = driver.getWindowHandles();
        List<String> Tabs = new ArrayList<>(Windows);

        driver.switchTo().window(Tabs.get(1));
        driver.get(Mail_Url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login"))).sendKeys(MailId);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//i[@class='material-icons-outlined f36']"))).click();

        // Switch to the email iframe and get the OTP
        driver.switchTo().frame("ifmail");
        String OTP = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//td)[11]"))).getText();

        // Switch back to BookMyShow tab
        driver.switchTo().window(Tabs.get(0));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='sc-gvZAcH gkRWDL']"))).sendKeys(OTP);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[.='Continue']"))).click();

        String Actual_Result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='sc-gEkIjz iBRucH']"))).getText();

        if (Actual_Result.equals(Expected_Result)) {
            System.out.println("User is successfully signed in, and User Name: " + Actual_Result + " is displayed.");
        } else {
            System.out.println("Sign-in validation failed");
        }

        driver.quit();
    }
}
