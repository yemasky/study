/***********************************************************************  
 *  
 *   @package：example.seleniumTest,@class-name：DemoUseBacked.java  
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package example.seleniumTest;

/**
 * @author YEMASKY
 *
 */
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

public class DemoUseBacked {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		System.setProperty("webdriver.firefox.bin","D:\\Mozilla Firefox\\firefox.exe"); 
		// 创建一个WebDriver实例
		WebDriver driver = new FirefoxDriver();
		String baseUrl = "http://www.youdao.com";
		// 启动一个Selenium
		 WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver, baseUrl);
		// 访问google
		selenium.open(baseUrl);
		// 输入搜索关键字
		selenium.type("id=query", "selenium");
		// 点击 搜索
		selenium.click("id=qb");

		// 检查页面title
		System.out.println("Page title is: " + driver.getTitle());

		// google查询结果是通过javascript动态呈现的.
		// 设置页面等待10秒超时
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase().startsWith("selenium");
			}
		});

		// 显示查询结果title
		System.out.println("Page title is: " + driver.getTitle());

		// 停止Selenium
		selenium.stop();
	}
}
