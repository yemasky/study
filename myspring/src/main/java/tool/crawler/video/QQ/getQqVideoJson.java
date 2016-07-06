package tool.crawler.video.QQ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class getQqVideoJson {
	private static Map<String, String> allVideoUrl = new LinkedHashMap<String, String>();

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
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> video = (List<Map<String, Object>>) videoSrcMap.get("videolst");
					for (int j = 0; j < video.size(); j++) {
						String src = "http://v.qq.com/iframe/player.html?vid=" + (String) video.get(j).get("vid")
								+ "&width=500&height=375&auto=0";
						String title = (String) video.get(j).get("title_s");
						allVideoUrl.put(title, src);
						System.out.println("list:" + title + ", src: " + src);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// break;
		}
		if (allVideoUrl != null) {
			videoGet.setVideoIframeUrl(allVideoUrl);
			videoGet.getVideoUrl();
		}
		// System.out.println(html);
	}

	public static void renameVideo() {
		File file = new File("configs/tool.crawler.QQ.txt");
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			String videoName = "", fontName = "";
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				line++;
				videoName = "";
				if ((line % 2) == 1) {
					String[] src = tempString.split("\\?");
					videoName = src[0].substring(src[0].lastIndexOf("/") + 1);
					System.out.println(videoName + " " + fontName);
					File oldfile = new File("configs/font/" + videoName);
					File newfile = new File("configs/font/" + fontName + ".mp4");
					if (!oldfile.exists()) {
						System.out.println("oldfile.exists:" + oldfile);
					}
					if (newfile.exists()) {// 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
						System.out.println("newfile.exists:" + newfile);
					} else {
						oldfile.renameTo(newfile);
					}
				} else {
					fontName = tempString;
				}
				// System.out.println(videoName + " " + line + ": " +
				// tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static void main(String[] args) {
		renameVideo();
	}
	// http://v.qq.com/iframe/player.html?vid=n0142tcuxy8&width=500&height=375&auto=0
}
