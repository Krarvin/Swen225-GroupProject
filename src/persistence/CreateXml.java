package persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

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
   * @return the new Element for room.
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
   * @param item
   * @param doc
   * @return new treasure element
   */
  public static Element makeTreasure(gameworld.Treasure item, Document doc) {
    Element treasure = doc.createElement("Treasure");
    Attr name = doc.createAttribute("name");
    Attr description = doc.createAttribute("description");
    name.setValue(item.getName());
    description.setValue(item.getDescription);
    treasure.setAttributeNode(name);
    treasure.setAttributeNode(description);
    return treasure;

  }

  /**
   * @param container
   * @param doc
   * @return new container element
   */
  public static Element makeStationaryContainer(gameworld.StationaryContainer container, Document doc) {
    Element StationaryContainer = doc.createElement("StationaryContainer");
    Attr name = doc.createAttribute("name");
    Attr description = doc.createAttribute("description");
    Element contents = doc.createElement("contents");
    //create items and add them to contents here


    return null;
  }

  /**
   * @param quest
   * @param doc
   * @return new quest element
   */
  public static Element makeQuest(gameworld.Quest quest, Document doc) {
    Element questelem = doc.createElement("quest");
    Attr complete = doc.createAttribute("complete");
    Element requirements = doc.createElement("requirements");
    //create items and assign them to quest requirements;
    return questelem;
  }

  /**
   * @param location
   * @param doc
   * @return location element
   */
  public static Element makeLocation(gameworld.Location location, Document doc) {
    Element locationElem = doc.createElement("Location");
    Element grid = doc.createElement("grid");
    for(int i =0; i<location.getGrid().length; i++) {
      for(int j =0; i<location.getGrid()[0].length; j++) {
        if(location.getGrid()[i][j] instanceof Item) {
          Element GridItem = doc.createElement("item");
          Attr name = doc.createAttribute("name");
          Attr description = doc.createAttribute("description");
          Attr x = doc.createAttribute("x");
          Attr y = doc.createAttribute("y");
          x.setValue(i + "");
          y.setValue(j + "");
          name.setValue(location.getGrid()[i][j].getName());
          description.setValue(location.getGrid()[i][j].getDescription());
          GridItem.setAttributeNode(name);
          GridItem.setAttributeNode(description);
          GridItem.setAttributeNode(x);
          GridItem.setAttributeNode(y);
          grid.appendChild(GridItem);
        }
      }
    }
    Element exits = doc.createElement("exits");
    for(int i=0; i<location.getExits().length; i++) {
      if(location.getExits()[i] != null) {
        Element exit = doc.createElement("exit");
        Attr exitLoc = doc.createAttribute("exit direction");
        exitLoc.setValue(""+ i);
        exits.appendChild(exit);
      }
    }
    Attr width = doc.createAttribute("width");
    Attr height = doc.createAttribute("height");
    width.setValue(location.getGrid().length + "");
    height.setValue(location.getGrid()[0].length + "");
    locationElem.setAttributeNode(width);
    locationElem.setAttributeNode(height);
    locationElem.appendChild(grid);
    locationElem.appendChild(exits);
    return locationElem;
  }

  /**
   * @param key
   * @param doc
   * @return new key element
   */
  public static Element makeKey(gameworld.Key key, Document doc) {
    Element keyElem = doc.createElement("key");
    Element unlocks = doc.createElement("unlocks");
    Element passage = doc.createElement("passage");
    Attr loc1 = doc.createAttribute("loc1");
    Attr loc2 = doc.createAttribute("loc2");
    Attr locked = doc.createAttribute("locked");
    loc1.setValue(key.getUnlocks().getOtherLocation();
    locked.setValue(key.getUnlocks().toString());
    unlocks.appendChild(passage);
    keyElem.appendChild(unlocks);
    return keyElem;
  }

  /**
   * @param decoration
   * @param doc
   * @return new decor
   */
  public static Element makeDecoration(gameworld.Decoration decoration, Document doc) {
    Element decoElem = doc.createElement("decoration");
    Attr name = doc.createAttribute("name");
    Attr description = doc.createAttribute("description");
    Attr filepath = doc.createAttribute("filepath");
    return decoElem;
  }

  /**
   * @param player
   * @param collection
   * @param doc
   * @return player inventory
   */
  public static Element makeInventory(Collection<Item> collection, Document doc) {
    Element Inventory = doc.createElement("Inventory");
    for(Item i : collection) {
      Element item = doc.createElement("Item");
      Attr name = doc.createAttribute("name");
      Attr description = doc.createAttribute("description");
      Attr image = doc.createAttribute("imagePath");
      name.setValue(i.getName());
      description.setValue(i.getDescription());
      image.setValue(i.getImage().toString());
      item.setAttributeNode(name);
      item.setAttributeNode(description);
      item.setAttributeNode(image);
      Inventory.appendChild(item);
    }

    return Inventory;
  }

  /**
   * @param player
   * @param doc
   * @return new player ELement.
   */
  public static Element makePlayer(gameworld.Player player, Document doc) {
    Element Player = doc.createElement("Player");
    Element Inventory = makeInventory(player.getInventory(), doc);
    Player.appendChild(Inventory);
    Element Location = makeLocation(player.getCurrentLocation(), doc);
    Attr facing = doc.createAttribute("y");
    facing.setValue(player.getFacing().toString());
    Player.appendChild(Location);
    Player.setAttributeNode(facing);
    return Player;
  }

  /**
   * @param passage
   * @param doc
   * @return a new passage element
   */
  public static Element makePassage(gameworld.Passage passage, Document doc) {
    Element Passage = doc.createElement("passage");
    Element loc1 = doc.createAttribute("loc1");
    Attr loc2 = doc.createAttribute("loc2");
    Attr blocked = doc.createAttribute("blocked");
    loc1.setValue(passage.loc1);
    loc2.setValue(passage.loc2);
    blocked.setValue(passage.blocked);
    Passage.setAttributeNode(loc1);
    Passage.setAttributeNode(loc2);
    Passage.setAttributeNode(blocked);
    return Passage;

  }

 /**
 * @param game
 * @throws ParserConfigurationException
 */
public void makeXml(GameWorld game) throws ParserConfigurationException {
   DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
   DocumentBuilder db = df.newDocumentBuilder();
   Document document = db.newDocument();

   Element gamefile = document.createElement("Game");
   document.appendChild(gamefile);

   Element player = document.createElement("Player");
   gamefile.appendChild(player);

   Element inventory = document.createElement("Inventory");
   player.appendChild(inventory);

   for(Item i: gameworld.Player.get) {
       Attr temp = document.createAttribute(i.getName());
       inventory.setAttributeNode(temp);

   }
 }




  /**
   * gets the file path.
   * @return filepath
   */
  public static String getFilepath() {
    return filePath;
  }

}
