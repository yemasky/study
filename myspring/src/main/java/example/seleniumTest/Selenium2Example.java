/***********************************************************************  
 *  
 *   @package：seleniumTest,@class-name：Selenium2Example.java  
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package example.seleniumTest;

import java.io.File;

/**
 * @author YEMASKY
 *
 */
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
public class Selenium2Example  {
     public static void main(String[] args) {
    	 
    	 System.setProperty("webdriver.firefox.bin","D:\\Mozilla Firefox\\firefox.exe"); 
    	 File pathToFirefoxBinary = new File("D:\\Mozilla Firefox\\firefox.exe");
    	 FirefoxBinary firefoxbin = new FirefoxBinary(pathToFirefoxBinary);
    	 WebDriver driver = new FirefoxDriver(firefoxbin,null);//这里使用这个构造方法。
         //创建一个WebDriver实例
         //WebDriver driver = new FirefoxDriver();
         // 访问google
         //driver.get("http://www.google.com");
         // 另一种访问方法
          driver.navigate().to("http:////www.baidu.com");
         // 找到文本框
         WebElement element = driver.findElement(By.name("q"));
         // 输入搜索关键字
         element.sendKeys("Selenium");
         //提交表单 WebDriver会自动从表单中查找提交按钮并提交
         element.submit();
         // 检查页面title
         System.out.println("Page title is: " + driver.getTitle());
         // google查询结果是通过javascript动态呈现的.
         // 设置页面等待10秒超时
         (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
              public Boolean apply(WebDriver d) {
                  return d.getTitle().toLowerCase().startsWith("Selenium");
              }
         });
         // 显示查询结果title
         System.out.println("Page title is: " + driver.getTitle());
         //关闭浏览器
         driver.quit();
     }
}