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
		
		String[] line = Detector(str);
		//static int total = 0;
		List<String> lines = new ArrayList<String>();
		for (int i = 0; i < line.length; i++) {
			if ((line[i].endsWith("e.g") )||(line[i].endsWith("e.g.") ) ) {
				lines.add(line[i] + line[i + 1]);
				i++;
			} else {
				lines.add(line[i]);
			}
		}
		for (int i = 0; i < lines.size(); i++) {
			String[][] result = Matchs(lines.get(i));
			if (result != null) {
				int temp = 0 ;
				while(result[0][temp] != null){
					String authorAndYear = result[0][temp];
	
					temp++;
					String contextall = "";
					/** get context four sentences before citation and four sentences after citation */
					for (int j = i - 4; j < i + 5; j++) {
						if (j >= 0 && j < lines.size() && j != i) {
							contextall += lines.get(j);
					//		System.out.println("con: " +i+"   "+j+"  "+lines.get(j));
						}
					}
					/** drop xref# from context  */
					String context = contextall.replaceAll("xref#", "");
					String citations = result[1][0];
					String citation = citations.replaceAll("xref#", "");
					if(authorAndYear.contains(";")){
						String id1[]  = authorAndYear.split(";");
						for(String id:id1){		
							id = id.replaceAll("xref#", "");
							id = id.replaceFirst("^ ", "");
							id = id.replaceFirst("$ ", "");
						//	System.out.println(lines.size());	
							Storeinmysql(id,citation,context);
						}				
					}
					else{
						String id = authorAndYear.replaceAll("xref#", "");
						id = id.replaceFirst("^ ", "");
						id = id.replaceFirst("$ ", "");			
						Storeinmysql(id,citation,context);	
					}
				}				
			}
		}
	}

	private static void Storeinmysql(String id, String citation, String context) throws SQLException {
		// TODO 自动生成的方法存根
		System.out.println("------------");
		System.out.println("id:" + id);
		System.out.println("citation:" + citation);
		System.out.println("context:" + context);
		Connection con = SQLConnection.connection("root", "123456");
		String sql = "select * from info where id = ?;";
		if (SQLConnection.findByAuthorAndYear(sql, con, id)) {
			String s2 = citation + SQLConnection.RESULT.getString(2);
			String s3 = context + SQLConnection.RESULT.getString(3);
			String updateSQL = "update info set citation = ?, context = ? where id = ?;";
			PreparedStatement pre = con.prepareStatement(updateSQL);
			pre.setString(1, s2);
			pre.setString(2, s3);
			pre.setString(3, id);
			pre.executeUpdate();
		} else {
			String insertSQL = "insert into info values(?,?,?);";
			PreparedStatement pre = con.prepareStatement(insertSQL);
			pre.setString(1, id);
			pre.setString(2, citation);
			pre.setString(3, context);
			pre.executeUpdate();
		}
	}

	/**
	 * <p> matchs author and year @param String sentence a sentence @return
	 * String
	 *
	 * @throws
	 */
	public static String[][] Matchs(String sentence) {
		// 正则表达式
		String regEx = "\\(.*?xref#[0-9]{4}(.*)?\\)";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(sentence);
		if (!matcher.find()) {
			return null;
		} else {
			/** get a sentences contains citation */
			String b = matcher.group();  
			String[] m = b.split("(\\()|(\\))");
			String[] id =new String[10] ;
			int i = 0 ;
			String mm  = null ;
			for (String pl : m) {		
				if (pl.toString().matches(".*?xref#[0-9]{4}.*?")) {
					id[i] = pl;
				//	System.out.println("id:"+i + id[i]);
					 mm = sentence.replace("(" + id[i]+ ")", "");
					i++;
				}
			}
			/** id  = "Garland et al. xref#2001" */
			/** drop "xref#" */
		//	String c =id.replaceAll("xref#","");
			// System.out.println("cit:" + c);
		
			String[] cia = {mm};
		//	System.out.println("cia:" + cia[0]);
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
