package tool.crawler.contents.Ctrip;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

import app.util.file.Write;

public class CrawlCtrip {
	private static List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
	
	public static void main(String[] args) throws InterruptedException {
		// 配置服务器.\\res\\chromedriver.exe
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");

		// 创建一个WebDriver实例
		// DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized");

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		WebDriver driver = new ChromeDriver(capabilities);

		// 访问google
		driver.get("http://hotels.ctrip.com/hotel/1698062.html#ctm_ref=ctr_hp_sb_lst");
		System.out.println("Page title is: " + driver.getTitle());
		File file = new File("configs/broswer.data");
		if (file.exists()) {
			driver.get("http://hotels.ctrip.com/hotel/1698062.html#ctm_ref=ctr_hp_sb_lst");
			Thread.sleep(1000);

		} else {
			// 另一种访问方法
			// driver.navigate().to("http://www.google.com");
			// google查询结果是通过javascript动态呈现的.
			// 设置页面等待7秒超时
			Thread.sleep(1000);
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
		Thread.sleep(1000);//等待出现		
		element = driver.findElement(By.className("c_down"));
		Actions actions = new Actions(driver);
		actions.moveToElement(element).perform();//
		getComment(driver);//第一页
		driver.findElement(By.className("fl_wrap_close")).click();
		element = driver.findElement(By.className("c_down"));//查找点击的下一页
		Thread.sleep(1000);//等待出现
		actions.moveToElement(element).perform();//
		//int i = 0;
		while (element != null) {
			//actions.moveToElement(element).click().perform();
			element.click();
			Thread.sleep(3000);//等待出现
			getComment(driver);
			try {
				element = driver.findElement(By.className("c_down"));
			} catch (Exception e) {
				System.out.println("采集完毕！");
				element = null;
			}
			if (element != null) {
				Thread.sleep(1000);//等待出现
				//actions = new Actions(driver);
				actions.moveToElement(element).perform();//
			}
			//i++;
			//if(i >= 10) break;
			
		}
		
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("1", "用户名");
		map.put("2", "用户级别");
		map.put("3", "此用户评论数");
		map.put("4", "评分类别");
		map.put("5", "评分平均值");
		map.put("6", "入住类型");
		map.put("7", "入住时间");
		map.put("8", "房型");
		map.put("9", "评论内容");
		map.put("10", "图片");
		map.put("11", "优点");
		map.put("12", "缺点");
		map.put("13", "发表时间");

		String path = "d:/莫干山西坡山乡度假-评论.csv";
		Write write = new Write();
		write.csv(exportData, map, path);
		
		System.out.println("end.");
		
		
		// 显示查询结果title
		System.out.println("Page title is: " + driver.getTitle());
		// 关闭浏览器
		// driver.quit();
	}
	
	public static void getComment(WebDriver driver) throws InterruptedException {
		Thread.sleep(2000);//等待出现		
		WebElement element = driver.findElement(By.className("comment_detail_list"));
		//String elementHtml = element.getAttribute("innerHTML");
		//System.out.println(elementHtml);
		//List<WebElement> listElement = element.findElements(By.cssSelector("div.comment_block"));
		//List<WebElement> listElement = element.findElements(By.className("comment_block"));
		List<WebElement> listElement = element.findElement(By.xpath("//*[@id=\"divCtripComment\"]/div[4]")).findElements(By.className("comment_block"));
		WebElement commentElement;
		Map<String, Object> row = new LinkedHashMap<String, Object>();
		Actions actions = new Actions(driver);
		System.out.println("listElement size:" + listElement.size());
		int size = listElement.size();
		for (int i = 0; i < size; i++) {
			//listElement.get(i).findElement(By.className("comment_main")).getAttribute("innerHTML");
			commentElement = listElement.get(i);
			String name = commentElement.findElement(By.className("name")).findElement(By.tagName("span")).getAttribute("innerHTML");
			String level = commentElement.findElement(By.cssSelector("div.user_info.J_ctrip_pop")).findElements(By.tagName("p")).get(2).getAttribute("class");
			String num = "";
			try {
				num = commentElement.findElement(By.className("num")).getAttribute("innerHTML");
			} catch (Exception e) {
				//System.out.println("num is: null");
			}	
			String score_all = commentElement.findElement(By.className("small_c")).getAttribute("data-value");
			String score = commentElement.findElement(By.className("n")).getAttribute("innerHTML");
			String type = commentElement.findElement(By.className("type")).getAttribute("innerText");
			String date = commentElement.findElement(By.className("date")).getAttribute("innerText");
			String room = commentElement.findElement(By.className("room")).getAttribute("innerText");
			String commentDetail = commentElement.findElement(By.className("J_commentDetail")).getAttribute("innerText");
			List<WebElement> listPic = commentElement.findElements(By.className("pic"));
			String pic = "";
			for (int j = 0; j < listPic.size(); j++) {
				pic += "\""+listPic.get(j).findElement(By.tagName("img")).getAttribute("data-bigimgsrc")+"\"";
			}
			String good = "";
			String bad = "";
			
			try{
				List<WebElement> comment_adv = commentElement.findElement(By.className("comment_adv")).findElements(By.tagName("p"));
				good = comment_adv.get(0).getAttribute("innerText");
				bad = comment_adv.get(1).getAttribute("innerText");
			}catch (Exception e) {
				//System.out.println("num is: null");
			}
			WebElement timeElement = commentElement.findElement(By.className("time"));
			String time = timeElement.getAttribute("innerText");
			actions.moveToElement(timeElement).perform();//
			Thread.sleep(1000);//等待出现
			//System.out.println("name:"+name+"`-`"+level+"`-`"+num+"`-`"+score_all+"`-`"+score+"`-`"+type+"`-`"+date+"`-`"+room+"`-`"+good+"`-`"+bad+"`-`"+commentDetail+"`-`"+pic+"`-`"+time);
			row = new LinkedHashMap<String, Object>();
			row.put("1", name);
			row.put("2", level);
			row.put("3", num);
			row.put("4", score_all);
			row.put("5", score);
			row.put("6", type);
			row.put("7", date);
			row.put("8", room);
			row.put("9", commentDetail);
			row.put("10", pic);
			row.put("11", good);
			row.put("12", bad);
			row.put("13", time);
			exportData.add(row);
			//System.out.println("-->" + );
			
		}
	}
}
