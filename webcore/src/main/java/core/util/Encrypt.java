package core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * MD5等工具
 * 
 * @author liyong
 * @author CooC
 *
 */
public class Encrypt {

	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	private static char hexDigitsLower[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private static SecureRandom secureRandom = new SecureRandom();

	/**
	 * 产生一个不带'-'的UUID字符串
	 * 
	 * @return
	 */
	public static String getRandomUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 对字符串进行MD5摘要，字符编码为UTF-8，md5后的字符串是大写。
	 * 
	 * @param text
	 *            要摘要的字符串
	 * @return
	 */
	public static String md5Upper(String text) {
		return md5(text, false);
	}

	/**
	 * 对字符串进行MD5摘要，字符编码为UTF-8，md5后的字符串是小写。
	 * 
	 * @param text
	 *            要摘要的字符串
	 * @return
	 */
	public static String md5Lower(String text) {
		return md5(text, true);
	}

	/**
	 * 对byte[] 进行md5摘要，md5后的字符串是大写。
	 * 
	 * @param data
	 * @return
	 */
	public static String md5Upper(byte[] data) {
		return md5(data, false);
	}

	/**
	 * 对byte[] 进行md5摘要，md5后的字符串是小写。
	 * 
	 * @param data
	 * @return
	 */
	public static String md5Lower(byte[] data) {
		return md5(data, true);
	}

	/**
	 * 生成随机的numBytes位字符串。
	 * 
	 * @param numBytes
	 *            字符串长度。
	 */
	public static String generateRandomString(int numBytes) {
		if (numBytes < 1) {
			throw new RuntimeException("字节数参数必需是正整数");
		}

		byte[] bytes = new byte[numBytes];
		secureRandom.nextBytes(bytes);
		return encodeHexString(bytes);
	}

	/**
	 * 
	 * @param salt
	 *            盐值
	 * @param plainText
	 *            明文
	 * @return md5(md5(plainText)+salt)
	 */
	public static String entryptText(String plainText, String salt) {
		String ret = md5Lower(plainText);
		if (salt != null) {
			ret += salt.trim();
		}
		return md5Lower(ret);
	}

	/**
	 * 
	 * @param text
	 * @param toLowerCase
	 *            是否用小写字母。
	 * @return
	 */
	private static String md5(String text, final boolean toLowerCase) {
		if (text == null || text.trim().length() < 1) {
			return "";
		}
		try {
			return md5(text.getBytes("UTF-8"), toLowerCase);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static String md5(byte[] source, final boolean toLowerCase) {
		if (source == null || source.length == 0) {
			return null;
		}
		return md5(source, toLowerCase ? hexDigitsLower : hexDigits);
	}

	private static String md5(byte[] source, final char[] hexDigits) {
		String s = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			s = new String(str); // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	private static String encodeHexString(final byte[] data) {
		return new String(encodeHex(data, true));
	}

	private static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
		return encodeHex(data, toLowerCase ? hexDigitsLower : hexDigits);
	}

	private static char[] encodeHex(final byte[] data, final char[] toDigits) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * 进行MD5加密
	 * 
	 * @param String
	 *            原始的SPKEY
	 * @return byte[] 指定加密方式为md5后的byte[]
	 */
	private byte[] md5(String strSrc) {
		byte[] returnByte = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			returnByte = md5.digest(strSrc.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnByte;
	}

	/**
	 * 得到3-DES的密钥匙 根据根据需要，如密钥匙为24个字节，md5加密出来的是16个字节，因此后面补8个字节的0
	 * 
	 * @param String
	 *            原始的SPKEY
	 * @return byte[] 指定加密方式为md5后的byte[]
	 */
	private byte[] getEnKey(String spKey) {
		byte[] desKey = null;
		try {
			byte[] desKey1 = md5(spKey);
			desKey = new byte[24];
			int i = 0;
			while (i < desKey1.length && i < 24) {
				desKey[i] = desKey1[i];
				i++;
			}
			if (i < 24) {
				desKey[i] = 0;
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return desKey;
	}

	/**
	 * 3-DES加密
	 * 
	 * @param byte[]
	 *            src 要进行3-DES加密的byte[]
	 * @param byte[]
	 *            enKey 3-DES加密密钥
	 * @return byte[] 3-DES加密后的byte[]
	 */
	public byte[] desEncrypt(byte[] src, byte[] enKey) {
		byte[] encryptedData = null;
		try {
			DESedeKeySpec dks = new DESedeKeySpec(enKey);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey key = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DESede");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encryptedData = cipher.doFinal(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedData;
	}

	/**
	 *
	 * 进行3-DES解密（密钥匙等同于加密的密钥匙）。
	 * 
	 * @param byte[]
	 *            src 要进行3-DES解密byte[]
	 * @param String
	 *            spkey分配的SPKEY
	 * @return String 3-DES解密后的String
	 */
	public String desDecrypt(byte[] debase64, String spKey) {		
		String strDe = null;
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("DESede");
			byte[] key = getEnKey(spKey);
			DESedeKeySpec dks = new DESedeKeySpec(key);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			SecretKey sKey = keyFactory.generateSecret(dks);
			cipher.init(Cipher.DECRYPT_MODE, sKey);
			byte ciphertext[] = cipher.doFinal(debase64);
			strDe = new String(ciphertext, "UTF-16LE");
		} catch (Exception ex) {
			strDe = "";
			ex.printStackTrace();
		}
		return strDe;
	}
}
