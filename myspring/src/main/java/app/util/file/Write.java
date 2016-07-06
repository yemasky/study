package app.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Write {
	private String encode = "UTF-8";

	public void writeFile(String fileNamePath, String data) throws IOException {
		File file = new File(fileNamePath);
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fileWritter = new FileWriter(file, true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		bufferWritter.write(data);
		bufferWritter.close();
	}

	public void csv(List<Map<String, Object>> exportData, LinkedHashMap<String, Object> mapHead,
			String fileNamePath) {
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		try {
			csvFile = new File(fileNamePath);
			// csvFile.getParentFile().mkdir();
			File parent = csvFile.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			if (!csvFile.exists())
				csvFile.createNewFile();

			// GB2312使正确读取分隔符","
			csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), encode),
					1024);
			// 写入文件头部
			for (Iterator<Entry<String, Object>> propertyIterator = mapHead.entrySet().iterator(); propertyIterator
					.hasNext();) {
				Entry<String, Object> propertyEntry = propertyIterator.next();
				csvFileOutputStream.write("\"" + (String) propertyEntry.getValue() != null
						? (String) propertyEntry.getValue() : "" + "\"");
				if (propertyIterator.hasNext()) {
					csvFileOutputStream.write(",");
				}
			}
			csvFileOutputStream.newLine();

			// 写入文件内容
			for (Iterator<Map<String, Object>> iterator = exportData.iterator(); iterator.hasNext();) {
				Map<String, Object> row = iterator.next();
				for (Iterator<Entry<String, Object>> propertyIterator = mapHead.entrySet().iterator(); propertyIterator
						.hasNext();) {
					Entry<String, Object> propertyEntry = propertyIterator.next();
					System.out.println(row.get(propertyEntry.getKey()));
					//csvFileOutputStream.write((String) BeanUtils.getProperty(row, (String) propertyEntry.getKey()));
					csvFileOutputStream.write((String) row.get(propertyEntry.getKey()));
					if (propertyIterator.hasNext()) {
						csvFileOutputStream.write(",");
					}
				}
				if (iterator.hasNext()) {
					csvFileOutputStream.newLine();
				}
			}
			csvFileOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				csvFileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Write setEncode(String encode) {
		this.encode = encode;
		return this;
	}

	public static void main(String[] args) {
		List<Map<String, Object>> exportData = new ArrayList<Map<String, Object>>();
		Map<String, Object> row1 = new LinkedHashMap<String, Object>();
		row1.put("1", "11");
		row1.put("2", "12");
		row1.put("3", "13");
		row1.put("4", "14");
		exportData.add(row1);
		row1 = new LinkedHashMap<String, Object>();
		row1.put("1", "21");
		row1.put("2", "22");
		row1.put("3", "23");
		row1.put("4", "24");
		exportData.add(row1);
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("1", "第一列");
		map.put("2", "第二列");
		map.put("3", "第三列");
		map.put("4", "第四列");

		String path = "d:/11.csv";
		Write write = new Write();
		write.csv(exportData, map, path);

	}

}
