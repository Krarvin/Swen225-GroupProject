package persistence;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import gameworld.*;
import mapeditor.*;
import renderer.*;
import application.*;

/**
 * Creates an XML file which fits our games elements and description.
 * @author hoongkevi
 *
 */
public class CreateXml {
  private static final String filePath = "C:\\Users\\krarv\\Desktop\\game.xml";


  /**
   * creates the XML file by taking game attributes and creating elements.
   * @param args main function.
   * @throws ParserConfigurationException parser exception.
   * @throws TransformerException transformer exception.
   */
  public static void main(String[] args) throws ParserConfigurationException, TransformerException {
    try {
      DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = df.newDocumentBuilder();
      Document document = db.newDocument();

      //creates a game Element
      Element game = document.createElement("Game");
      document.appendChild(game);
      
      //creates a locations Element which is appended to game
      Element locations = document.createElement("Locations");
      game.appendChild(locations);
      
      //creates player element which is appended to game
      Element player = document.createElement("Player");
      game.appendChild(player);
      
      
      //creates an inventory element which is appended to player
      Element inventory = document.createElement("Inventory");
      player.appendChild(inventory);
      
      Element rooms = document.createElement("Rooms");
      game.appendChild(rooms);
      
      rooms.appendChild(makeRoom(0, 0, 0, 0, "Siglo_Balcony", document));
      
      
      
      //creates player attributes and assigns them to player
      Attr direction = document.createAttribute("Direction");
      direction.setValue("NORTH");
      player.setAttributeNode(direction);
      
      Attr healthAttr = document.createAttribute("score");
      healthAttr.setValue("0");
      player.setAttributeNode(healthAttr);

      Attr attr = document.createAttribute("health");
      attr.setValue("100");
      player.setAttributeNode(attr);

      Attr attr1 = document.createAttribute("x");
      attr1.setValue("0");
      player.setAttributeNode(attr1);

      Attr attr2 = document.createAttribute("y");
      attr2.setValue("0");
      player.setAttributeNode(attr2);



      Element durries = document.createElement("durries");
      inventory.appendChild(durries);
      
      //Uses a transformer to stream the file into an XML file using DOMSource.
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      DOMSource ds = new DOMSource(document);
      StreamResult sr = new StreamResult(new File(filePath));
      transformer.transform(ds,sr);
      System.out.println("XML File Created");
    } catch (ParserConfigurationException e) {
      System.out.print("parser configuration exception");
    } catch (TransformerException e) {
      System.out.println("Transformer exception");
    }
  }
  
  /**
   * This method takes in room data and creates elements and respective attributes and then adds them to the document.
   * @param width width of the room
   * @param height
   * @param x position of room
   * @param y position of room
   * @param name of room
   * @param doc the document where we are adding the room element
   * @return the new room.
   */
  public static Element makeRoom(int width, int height, int x, int y, String name, Document doc) {
    Element room1 = doc.createElement(name);
    Attr width1 = doc.createAttribute("width");
    Attr height1 = doc.createAttribute("height");
    Attr x1 = doc.createAttribute("x");
    Attr y1 = doc.createAttribute("y");
    width1.setValue("0");
    height1.setValue("0");
    x1.setValue("0");
    y1.setValue("0");
    room1.setAttributeNode(width1);
    room1.setAttributeNode(height1);
    room1.setAttributeNode(x1);
    room1.setAttributeNode(y1);
    
    return room1;
  }

 /**
 * @param game
 * @throws ParserConfigurationException
 */
//public void makeXml(GameWorld game) throws ParserConfigurationException {
//   DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
//   DocumentBuilder db = df.newDocumentBuilder();
//   Document document = db.newDocument();
//
//   Element gamefile = document.createElement("Game");
//   document.appendChild(gamefile);
//
//   Element player = document.createElement("Player");
//   gamefile.appendChild(player);
//
//   Element inventory = document.createElement("Inventory");
//   player.appendChild(inventory);

//   for(Item i: gameworld.Player.inventory) {
//       Attr temp = document.createAttribute(i.getName());
//       inventory.setAttributeNode(temp);
//
//   }
// }
 
 


  /**
   * gets the file path.
   * @return filepath
   */
  public static String getFilepath() {
    return filePath;
  }

}
