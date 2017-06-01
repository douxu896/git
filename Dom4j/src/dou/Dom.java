package dou;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.mysql.jdbc.PreparedStatement;

public class Dom {

	public static void main(String[] args) throws DocumentException, SQLException {
		// TODO Auto-generated method stub
		SAXReader reader = new SAXReader();
		reader.setEntityResolver(new IgnoreDTDEntityResolver());
		String path = "/Users/douxu/Desktop" + "/Adm_Policy_Ment_Health_2010_Mar_15_37(1-2)_201-204.nxml";
		// Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		Document doc = reader.read(new File(path));
		// 取得文章名，引言和正文
		Element title = (Element) doc.selectObject("/article/front/article-meta/title-group/article-title");
		Element abstractt = (Element) doc.selectObject("/article/front/article-meta/abstract");
		Element body = (Element) doc.selectObject("article/body");
		List<String> data = new ArrayList<String>();
		System.out.println(title.getName());
		System.out.println(title.getText());
		// Text(title, data);
		System.out.println(abstractt.getName());
		Text(abstractt, data);
		System.out.println(body.getName());
		Text(body, data);
		JDBC db = new JDBC();
		Sentence sen;
		 String sql="insert into sentence(citation,sentences) values (?,?)";
	}

	private static void exportCsv(File file, List<String> dataList) {
		// TODO 自动生成的方法存根
		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			out = new FileOutputStream(file);
			osw = new OutputStreamWriter(out);
			bw = new BufferedWriter(osw);
			if (dataList != null && !dataList.isEmpty()) {
				for (String data : dataList) {
					bw.append(data).append("\r");
				}
			}
			// isSucess=true;
		} catch (Exception e) {
			// isSucess=false;
		} finally {
			if (bw != null) {
				try {
					bw.close();
					bw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (osw != null) {
				try {
					osw.close();
					osw = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private static void Text(Element node, List<String> data) {
		// TODO 自动生成的方法存根
		/*
		 * if(!node.getText().equals("")) { System.out.println(node.getText());
		 * }
		 */
		Iterator it = node.elementIterator();
		while (it.hasNext()) {
			// System.out.println("======");
			Element element = (Element) it.next();
			Text(element, data);
			  System.out.println(element.attributeValue("ref-type"));
			Iterator iter = element.attributeIterator();
			while (iter.hasNext()) {
            
              if( iter.next().toString().matches(""));
			}
			if (!element.getText().equals("")) {
				data.add(element.getText());
				System.out.println(element.getText());
			}

		}
	}

	private static void SavaXml(String fileName) {
		Document document = DocumentHelper.createDocument();// 建立document对象，用来操作xml文件
		Element booksElement = document.addElement("books");// 建立根节点
		booksElement.addComment("This is a test for dom4j ");// 加入一行注释
		Element bookElement = booksElement.addElement("book");// 添加一个book节点
		bookElement.addAttribute("show", "yes");// 添加属性内容
		Element titleElement = bookElement.addElement("title");// 添加文本节点
		titleElement.setText("ajax in action");// 添加文本内容
		try {
			XMLWriter writer = new XMLWriter(new FileWriter(new File(fileName)));
			writer.write(document);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
