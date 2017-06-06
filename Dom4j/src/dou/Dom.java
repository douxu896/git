package dou;

import java.io.File;
import java.io.IOException;
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

public class Dom {
	public static void main(String[] args) throws DocumentException, SQLException {
		// TODO Auto-generated method stub
		SAXReader reader = new SAXReader();
		reader.setEntityResolver(new IgnoreDTDEntityResolver());
		String path = "/Users/douxu/Desktop/Adm_Policy_Ment_Health"
				+ "/Adm_Policy_Ment_Health_2010_Mar_30_37(1-2)_128-131.nxml";
		Document doc = reader.read(new File(path));
		// get title,abstract,body
		Element title = (Element) doc.selectObject("/article/front/article-meta/title-group/article-title");
		Element abstractt = (Element) doc.selectObject("/article/front/article-meta/abstract");
		Element body = (Element) doc.selectObject("article/body");
		System.out.println(title.getName() + ":" + title.getText());
		System.out.println(abstractt.getName() + ":");
		//Text(abstractt);
		System.out.println("");
		System.out.println(body.getName() + ":");
		Text(body);
	}

	private static void Text(Element element) {
		Iterator it = element.elementIterator();
		while (it.hasNext()) {
			Element ele = (Element) it.next();
			if (ele.getName().equals("p")) {
				paragraph(ele);
			} else {
				Text(ele);
			}
		}
	}

	private static void paragraph(Element ele) {
		// TODO 自动生成的方法存根
		int num = ele.content().size();
		List content = ele.content();
		StringBuilder sb = new StringBuilder();
		for (int cindex = 0; cindex < num; cindex++) {
			Object first = content.get(cindex);
			String firstText = getContentAsText(first);
			sb.append(firstText);
		}
	//	System.out.print(sb.toString());
		try {
			StringSplice.stringSplice(sb.toString());
		} catch (IllegalFormatException | SQLException | IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public static String getContentAsText(Object content) {
		if (content instanceof Node) {
			Node node = (Node) content;
			int type = node.getNodeType();
			switch (type) {
			case Node.ELEMENT_NODE:
				Element m = (Element) content;
				//if(m.attribute(0).)
				Iterator it = m.attributeIterator();
				while(it.hasNext()){
					System.out.println("it"+);
					if(it.toString().equals("bibr")){
						System.out.println(node.getName() + "#" + node.getText());
						return node.getName() + "#" + node.getText();	
					}
				}
				
				
			case Node.TEXT_NODE:
				return node.getText();
			default:
				System.out.println("");
				System.out.println("other type: " + type);
				break;
			}
		} else if (content instanceof String) {
			return (String) content;
		}
		return "";
	}
}