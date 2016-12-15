package tool.crawler.contents.Ctrip;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CrawlCtrip {
	public static void main(String[] args) throws InterruptedException {
		// 配置服务器.\\res\\chromedriver.exe
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");

		// 创建一个WebDriver实例
		// DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		// options.addArguments("start-maximized");

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		WebDriver driver = new ChromeDriver(capabilities);

		// 访问google
		driver.get("http://hotels.ctrip.com/hotel/1698062.html#ctm_ref=ctr_hp_sb_lst");
		System.out.println("Page title is: " + driver.getTitle());
		File file = new File("configs/broswer.data");
		if (file.exists()) {
			//SeleniumCookie.readCookie(driver);
			driver.get("http://hotels.ctrip.com/hotel/1698062.html#ctm_ref=ctr_hp_sb_lst");
			Thread.sleep(5000);

		} else {
			// 另一种访问方法
			// driver.navigate().to("http://www.google.com");
			// google查询结果是通过javascript动态呈现的.
			// 设置页面等待7秒超时
			Thread.sleep(7000);
			//SeleniumCookie.writeCookie(driver);
		}

		(new WebDriverWait(driver, 1)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.getTitle().toLowerCase().startsWith("");
			}
		});
		// 找到文本框
		WebElement element = driver.findElement(By.id("id_comment_view"));
		Thread.sleep(1000);//等待出现
		// 点击
		element.click();
		Thread.sleep(2000);//等待出现
		element = driver.findElement(By.className("comment_detail_list"));
		
		List<WebElement> listElement = element.findElements(By.cssSelector("div.comment_block"));
		for (int i = 0; i < listElement.size(); i++) {
			System.out.println("-->" + listElement.get(i).findElement(By.className("comment_main")).getAttribute("innerHTML"));
		}
		System.out.println("end iframe");

		Actions actions = new Actions(driver);
		List<WebElement> fontElement = driver.findElements(By.cssSelector("div.read_item.slide-left.ng-scope"));
		Map<String, Integer> haveText = new HashMap<String, Integer>();
		String fontTextPrevious = "";
		for (int i = 0; i < fontElement.size(); i++) {
			String fontText = fontElement.get(i).findElement(By.tagName("h3")).getText();
			if (haveText.get(fontText) != null) {
				int fontTextRepeatCount = haveText.get(fontText);
				if (fontTextRepeatCount >= 3)
					break;
				haveText.put(fontText, fontTextRepeatCount + 1);
			} else {
				haveText.put(fontText, 1);
			}
			System.out.println(fontText);
			if (!fontTextPrevious.equals("") && !fontTextPrevious.equals(fontText)) {
				haveText.remove(fontTextPrevious);
			}
			fontTextPrevious = fontText;
			fontElement.get(i).click();
			// Thread.sleep(2000);
			// WebElement videoElement =
			// driver.findElement(By.cssSelector("iframe.video_iframe"));
			WebElement videoElement = driver.findElement(By.cssSelector("iframe#reader.iframe"));
			// WebElement videoIframeElement =
			// videoElement.findElement(By.className("video_iframe"));
			System.out.println(videoElement.getAttribute("src"));
			// if (fontElement.size() > (i + 1))
			actions.moveToElement(driver.findElement(By.id("voiceMsgPlayer"))).perform();//
			// voiceMsgPlayer

			if ((i + 1) == fontElement.size()) {
				// yy = null;
				fontElement = driver.findElements(By.cssSelector("div.read_item.slide-left.ng-scope"));
				i = fontElement.size() - 1;
			}

		}
		
		// 显示查询结果title
		System.out.println("Page title is: " + driver.getTitle());
		// 关闭浏览器
		// driver.quit();
	}
}
