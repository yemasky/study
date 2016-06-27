/***********************************************************************  
 *  
 *   @package：example.seleniumTest,@class-name：DemoUseChromeWeixin.java  
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package example.seleniumTest;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author YEMASKY
 *
 */
public class DemoUseChromeWeixin {
	public static void main(String[] args) {
		// 配置服务器.\\res\\chromedriver.exe
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");

		// 创建一个WebDriver实例
		// DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		//options.addArguments("start-maximized");

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		WebDriver driver = new ChromeDriver(capabilities);
		// 访问google
		driver.get("https://wx2.qq.com/");
		// 另一种访问方法
		// driver.navigate().to("http://www.google.com");

		System.out.println("Page title is: " + driver.getTitle());
		// google查询结果是通过javascript动态呈现的.
		// 设置页面等待10秒超时
		try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		(new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase().startsWith("");
			}
		});
		// 找到文本框
		WebElement element = driver.findElement(By.className("tab"));
		System.out.println("element is: " + element.getAttribute("ng-class"));
		// 点击
		element.click();
		Actions actions = new Actions(driver);
		List<WebElement> yy = driver.findElements(By.cssSelector("div.read_item.slide-left.ng-scope"));
		for(int i = 0; i < yy.size(); i++) {
			System.out.println(yy.get(i).findElement(By.tagName("h3")).getText());
			yy.get(i).click();
			//if(yy.size() > (i + 1)) actions.moveToElement(yy.get(i+1)).perform();;
			if((i + 1) == yy.size()) {
				//yy = null;
				yy = driver.findElements(By.cssSelector("div.read_item.slide-left.ng-scope"));
				i = 13;
			}
			
		}
		
		// 显示查询结果title
		System.out.println("Page title is: " + driver.getTitle());
		// 关闭浏览器
		// driver.quit();
	}
}
