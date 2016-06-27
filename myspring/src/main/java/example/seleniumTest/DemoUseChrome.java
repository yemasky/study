/***********************************************************************  
 *  
 *   @package：seleniumTest,@class-name：DemoUseChrome.java  
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
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DemoUseChrome {
	public static void main(String[] args) {
		// 配置服务器.\\res\\chromedriver.exe
		System.setProperty("webdriver.chrome.driver", "D:\\chrome\\chromedriver.exe");
		
		// 创建一个WebDriver实例
		//DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");
		Map<String, Object> mobileEmulation = new HashMap<String, Object>();
		mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.36");
		mobileEmulation.put("deviceName", "Nexus 6P");
		mobileEmulation.put("deviceMetrics", "435x773");		
		options.setExperimentalOption("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		WebDriver driver = new ChromeDriver(capabilities);
		// 访问google
		driver.get("http://www.baidu.com");
		// 另一种访问方法
		// driver.navigate().to("http://www.google.com");
		// 找到文本框
		WebElement element = driver.findElement(By.name("wd"));
		// 输入搜索关键字
		element.sendKeys("Selenium");
		// 提交表单 WebDriver会自动从表单中查找提交按钮并提交
		element.submit();
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
		// 关闭浏览器
		driver.quit();
	}
}