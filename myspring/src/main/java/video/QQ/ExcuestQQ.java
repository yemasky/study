/***********************************************************************  
 *  
 *   @package：video.QQ,@class-name：ExcuestQQ.java
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2016年6月27日
 *   @creator         YEMASKY
 *   @create-time     2016年6月27日 上午10:42:33
 *   @revision        Id: 1.0    
 ***********************************************************************/
package video.QQ;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

/**
 * @author admin
 *
 */
public class ExcuestQQ {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VideoGet videoGet = new VideoGet();
		try {
			videoGet.getVideoUrl(null);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
