package tool.crawler.video.QQ;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import app.util.encrypt.Md5Encrypt;

public class VideoGet {
	private Map<String, String> videoSrc = new HashMap<String, String>();

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
}
