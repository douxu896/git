package dou;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

/**
 * <p>
 * <code>StringSplice</code> is spilt sentences into tokens(id(author and
 * year),citation，context) , then memory in mysql
 * </p>
 * 
 * @author <a href="bjtu:15121684@bjtu.edu.cn">Emily </a>
 * @version $Revision: 2.0 $
 */
public class StringSplice {
	/**
	 * 
	 * @param str
	 *            the context of a paragraph
	 * @param papername
	 * @return 
	 * @throws SQLException
	 * @throws IOException
	 * @throws IllegalFormatException
	 * 
	 * @exception ClassNotFoundException
	 *                SqlException
	 */
	public static void stringSplice(String str, String papername)
			throws SQLException, IllegalFormatException, IOException {
		List<String> dataList = new ArrayList<String>();
		List<String> lines = Detector(str);
		for (int i = 0; i < lines.size(); i++) {
			// System.out.println(lines.get(i));
			String[][] result = Matchs(lines.get(i));
			if (result != null) {
			String[] id = new String[30];
				String citation = lines.get(i);
				//lines.set(i, citation);
				 citation = citation.replaceAll("xref#", "");
				String contextall = "";
				/**
				 * get context four sentences before citation and four sentences
				 * after citation
				 */
				for (int j = i - 4; j < i + 5; j++) {
					if (j >= 0 && j < lines.size() && j != i) {
						contextall += lines.get(j);
						// System.out.println("con: " +i+" "+j+ "
						// "+lines.get(j));
					}
				}
				/** drop xref# from context */
				String context = contextall.replaceAll("xref#", "");
				int temp = 0;
				while ((temp < result[0].length) && (result[0][temp] != null)) {
					String authorAndYear = result[0][temp];
					temp++;
					if (authorAndYear.contains(",")) {
						String id1[] = authorAndYear.split(",");
						for (String ids : id1) {
							id = getid(ids);
							for (String mm : id) {
								if (mm != null) {
									// System.out.println(mm);		
									dataList.add(papername + "#" + mm+","+citation+","+context);
								//	CSVUtils.createCSVFile(dataList, null,"/Users/douxu/Desktop/CSV", "data") ;
								//	CSVUtils.exportCsv(new File("/Users/douxu/Desktop/CSV/data.csv"), dataList);
									Storeinmysql(papername + "#" + mm, citation, context);
								}
							}
						}
					} else {
						id = getid(authorAndYear);
						for (String mm : id) {
							if (mm != null) {
								// System.out.println(mm);
								Storeinmysql(papername + "#" + mm, citation, context);
							}
						}
					}
				}
			}
		}
	}

	private static String[] getid(String id) {
		// TODO 自动生成的方法存根
		String[] mm = new String[30];
		
		if (id.contains("–")) {
			System.out.println("-------"+id);
			String id2[] = id.split("–");
			String x = id2[0];
			x = x.replaceAll("xref#", "");
			x = x.replaceFirst("^ ", "");
			x = x.replaceFirst("$ ", "");
			x = x.replaceAll("\\D*", "");
			int a = Integer.parseInt(x);
			String y = id2[1];
			y = y.replaceAll("xref#", "");
			y = y.replaceFirst("^ ", "");
			y = y.replaceFirst("$ ", "");
			y = y.replaceAll("\\D*", "");
			int b = Integer.parseInt(y);
			int ab = a;
			int i = 0;
			while ((b > a) && (ab <= b)) {
				mm[i] = String.valueOf(ab);
				ab++;
				i++;
			}
		} else {
			id = id.replaceAll("xref#", "");
			id = id.replaceFirst("^ ", "");
			id = id.replaceFirst("$ ", "");
			id = id.replaceAll("\\D*", "");
			mm[0] =id;
		}
		return mm;
	}
	private static void Storeinmysql(String id, String citation, String context) throws SQLException {
		// TODO 自动生成的方法存根
		System.out.println("------------");
		System.out.println("id:" + id);
		System.out.println("citation:" + citation);
		System.out.println("context:" + context);
		Connection con = SQLConnection.connection("root", "123456");
		String sql = "select * from info1 where id = ?;";
		if (SQLConnection.findByAuthorAndYear(sql, con, id)) {
			if(!SQLConnection.RESULT.getString(2).contains(citation)){
				String s2 = SQLConnection.RESULT.getString(2) + citation;
				String s3 = SQLConnection.RESULT.getString(3) + context;
				String updateSQL = "update info1 set citation = ?, context = ? where id = ?;";
				PreparedStatement pre = con.prepareStatement(updateSQL);
				pre.setString(1, s2);
				pre.setString(2, s3);
				pre.setString(3, id);
				pre.executeUpdate();
			}
			
		} else {
			String insertSQL = "insert into info1 values(?,?,?);";
			PreparedStatement pre = con.prepareStatement(insertSQL);
			pre.setString(1, id);
			pre.setString(2, citation);
			pre.setString(3, context);
			pre.executeUpdate();
		}
		con.close();
	}

