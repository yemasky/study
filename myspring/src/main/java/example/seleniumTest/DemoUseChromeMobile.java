package example.seleniumTest;

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

public class DemoUseChromeMobile {
	public static void main(String[] args) {
		// 配置服务器.\\res\\chromedriver.exe
		System.setProperty("webdriver.chrome.driver", "D:\\chrome\\chromedriver.exe");
		
		Map<String, Object> deviceMetrics = new HashMap<String, Object>();
		deviceMetrics.put("width", 360);
		deviceMetrics.put("height", 640);
		deviceMetrics.put("pixelRatio", 3.0);
		Map<String, Object> mobileEmulation = new HashMap<String, Object>();
		mobileEmulation.put("deviceMetrics", deviceMetrics);
		mobileEmulation.put("userAgent", "Mozilla/5.0 (Linux; Android 4.2.1; en-us; Nexus 5 Build/JOP40D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19");
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		WebDriver driver = new ChromeDriver(capabilities);
		
		driver.get("http://v.qq.com/iframe/player.html?vid=z0142hdzpbd&width=405&height=303.75.5&auto=0&encryptVer=6.0&platform=61001&cKey=NiikxykMGp0AYUM6nynNUjDFW6VbAU6hJroItLzpu9JD5IdZJHPGgFxmZzXR9m9K");
		// 另一种访问方法
		// driver.navigate().to("http://www.google.com");
		
		// 检查页面title
		System.out.println("Page title is: " + driver.getTitle());
		// google查询结果是通过javascript动态呈现的.
		// 设置页面等待10秒超时
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase().startsWith("");
			}
		});
		// 显示查询结果title
		System.out.println("Page title is: " + driver.getTitle());
		WebElement element = driver.findElement(By.id("tenvideo_video_player_0"));
		System.out.println("video: " + element.getAttribute("src"));
		// 关闭浏览器
		driver.quit();
		
	}
}
