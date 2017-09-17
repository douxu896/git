package dou;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class CSVUtils {
	public static File createCSVFile(List exportData, LinkedHashMap rowMapper,String outPutPath, String filename) {

		File csvFile = null;

		BufferedWriter csvFileOutputStream = null;

		try {

			csvFile = new File(outPutPath + filename + ".csv");
			// csvFile.getParentFile().mkdir();
			File parent = csvFile.getParentFile();
			if (parent != null && !parent.exists()) {
				parent.mkdirs();
			}
			csvFile.createNewFile();
			// GB2312 ","
			csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "GB2312"),
					1024);
			/*
			 * for (Iterator propertyIterator = rowMapper.entrySet().iterator();
			 * propertyIterator.hasNext();) { java.util.Map.Entry propertyEntry
			 * = (java.util.Map.Entry) propertyIterator.next();
			 * csvFileOutputStream.write("\""+
			 * propertyEntry.getValue().toString() + "\""); if
			 * (propertyIterator.hasNext()) { csvFileOutputStream.write(","); }
			 * }
			 */
			csvFileOutputStream.newLine();
			for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
				Object row = (Object) iterator.next();
				System.out.println(row);
				for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator.hasNext();) {
					java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
					// System.out.println( BeanUtils.getProperty(row,
					// propertyEntry.getKey().toString()));

					csvFileOutputStream.write("\"" + propertyEntry.getValue().toString() + "\"");
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
		return csvFile;

	}

	/**
	 * 导出
	 * 
	 * @param file
	 *            csv文件(路径+文件名)，csv文件不存在会自动创建
	 * @param dataList
	 *            数据
	 * @throws IOException 
	 */
	public static void exportCsv(String filename, List<String> dataList) throws IOException {
			
		    File file = new File(filename);
			FileWriter filewriter = new FileWriter(file, true);		
			if (dataList != null && !dataList.isEmpty()) {
				for (String data : dataList) {
					filewriter.write(data);
				}
			}
			filewriter.flush();
			filewriter.close();
			
	}

	/**
	 * 导入
	 * 
	 * @param file
	 *            csv文件(路径+文件)
	 * @return
	 */
	public static List<String> importCsv(File file) {
		List<String> dataList = new ArrayList<String>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				dataList.add(line);
			}
		} catch (Exception e) {
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return dataList;
	}
}