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

public class StringSplice {
	/**
	 * 
	 * @param str
	 * @throws SQLException
	 * @throws IOException
	 * @throws IllegalFormatException
	 * 
	 * @exception ClassNotFoundException
	 *                SqlException
	 * 
	 *                Ã·»°“˝Œƒæ‰º∞«∞∫Û»˝æ‰£¨≤¢Ω´Ω·π˚–¥»Î ˝æ›ø‚
	 */
	public static void stringSplice(String str) throws SQLException, IllegalFormatException, IOException {
		Connection con = SQLConnection.connection("root", "123456");
		String[] line = Detector(str);
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
			//System.out.println(lines.get(i));
			Matchs(lines.get(i)) ;
			}
		/*for (int i = 0; i < lines.size(); i++) {
			System.out.println(lines.get(i));
			if (lines.get(i).indexOf('(') > 0) {
				String authorAndYear = lines.get(i).substring(lines.get(i).indexOf('(') + 1, lines.get(i).indexOf(')'));
				String quotation = lines.get(i).substring(0, lines.get(i).indexOf('('));
				String context = "";
				for (int j = i - 3; j < i + 3; j++) {
					if (j > 0 && j < lines.size()) {
						context += lines.get(i);
					}
				}
				System.out.println(authorAndYear);
				String sql = "select * from quotationInfo where authorAndYear = ?;";
				// System.out.println(SQLConnection.findByAuthorAndYear(sql,
				// con, authorAndYear));
				if (SQLConnection.findByAuthorAndYear(sql, con, authorAndYear)) {
					String s2 = quotation + SQLConnection.RESULT.getString(2);
					String s3 = context + SQLConnection.RESULT.getString(3);
					String updateSQL = "update quotationInfo set quotation = ?, context = ? where authorAndYear = ?;";
					PreparedStatement pre = con.prepareStatement(updateSQL);
					pre.setString(1, s2);
					pre.setString(2, s3);
					pre.setString(3, authorAndYear);
					pre.executeUpdate();
				} else {
					String insertSQL = "insert into citation values(?,?,?);";
					PreparedStatement pre = con.prepareStatement(insertSQL);
					pre.setString(1, authorAndYear);
					pre.setString(2, quotation);
					pre.setString(3, context);
					pre.executeUpdate();
				}
			} else {
				System.out.println("-----end-------");
			}
		}*/
	}
	public static String[] Matchs(String sentence){
        // 要验证的字符串
//        String str = "(Weisz et al. xref#2006)";
        // 邮箱验证规则
        String regEx = "\\(.*?xref#[0-9]{4}(.*)?\\)";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sentence);
//        System.out.print(matcher.find());
        if(!matcher.find()){
       // 	 System.out.println("false");
            return  null;
        }else{
            String b = matcher.group();
//        System.out.println(b);
            String a = matcher.replaceFirst(" ");
            String regEx2 = "xref#";
            Pattern pattern1 = Pattern.compile(regEx2);
            Matcher matcher1 = pattern1.matcher(b);
            String c = matcher1.replaceFirst(" ");
            String s[] = {a,c};
            //for(String m:s){
            	 //System.out.println("citation:"+s[0]);
            	 System.out.println("citations:"+s[1]);
           // }
            return s;
        }
    }
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
		/*for (String s : sentences) {
			System.out.println(s);
		}*/
		modelIn.close();
	//	System.out.println("---------------1------------");
		return sentences;
	}
}
