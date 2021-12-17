package webAutomation.base;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseTest {
    public static ChromeDriver driver;
    public  static ChromeDriver
    openBrowser(){
        System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
        driver=new ChromeDriver();
        return driver;
    }
    public static byte[] screenshotas(){
        TakesScreenshot screenShot=driver;
        byte[] srcFile=screenShot.getScreenshotAs(OutputType.BYTES);
        return srcFile;
    }
}
