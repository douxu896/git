package dou;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
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
	 * @throws SQLException
	 * @throws IOException
	 * @throws IllegalFormatException
	 * 
	 * @exception ClassNotFoundException
	 *                SqlException
	 */
	public static void stringSplice(String str) throws SQLException, IllegalFormatException, IOException {
		Connection con = SQLConnection.connection("root", "123456");
		String[] line = Detector(str);
		int total = 0;
		List<String> lines = new ArrayList<String>();
		for (int i = 0; i < line.length; i++) {
			if (line[i].endsWith("e.g")) {
				lines.add(line[i] + ". " + line[i + 1]);
				i++;
			} else {
				lines.add(line[i]);
			}
		}
		for (int i = 0; i < lines.size(); i++) {
			String[] a = Matchs(lines.get(i));
			if (a != null) {
				String authorAndYear = a[0];
				String citation = a[1];
				String context = "";
				System.out.println("------------");
				System.out.println(lines.size());
				/** get context four sentences before citation and four sentences after citation */
				for (int j = i - 4; j < i + 5; j++) {
					if (j >= 0 && j < lines.size() && j != i) {
						context += lines.get(j);
						System.out.println("con: " +i+"   "+j+"  "+lines.get(j));
					}
				}
				//System.out.println("------------");
				total++;
				System.out.println("id:" + authorAndYear);
				System.out.println("citation:" + citation);
				System.out.println("context:" + context);
				System.out.println("total:" +total);
				String sql = "select * from info where id = ?;";
				if (SQLConnection.findByAuthorAndYear(sql, con, authorAndYear)) {
					String s2 = citation + SQLConnection.RESULT.getString(2);
					String s3 = context + SQLConnection.RESULT.getString(3);
					String updateSQL = "update info set citation = ?, context = ? where id = ?;";
					PreparedStatement pre = con.prepareStatement(updateSQL);
					pre.setString(1, s2);
					pre.setString(2, s3);
					pre.setString(3, authorAndYear);
					pre.executeUpdate();
				} else {
					String insertSQL = "insert into info values(?,?,?);";
					PreparedStatement pre = con.prepareStatement(insertSQL);
					pre.setString(1, authorAndYear);
					pre.setString(2, citation);
					pre.setString(3, context);
					pre.executeUpdate();
				}
			}
		}
	}

	/**
	 * <p> matchs author and year @param String sentence a sentence @return
	 * String
	 *
	 * @throws
	 */
	public static String[] Matchs(String sentence) {
		// 正则表达式
		//String regEx = "\\(.*?xref#[0-9]{4}(.*)?\\)";
		String regEx = "\\(.*?[0-9]{4}(.*)?\\)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(sentence);
		if (!matcher.find()) {
			return null;
		} else {
			String b = matcher.group();
			String a = matcher.replaceFirst(" ");
			String[] m = b.split("(\\()|(\\))");
			String id = null;
			for (String pl : m) {
			//	if (pl.toString().matches(".*?xref#[0-9]{4}.*?")) {
					if (pl.toString().matches(".*?[0-9]{4}.*?")) {
					id = pl;
				}
			}
			/** drop "xref#" */
			/*String regEx2 = "xref#";
			Pattern pattern2 = Pattern.compile(regEx2);
			Matcher matcher2 = pattern2.matcher(id);
			String c = matcher2.replaceAll("");*/
			// System.out.println("cit:" + c);
			String cia = sentence.replace("(" + id + ")", "");
			// System.out.println("citations:" + cia);
			String[] result = { id, cia };
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
	public static String[] Detector(String str) throws IllegalFormatException, IOException {
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
		return sentences;
	}
}
