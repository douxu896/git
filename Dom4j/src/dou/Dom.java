package dou;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import com.mysql.jdbc.PreparedStatement;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class Dom {

	public static void main(String[] args) throws DocumentException, SQLException {
		// TODO Auto-generated method stub
		SAXReader reader = new SAXReader();
		reader.setEntityResolver(new IgnoreDTDEntityResolver());
		String path = "/Users/douxu/Desktop/Adm_Policy_Ment_Health" + "/Adm_Policy_Ment_Health_2010_Mar_24_37(1-2)_15-26.nxml";
		Document doc = reader.read(new File(path));
		// get title,abstract,body
		Element title = (Element) doc.selectObject("/article/front/article-meta/title-group/article-title");
		Element abstractt = (Element) doc.selectObject("/article/front/article-meta/abstract");
		Element body = (Element) doc.selectObject("article/body");
		List<String> data = new ArrayList<String>();
		System.out.println(title.getName() + ":" + title.getText());
		System.out.println(abstractt.getName() + ":");
		Text(abstractt, data);
		System.out.println(body.getName() + ":");
		Text(body, data);
		JDBC db = new JDBC();
		Sentence sen;
		String sql = "insert into sentence(citation,sentences) values (?,?)";
	}

	private static void Text(Element element, List<String> data) {
		Iterator it = element.elementIterator();
		while (it.hasNext()) {
			Element ele = (Element) it.next();
			//System.out.println("name:" + ele.getName());
			if (ele.getName().equals("p")) {
				int num = ele.content().size();
				List content = ele.content();
				//System.out.println("元素个数：" + num);
				String Name = ele.getName();
				for (int cindex = 0; cindex < num; cindex++) {
					Object first = content.get(cindex);
					String firstText = getContentAsText(first);
					System.out.print(firstText);
				}
				System.out.println(" ");
			} else {
				/*if (!(ele.getText().equals(""))) {
					System.out.println(ele.getText());
				}*/
				Text(ele, data);
			}
		}
	}

	public static String getContentAsText(Object content) {
		if (content instanceof Node) {
			Node node = (Node) content;
			int type = node.getNodeType();
			switch (type) {
			case 1:
				return node.getText();
			case 3:
				return node.getText();

			default:
				break;
			}
		} else if (content instanceof String) {
			return (String) content;
		}

		return "";
	}
}