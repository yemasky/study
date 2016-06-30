package tool.crawler.video.QQ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import app.util.encrypt.Md5Encrypt;
import app.util.file.Write;

public class VideoGet {
	private Map<String, String> videoSrc = new HashMap<String, String>();
	private static BufferedReader br;

	public void getBeginVideoUrl(String url) throws InterruptedException {
		WebDriver driver = null;
		driver = chromeDriver();
		driver.get("https://wx2.qq.com/");
		System.out.println("Page title is: " + driver.getTitle());
		File file = new File("configs/broswer.data");
		if (file.exists()) {
			readCookie(driver);
			driver.get("https://wx2.qq.com/");
			Thread.sleep(5000);

		} else {
			// 另一种访问方法
			// driver.navigate().to("http://www.google.com");
			// google查询结果是通过javascript动态呈现的.
			// 设置页面等待7秒超时
			Thread.sleep(7000);
			writeCookie(driver);
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
		List<WebElement> iframeElement = driver.findElements(By.tagName("iframe"));
		for (int i = 0; i < iframeElement.size(); i++) {
			System.out.println(iframeElement.get(0).getAttribute("src"));
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
		// String setscroll = "document.documentElement.scrollTop=" + 800;
		// JavascriptExecutor jse = (JavascriptExecutor) driver;
		// jse.executeScript(setscroll);
		// ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,
		// document.body.scrollHeight)");
		// ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,
		// 700)");

		// 显示查询结果title
		System.out.println("Page title is: " + driver.getTitle());
		// 关闭浏览器
		// driver.quit();
	}

	public void getVideoIframeUrl(String url) throws ClientProtocolException, IOException {
		String md5Key = Md5Encrypt.MD5Encode(url, null);
		String src = "";
		String html = getMobileHtml(url);
		// System.out.println(html);
		Document doc = Jsoup.parse(html);
		Elements linksElements = doc.getElementsByTag("iframe");
		for (Element element : linksElements) {
			src = element.attr("data-src");
			if (src.contains("cKey")) {
				md5Key = Md5Encrypt.MD5Encode(src, null);
				videoSrc.put(md5Key, src);
				System.out.println("-->" + src);
			}
			src = element.attr("src");
			if (src.contains("cKey")) {
				md5Key = Md5Encrypt.MD5Encode(src, null);
				videoSrc.put(md5Key, src);
				System.out.println("-->" + src);
			}
		}
		// System.out.println(EntityUtils.toString(entity));
		// do something useful with the response body
		// and ensure it is fully consumed

	}

	public void getVideoUrl(String url) {
		WebDriver driver = null;
		driver = chromeDriver();
		// 找到文本框
		WebElement element = driver.findElement(By.className("tvp_video")).findElement(By.tagName("video"));
		System.out.println("element is: " + element.getAttribute("src"));
		Write write = new Write();
		try {
			write.writeFile("", element.getAttribute("src"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getMobileHtml(String url) throws ClientProtocolException, IOException, ParseException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch");
		httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
		httpGet.addHeader("User-Agent",
				"Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
		httpGet.addHeader("Connection", "keep-alive");
		httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		System.out.println(response.getStatusLine());
		HttpEntity entity = response.getEntity();
		String html = null;
		try {
			html = EntityUtils.toString(entity);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			response.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			httpclient.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EntityUtils.consume(entity);
		return html;
	}

	public WebDriver chromeDriver() {
		// 配置服务器.\\res\\chromedriver.exe
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");

		// 创建一个WebDriver实例
		// DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		// options.addArguments("start-maximized");

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);

		WebDriver driver = new ChromeDriver(capabilities);
		// 读取cookie

		// 访问google
		
		return driver;
	}

	public static void writeCookie(WebDriver driver) {
		File file = new File("configs/broswer.data");
		try {
			// delete file if exists
			file.delete();
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Cookie ck : driver.manage().getCookies()) {
				bw.write(ck.getName() + ";" + ck.getValue() + ";" + ck.getDomain() + ";" + ck.getPath() + ";"
						+ ck.getExpiry() + ";" + ck.isSecure());
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("cookie write to file");
		}
	}

	public static void readCookie(WebDriver driver) {
		// Cookies.addCookies();
		try {
			File file = new File("configs/broswer.data");
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				StringTokenizer str = new StringTokenizer(line, ";");
				while (str.hasMoreTokens()) {
					String name = str.nextToken();
					String value = str.nextToken();
					String domain = str.nextToken();
					String path = str.nextToken();
					Date expiry = null;
					String dt = str.nextToken();
					if (!dt.equals(null) && !dt.equals("null")) {
						SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
						expiry = format.parse(dt);
						System.out.println(expiry);
						// expiry = null;
					}
					boolean isSecure = new Boolean(str.nextToken()).booleanValue();
					Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
					driver.manage().addCookie(ck);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// driver.get("http://www.zhihu.com/");
	}
}
