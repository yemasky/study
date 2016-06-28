package video.QQ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import encrypt.Md5Encrypt;

public class VideoGet {
	private Map<String, String> videoSrc = new HashMap<String, String>();
	private List<String> nextUrlSrc = new ArrayList<String>();

	public void getVideoUrl(String url) throws ClientProtocolException, IOException {
		if (url == null || url.equals("") || url.isEmpty()) {
			url = "http://mp.weixin.qq.com/s?__biz=MjM5NjY4MDM1MA==&mid=204264977&idx=1&sn=82550433c92edaa25169f15069f2f923&scene=20#wechat_redirect";
		}
		String md5Key = Md5Encrypt.MD5Encode(url, null);//$1$6sPFe.8t$/sfm712BJGg9Nq1pma8O4.
		System.out.println(md5Key);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String src = "", nextUrl = "";
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch");
			httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
			httpGet.addHeader("User-Agent",
					"Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
			httpGet.addHeader("Connection", "keep-alive");
			httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			CloseableHttpResponse response = httpclient.execute(httpGet);

			try {
				System.out.println(response.getStatusLine());
				HttpEntity entity = response.getEntity();
				String html = EntityUtils.toString(entity);
				
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
				linksElements = doc.getElementById("js_content").getElementsByTag("a");
				for (Element element : linksElements) {
					if (element.html().contains("点击下一个字")) {
						nextUrl = element.attr("href");
						nextUrlSrc.add(nextUrl);
						System.out.println("-->" + nextUrl);
					}

				}
				// System.out.println(EntityUtils.toString(entity));

				// do something useful with the response body
				// and ensure it is fully consumed
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}

		} finally {
			httpclient.close();
		}
		if(!nextUrl.equals("")) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.getVideoUrl(nextUrl);
		}
	}
}