	/**
	 * <p> matchs author and year @param String sentence a sentence @return
	 * String
	 *
	 * @throws
	 */
	public static String[][] Matchs(String sentence) {
		// System.out.println("句子:"+ sentence);
		// 正则表达式
		// String regEx = "\\(.*?xref#[0-9]{4}(.*)?\\)";
		String regEx = ".*?xref#(.*)?";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(sentence);
		if (!matcher.find()) {
			return null;
		} else {
			/** get a sentences contains citation */
			String b = matcher.group();
			// System.out.println("b:"+ b);
			// String[] m = b.split("(\\)|(\\))");
			String[] m = b.split("(\\[)|(\\])");
			String[] id = new String[30];
			int i = 0;
			String mm = sentence;
			for (String pl : m) {
				if (pl.toString().matches("xref#\\d.*")) {
					id[i] = pl;
					// System.out.println("id:"+id[i]);
					mm = mm.replace("[" + id[i] + "]", "");
					i++;
				}
			}
			/** id = "xref#1" */
			/** drop "xref#" */
			// String c =id.replaceAll("xref#","");
			// System.out.println("cit:" + c);
			String[] cia = { mm };
			// System.out.println("cia:" + cia[0]);
			// System.out.println("citations:" + cia);
			String[][] result = { id, cia };
			return result;
		}
	}

	/**
	 * <p>
	 * tokens into sentences use opennlp from stanford
	 * 
	 * @param String
	 *            str a paragraph
	 * @return String [] is every sentences
	 *
	 * @throws IllegalFormatException
	 * @throws IOException
	 */
	public static List<String> Detector(String str) throws IllegalFormatException, IOException {
		// always start with a model, a model is learned from training data
		InputStream modelIn = new FileInputStream("./nlpbin/en-sent.bin");
		SentenceModel model = new SentenceModel(modelIn);
		if (modelIn != null) {
			try {
				modelIn.close();
			} catch (IOException e) {
			}
		}
		SentenceDetectorME sdetector = new SentenceDetectorME(model);
		String sentences[] = sdetector.sentDetect(str); // 把句子之间含空格的句子分开 例如hi.
														// hello.分成两个
		// Span sentences[] = sdetector.sentPosDetect(str);
		/*
		 * for (String s : sentences) { System.out.println(s); }
		 */
		modelIn.close();
		// System.out.println("---------------1------------");
		List<String> lines = new ArrayList<String>();
		for (int i = 0; i < sentences.length; i++) {
			if ((sentences[i].endsWith("Figs.")) || (sentences[i].endsWith("e.g."))) {
				lines.add(sentences[i] + sentences[i + 1]);
				i++;
			} else {
				lines.add(sentences[i]);
			}
		}
		return lines;
	}
}
