package core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadProperties {
	protected Logger logger = LoggerFactory.getLogger(ReadProperties.class);
	private static HashMap<String, HashMap<String, String>>properties = new HashMap<String, HashMap<String, String>>();
	private String readkey = "";
	public ReadProperties (String propertiesFile) {
		if(properties == null || properties.get(propertiesFile) == null) {
			try {
				this.readToProperties(propertiesFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.readkey = propertiesFile;
	}
	
	public static ReadProperties instance() {
		return new ReadProperties("config.properties");
	}

	public static ReadProperties instance(String propertiesFile) {
		return new ReadProperties(propertiesFile);
	}
	
	public String read(String key) {
		if(properties.containsKey(this.readkey) && properties.get(this.readkey).containsKey(key)) {
			return properties.get(this.readkey).get(key);
		}
		return "";
	}
	
	public void readToProperties(String propertiesFile) throws IOException {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config/"+propertiesFile);
		Properties proper = new Properties();
		proper.load(inputStream);
		inputStream.close();
		Set<Object> set = proper.keySet();
		Iterator<Object> it = set.iterator();
		String key = "";
		String value = "";
		HashMap<String, String> thisProperties = new HashMap<String, String>();
		while (it.hasNext()) {
			key = (String) it.next();
			value = proper.getProperty(key);
			logger.info("loading "+propertiesFile+": key->" + key + ", value->" + value);
			thisProperties.put(key, value);
		}
		properties.put(propertiesFile, thisProperties);
	}
	

}
