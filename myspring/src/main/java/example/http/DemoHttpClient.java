package example.http;

import java.io.IOException;

import org.apache.http.HttpEntity;
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

public class DemoHttpClient {

	public void get() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet("http://mp.weixin.qq.com/s?__biz=MjM5NjY4MDM1MA==&mid=204264977&idx=1&sn=82550433c92edaa25169f15069f2f923&scene=20#wechat_redirect");
            httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                System.out.println(EntityUtils.toString(entity1));

                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            } finally {
                response1.close();
            }

        } finally {
            httpclient.close();
        }
	}
	
	public void getvideo() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet("http://v.qq.com/iframe/player.html?vid=q0145jxw74g&width=500&height=375&auto=0");
            httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
            httpGet.addHeader("Connection", "keep-alive");
            httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                String html = EntityUtils.toString(entity1);
                System.out.println(html);
                Document doc = Jsoup.parse(html);  
                //System.out.println(doc.html());
                Elements linksElements = doc.select("html");  
                for (Element ele1 : linksElements) {
                    System.out.println("-->" + ele1.html());
                   }
                Element linksElementss = doc.getElementById("mod_download");
                    System.out.println("--+>" + linksElementss.html());

                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            } finally {
                response1.close();
            }

        } finally {
            httpclient.close();
        }
	}
	
}
