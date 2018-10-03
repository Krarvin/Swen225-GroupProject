package persistence;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import gameworld.*;


/**
 * XML Reader which reads an XML save file.
 * @author hoongkevi
 *
 */

public class XmlReader {
  /**
   * reads an XML file from a given directory.
   * @param args
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */

  public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
    String filePath = "/home/hoongkevi/game.xml";
    File xml = new File(filePath);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = dbFactory.newDocumentBuilder();
    try {
      Document doc = builder.parse(xml);
      doc.getDocumentElement().normalize();

    } catch (IOException e) {
      System.out.println("Error");
    }


  }

  public void loadXml(Game game) {

  }
}
