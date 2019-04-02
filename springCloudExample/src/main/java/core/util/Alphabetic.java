package core.util;

public class Alphabetic {
	private static Alphabetic _instance = null;
	public static Alphabetic instance() {
		if(_instance == null) return new Alphabetic();
		return _instance;
	}
	// 首字母转大写
	public String ucfirst(String word) {
		if (Character.isUpperCase(word.charAt(0)))
			return word;
		else
			return (new StringBuilder()).append(Character.toUpperCase(word.charAt(0))).append(word.substring(1))
					.toString();
	}
}
