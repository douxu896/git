package dou;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * <p>
 * <code>MainTest</code> is main class
 * </p>
 * 
 * @author <a href="bjtu:15121684@bjtu.edu.cn">Emily </a>
 * @version $Revision: 2.0 $
 */
public class Dom {

	public static void main(String[] args) throws DocumentException, SQLException {
		SAXReader reader = new SAXReader();
		/** ignore ignore DTDEntity */
		reader.setEntityResolver(new IgnoreDTDEntityResolver());
		/** input path */
		String inpath = "/Users/douxu/Desktop/xml";
		List<String> file = FilePath.getListFiles(inpath, "", true);
		Iterator<String> it = file.iterator();
		while (it.hasNext()) {
			String filename = (String) it.next();
			System.out.println(filename);
			if (new File(filename).exists()) {
				Document doc = reader.read(new File(filename));
				//System.out.println(filename);
				Element title = (Element) doc.selectObject("/article/front/article-meta/title-group/article-title");
				System.out.println(title.getName() + ":" + title.getText());
				/** use xpath to location abstract */
				//doc.selectObject(filename).
				//if((Element) doc.selectObject("/article/front/article-meta/abstract").)
				//Element abstractt = (Element) doc.selectObject("/article/front/article-meta/abstract");
				//Text(abstractt);
				/** use xpath to location main body */
				Element body = (Element) doc.selectObject("article/body");
				/** deal with the context of abstract and main body */
				
				
				Text(body);
			}
		}
	}

	/**
	 * <p>
	 * Iterate whole node from element use method paragraph to get text
	 * 
	 * @param Element
	 *            abstract and body
	 *
	 * @return void
	 */
	private static void Text(Element element) {
		Iterator it = element.elementIterator();
		while (it.hasNext()) {
			Element ele = (Element) it.next();
			/** if ele is paragraph ,then get text */
			if (ele.getName().equals("p")) {
				paragraph(ele);
				Text(ele);
			} else {
				Text(ele);
			}
		}
	}

	/**
	 * <p>
	 * paragraph get the all text from paragraph use StringSplice to take into
	 * sentences and three parts(authorandyear,citation,cintext)
	 * 
	 * @param Element
	 *            every paragraph
	 * @return void
	 *
	 * @throws IllegalFormatException
	 * @throws SQLException
	 * @throws IOException
	 *             e
	 */
	private static void paragraph(Element ele) {
		// TODO 自动生成的方法存根
		int num = ele.content().size();
		List content = ele.content();
		/** sb is the text of paragraph */
		StringBuilder sb = new StringBuilder();
		for (int cindex = 0; cindex < num; cindex++) {
			Object first = content.get(cindex);
			String firstText = getContentAsText(first);
			sb.append(firstText);
		}

		/** splice the text */
		try {
			// System.out.println(sb.toString());
			StringSplice.stringSplice(sb.toString());
		} catch (IllegalFormatException | SQLException | IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * get text from contents @param Object content every paragraph's
	 * content @return String return to method paragraph,is text of every
	 * element @throws
	 */
	public static String getContentAsText(Object content) {
		if (content instanceof Node) {
			Node node = (Node) content;
			int type = node.getNodeType();
			switch (type) {
			/** if element is ref-type, add xref# before year */
			case Node.ELEMENT_NODE:
				Element m = (Element) content;
				/** a list contains all attributes of element */
				List<Attribute> listAttr = m.attributes();
				if (!listAttr.isEmpty()) {
					Attribute attr = listAttr.get(0);
					/** name of attribute */
					String name = attr.getName();
					/** value of attribute */
					String value = attr.getValue();
					if (name.equals("ref-type") && value.equals("bibr")) {
						return node.getName() + "#" + node.getText();
					} else
						return node.getText();
				} else
					return node.getText();
			case Node.TEXT_NODE:
				return node.getText();
			default:
				System.out.println("");
				System.out.println("other nodetype: " + type);
				break;
			}
		} else if (content instanceof String) {
			return (String) content;
		}
		return "";
	}
}