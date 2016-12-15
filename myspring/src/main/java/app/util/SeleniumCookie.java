package app.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class SeleniumCookie {
	private static BufferedReader br;

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
						//expiry = null;
					}
					boolean isSecure = new Boolean(str.nextToken()).booleanValue();
					Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
					driver.manage().addCookie(ck);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
