package webAutomation;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ElementLocation {
/*
前程贷首页地址：http://8.129.91.152:8765/Index/index.html 用户名：13323234545 密码：lemon123456
后台地址：http://8.129.91.152:8765/Admin/Index/main.html 用户名：lemon7 密码：lemonbest
要求：定位到首页抢投标（轴定位）
* */
public static void main(String[] args) throws InterruptedException {
    System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
    ChromeDriver driver=new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    driver.manage().window().maximize();
    /*driver.get("http://8.129.91.152:8765/Index/index.html");
    driver.manage().window().maximize();
    driver.findElement(By.xpath("//a[text()='登录']")).click();
    driver.findElement(By.xpath("//input[@name='phone']")).sendKeys("13323234545");
   *//* driver.findElement(By.xpath("//input[@name='password']")).sendKeys("123456");
    driver.findElement(By.xpath("//button[text()='登录']")).click();
    String text = driver.findElement(By.xpath("//div[@class='layui-layer-content']")).getText();
    //错误提示断言
    Assert.assertEquals(text,"帐号或密码错误!");*//*
    driver.findElement(By.xpath("//input[@name='password']")).sendKeys("lemon123456");
    driver.findElement(By.xpath("//button[text()='登录']")).click();
    driver.findElement(By.xpath("//span[text()=' test1634472002245']/parent::div/parent::a/following-sibling::div[@class='b-body clearfix']//a")).click();
    driver.findElement(By.xpath("//input[@class='form-control invest-unit-investinput']")).sendKeys("9999");*/

//鼠标使用和显式等待
   /* driver.get("https://www.baidu.com/");
    //直接点击搜索设置会报NoSuchElementException，因为这个元素需要下拉设置才可见，不然就是存在但是不可见，不可点击。
    //解决办法：移动鼠标到设置，然后点击搜索设置,鼠标操作一定要加上perform()才会生效。
    Actions action=new Actions(driver);
    driver.findElement(By.xpath("//span[text()='设置']")).click();
    WebDriverWait webDriverWait = new WebDriverWait(driver,5);
    WebElement element= webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='搜索设置']")));
    action.moveToElement(element).click().perform();*/
//window切换
    /*driver.get("https://www.baidu.com/");
    driver.findElement(By.xpath("//a[text()='直播']")).click();
    System.out.println(driver.getTitle());//window任然还是百度首页的window，而不是直播，所以定位直播的元素需要切换window
    Set<String> windowHandles = driver.getWindowHandles();
    System.out.println(windowHandles);
    for(String handle:windowHandles){
        if(driver.getTitle().equals("百度直播-陪伴用户收获与成长的直播平台")){
            break;
        }else{
            driver.switchTo().window(handle);
            System.out.println(driver.getTitle());
        }
    }*/
//执行JavaScript
    //1、改变元素属性,元素为readonly,我们可以取消这个属性
   /* driver.get("https://www.12306.cn/index/");
    //得到JavaScriptExcutor对象
    JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;//这里是一个多态的应用，
    // driver继承于RemoteWebDriver，RemoteWebDriver实现了JavascriptExecutor，所以我们可以把driver转为JavascriptExecutor。
    javascriptExecutor.executeScript("document.getElementById('train_date').removeAttribute('readonly');");
    Thread.sleep(1000);
    driver.findElement(By.id("train_date")).clear();
    driver.findElement(By.id("train_date")).sendKeys("2021-05-02");*/
    //2、滚动滚动条
       /* driver.get("https://www.12306.cn/index/");
        //得到JavaScriptExcutor对象
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        //javascriptExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");//拖动滚动条到最底部
        javascriptExecutor.executeScript("document.getElementById('index_ads').scrollIntoViewIfNeeded(true);");//滚动到指定位置*/

    //3、特殊元素无法通过常规的click方法点击的情况
    driver.get("https://prepc.ketangpai.com/#/homePage");
    //点击事件会被父元素消费掉
   driver.findElement(By.xpath("//span[text()='登录']")).click();
    //JavaScript传参
    JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
    WebElement element = driver.findElement(By.xpath("//span[text()='登录']"));
    javascriptExecutor.executeScript("arguments[0].click()",element);//element作为参数传给arguments[0]
    //总结
    //NoSuchElementException发生原因有些？
    //1、元素定位表达式写错了
    //2、元素没有加载出来
    //3、元素是在iframe中
    //4、元素在新的窗口里面
}
@Test
public void futureloanAddLoan(){
    System.setProperty("webdriver.chrome.driver","src/test/resources/chromedriver.exe");
    ChromeDriver driver=new ChromeDriver();
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    driver.manage().window().maximize();
    driver.get("http://8.129.91.152:8765/Admin/Index/login.html");
    driver.findElement(By.xpath("//input[@name='admin_name']")).sendKeys("lemon7");
    driver.findElement(By.xpath("//input[@name='admin_pwd']")).sendKeys("lemonbest");
    driver.findElement(By.xpath("//input[@name='code']")).sendKeys("hapi");
    driver.findElement(By.xpath("//button[text()='登陆后台']")).click();
    Logger logger = Logger.getLogger(ElementLocation.class);
    logger.info("logger测试");
    //log,Allure报告，失败用例截图，以及他们的集成
    //失败用例截图
    TakesScreenshot screenShot=driver;
    byte[] srcFile=screenShot.getScreenshotAs(OutputType.BYTES);

}

}
