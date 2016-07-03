package tool.crawler.video.QQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class getQqVideoJson {
	private static Map<String, String> allVideoUrl = new HashMap<String, String>();
	public static void getVideoJson() {
		VideoGet videoGet = new VideoGet();
		String html = null;
		String matchResult = null;
		Gson gson = new Gson();
		Map<String, Object> videoSrcMap = new HashMap<String, Object>();
		for (int i = 1; i <= 22; i++) {
			String url = "http://c.v.qq.com/vchannelinfo?otype=json&uin=7438b7ceaa6748f2e370ba49c9e883e0&qm=1&pagenum="
					+ i
					+ "&num=30&sorttype=1&orderflag=0&callback=jQuery191025273524920583446_1467507332096&low_login=1&_=1467507332134";
			try {
				html = videoGet.getMobileHtml(url, 3);
				Pattern pattern = Pattern.compile("\\((.+?)\\)");
				Matcher matcher = pattern.matcher(html);
				if (matcher.find()) {
					matchResult = matcher.group(1);
					System.out.println(matchResult);
				} else {
					System.out.println("NO MATCH");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!matchResult.equals(null)) {
				try {
					videoSrcMap = gson.fromJson(matchResult, new TypeToken<Map<String, Object>>() {
					}.getType());
					List<Map<String,Object>> video = (List<Map<String, Object>>) videoSrcMap.get("videolst");
					System.out.println("list:" + video.get(0).get("url"));
					for(int j = 0; j < video.size(); j++) {
						allVideoUrl.put((String)video.get(j).get("title"), (String)video.get(j).get("url"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}
		System.out.println(html);
	}

	public static void main(String[] args) {
		getVideoJson();
	}

}
