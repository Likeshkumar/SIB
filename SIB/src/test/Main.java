package test;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Main {
  public static void main(String arg[]) throws Exception{
    String xmlRecords = "<data><employee><name>A</name>"
        + "<title>Manager</title></employee></data>";
    String request = "<WATERPAYMENTDETAILS><PRN>21147305</PRN><AREA>Kampala</AREA></WATERPAYMENTDETAILS>";
    DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(request));

    Document doc = db.parse(is);
    NodeList nodes = doc.getElementsByTagName("WATERPAYMENTDETAILS");

    for (int i = 0; i < nodes.getLength(); i++) {
      Element element = (Element) nodes.item(i);

      NodeList name = element.getElementsByTagName("PRN");
      Element line = (Element) name.item(0);
      String PRN = getCharacterDataFromElement(line);
      System.out.println("PRN: " + PRN);

      NodeList title = element.getElementsByTagName("AREA");
      line = (Element) title.item(0);
      System.out.println("Title: " + getCharacterDataFromElement(line));
      
      String ss = "HMASJH0038CB9F9F8F2633EE";
      System.out.println(ss.substring(8,24));
      
    }

  }

  public static String getCharacterDataFromElement(Element e) {
    Node child = e.getFirstChild();
    if (child instanceof CharacterData) {
      CharacterData cd = (CharacterData) child;
      return cd.getData();
    }
    return "";
  }
}
