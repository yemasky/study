/***********************************************************************  
 *  
 *   @package：seleniumTest,@class-name：ExampleForFireFox.java  
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package example.seleniumTest;

import java.io.File;
import java.util.ArrayList;

import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * @author YEMASKY
 *
 */
public class ExampleForFireFox {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExampleForFireFox fireFox = new ExampleForFireFox();
		fireFox.test();

	}
	public void test() {
		File pathToBinary = new File("D:\\Mozilla Firefox\\firefox.exe");
        FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        FirefoxDriver driver = new FirefoxDriver(ffBinary,firefoxProfile);
        firefoxProfile.setPreference("permissions.default.stylesheet", 2);
        firefoxProfile.setPreference("permissions.default.image", 2);
        firefoxProfile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so",false);



        //http://cq.qq.com/baoliao/detail.htm?294064
        //driver.get("http://baoliao.cq.qq.com/pc/detail.html?id=294064");

        ArrayList<String> list = new ArrayList<String>();
        list.add("http://www.sina.com.cn");
        list.add("http://www.sohu.com");
        list.add("http://www.163.com");
        //list.add("http://www.qq.com");

        long start,end;

        for(int i=0;i<list.size();i++){
            start = System.currentTimeMillis();
            driver.get(list.get(i).toString());
            end = System.currentTimeMillis();
            System.out.println(list.get(i).toString() + ":" + (end - start));
        }

        driver.close();
	}
}
