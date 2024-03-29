package persistence;

import gameworld.Decoration;
import gameworld.GameWorld;
import gameworld.Item;
import gameworld.Key;
import gameworld.Location;
import gameworld.Player;
import gameworld.Quest;
import gameworld.StationaryContainer;
import gameworld.Treasure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * XML Reader which reads an XML save file.
 *
 * @author hoongkevi
 *
 */

public class XmlReader {
  /**
   * The XmlReader reads elements from an XML file and uses the information to create new
   * objects, ultimately recreating the game world.
   * @param filepath the file path to find the load file
   * @return the loaded GameWorld
   * @throws ParserConfigurationException configuration exceptions
   * @throws IOException Exception for invalid input or outputs
   * @throws SAXException XMl parser exception.
   */
  public static GameWorld loadXml(String filepath)
      throws ParserConfigurationException, SAXException, IOException {
    // String filePath = "/home/hoongkevi/Desktop/game";
    File xml = new File(filepath);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder db;
    try {
      db = dbFactory.newDocumentBuilder();
      Document doc = db.parse(xml);
      doc.getDocumentElement().normalize();

      NodeList nodelist = doc.getElementsByTagName("Game");
      List<GameWorld> elemList = new ArrayList<GameWorld>();
      for (int i = 0; i < nodelist.getLength(); i++) {
        elemList.add(getGameWorld(nodelist.item(i), doc));
      }
      return elemList.get(0);
    } catch (ParserConfigurationException e) {
      System.out.println("ParserConfigurationException error");
    } catch (SAXException e) {
      System.out.println("SAX Exception error");
    } catch (IOException e) {
      System.out.println("File input/output error");
    }
    return null;
  }

  /**
   * takes in the main game node and document and returns a new loaded game world.
   * @param node the main game node
   * @param doc the document we are reading from
   * @return GameWorld with the state of the game
   */
  public static GameWorld getGameWorld(Node node, Document doc) {
    Player player = new Player(getPlayerLocation(node, doc));
    Location[][] locations = getLocations(node, doc);
    getQuest(node, doc);
    GameWorld game = new GameWorld(locations, player.getCurrentLocation());
    return game;
  }

  /**
   * Takes in a Inventory node and returns a list of items.
   * @param node Inventory node
   * @param doc file we are reading from
   * @param player the player to assign the inventory
   * @return inventory.
   */
  public static List<Item> getInventory(Node node, Document doc, Player player) {
    NodeList nodelist = doc.getElementsByTagName("Item");
    List<Item> itemList = new ArrayList<Item>();
    for (int i = 0; i < nodelist.getLength(); i++) {
      Item item = getItem(nodelist.item(i), doc);
      itemList.add(item);
      player.pickupItem(item);
    }
    return itemList;
  }

  /**
   * Reads item information from the players inventory and creates and returns new Items.
   * @param node Player node
   * @param doc the document we are reading from
   * @return item created by reading xml
   */
  public static Item getItem(Node node, Document doc) {
    String type = doc.getDocumentElement().getElementsByTagName("Player").item(1).getAttributes()
        .getNamedItem("type").getNodeValue();
    String name = doc.getDocumentElement().getElementsByTagName("Player").item(1).getAttributes()
        .getNamedItem("name").getNodeValue();
    String description = doc.getDocumentElement().getElementsByTagName("Player").item(1)
        .getAttributes().getNamedItem("description").getNodeValue();
    String imagepath = doc.getDocumentElement().getElementsByTagName("Player").item(1)
        .getAttributes().getNamedItem("filepath").getNodeValue();
    if (type.equals("Treasure")) {
      Treasure treasure = new Treasure(name, description, imagepath);
      return treasure;
    } else if (type.equals("Key")) {
      Key key = new Key();
      return key;
    }
    return null;
  }

  /**
   * Reads a stationarycontainer element from xml file and creates respective
   * stationarycontainer object.
   * @param node the StationaryContainer node
   * @param doc the document we are reading from
   * @return new container
   */
  public static StationaryContainer getContainer(Node node, Document doc) {
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      Element elem = (Element) node;
      String name = elem.getAttribute("name");
      String description = elem.getAttribute("description");
      StationaryContainer loadContainer = new StationaryContainer(name, description, null);
      return loadContainer;
    }
    return null;
  }

  /**
   * gets all the quests from within the game by taking in a game node and reading
   * quest elements.
   * @param node the game node which stores the quest.
   * @param doc the document we are reading from.
   * @return set of all quests in the game
   */
  public static Set<Quest> getQuest(Node node, Document doc) {
    NodeList questList = doc.getDocumentElement().getElementsByTagName("quest");
    Set<Quest> quests = new HashSet<Quest>();
    List<Treasure> requirements = new ArrayList<Treasure>();
    for (int i = 0; i < questList.getLength(); i++) {
      String name = questList.item(i).getAttributes().getNamedItem("name").getNodeValue();
      String description = questList.item(i).getAttributes().getNamedItem("description")
          .getNodeValue();
      String imagepath = questList.item(i).getAttributes().getNamedItem("imagepath").getNodeValue();
      requirements = getQuestReqs(node, doc);
      quests.add(new Quest(name, description, imagepath, requirements));
    }
    return quests;

  }

  /**
   * Takes in the quest node and assigns all the requirements for the quest as a
   * list of treasures.
   * @param node the quest node which we are getting requirements for
   * @param doc the document we are reading from.
   * @return quest requirements
   */
  public static List<Treasure> getQuestReqs(Node node, Document doc) {
    NodeList questItems = doc.getDocumentElement().getElementsByTagName("QuestItems");
    List<Treasure> requirements = new ArrayList<Treasure>();
    for (int i = 0; i < questItems.getLength(); i++) {
      String name = questItems.item(i).getAttributes().getNamedItem("name").getNodeValue();
      String description = questItems.item(i).getAttributes().getNamedItem("description")
          .getNodeValue();
      String imagepath = questItems.item(i).getAttributes().getNamedItem("imagePath")
          .getNodeValue();
      Treasure treasure = new Treasure(name, description, imagepath);
      requirements.add(treasure);
    }
    return requirements;
  }

  /**
   * takes in a game node and returns the 2d array of locations in the game.
   * @param node the game node
   * @param doc the document we are reading from
   * @return new Location
   */
  public static Location[][] getLocations(Node node, Document doc) {
    NodeList nodelist = doc.getElementsByTagName("Location");
    Node game = doc.getElementsByTagName("Game").item(0);
    Location[][] locations;
    if (game.getNodeType() == Node.ELEMENT_NODE) {
      int x = Integer.parseInt(doc.getElementsByTagName("Game")
          .item(0).getAttributes().getNamedItem("width").getNodeValue());
      int y = Integer.parseInt(doc.getElementsByTagName("Game")
          .item(0).getAttributes().getNamedItem("height").getNodeValue());
      locations = new Location[x][y];
      for (int i = 0; i < locations.length; i++) {
        for (int j = 0; j < locations[0].length; j++) {
          for (int k = 0; k < nodelist.getLength(); k++) {
            locations[i][j] = makeLocation(nodelist.item(k), doc,k);
          }
        }
      }
      return locations;
    }
    return null;

  }

  /**
   * creates a new location by taking in a location node.
   * @param node the location node
   * @param doc document we are reading from
   * @param k the item to get from a nodelist
   * @return location
   */
  public static Location makeLocation(Node node, Document doc, int k) {
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      Node gridNode = doc.getDocumentElement().getElementsByTagName("grid").item(k);
      Item[][] grid = makegrid(gridNode, doc);
      Node exitNode = doc.getDocumentElement().getElementsByTagName("exits").item(k);
      Boolean[] exits = makeExits(exitNode, doc);
      Location location = new Location(exits, grid);
      return location;
    }
    return null;
  }

  /**
   * goes through the XML Document and assigns exits to the Location.
   * @param node the Location node which contains the exits we are creating
   * @param doc document we are reading from
   * @return An array of exits
   */
  public static Boolean[] makeExits(Node node, Document doc) {
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      Boolean[] exits = new Boolean[4];
      NodeList exitNodes = doc.getDocumentElement().getElementsByTagName("exit");
      for (int i = 0; i < exitNodes.getLength(); i++) {
        if (exitNodes.item(i).getNodeValue() != null) {
          exits[Integer.parseInt(exitNodes.item(i).getNodeValue())] = true;
        }
      }
      return exits;
    }
    return null;
  }

  /**
   * goes through the XML Document and creates a 2d array of grid items to assign to location.
   * @param node the location node
   * @param doc document we are reading from
   * @return returns item grid for the location given
   */
  public static Item[][] makegrid(Node node, Document doc) {
    NodeList nodelist = doc.getDocumentElement().getElementsByTagName("GridItem");
    Item[][] grid = new Item[2][2];
    int count = 0;
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < 2; j++) {
        grid[i][j] = makeGridItem(nodelist.item(count), doc, count);
        count++;
      }
    }
    return grid;
  }

  /**
   * creates a grid item to be put into a grid.
   * @param node the grid node
   * @param doc the file we are reading from
   * @param k the place in the nodelist to get item
   * @return gridItem
   */
  public static Item makeGridItem(Node node, Document doc, int k) {
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      String name = doc.getDocumentElement().getElementsByTagName("GridItem").item(k)
          .getAttributes().getNamedItem("name").getNodeValue();
      String description = doc.getDocumentElement().getElementsByTagName("GridItem").item(k)
          .getAttributes().getNamedItem("description").getNodeValue();
      String imagepath = doc.getDocumentElement().getElementsByTagName("GridItem").item(k)
          .getAttributes().getNamedItem("filepath").getNodeValue();
      String type = doc.getDocumentElement().getElementsByTagName("GridItem").item(k)
          .getAttributes().getNamedItem("type").getNodeValue();
      if (type.equals("treasure")) {
        return new Treasure(name, description, imagepath);
      } else if (type.equals("key")) {
        return new Key();
      } else if (type.equals("decoration")) {
        return new Decoration(name,description,imagepath);
      }
    }
    return null;

  }

  /**
   * returns the player location from the last load by reading the xml element for player.
   * @param node the player node
   * @param doc the document we are reading from
   * @return new PlayerLocation
   */
  public static Location getPlayerLocation(Node node, Document doc) {
    int x = Integer.parseInt(doc.getDocumentElement().getElementsByTagName("PlayerLocation").item(0)
        .getAttributes().getNamedItem("height").getNodeValue());
    int y = Integer.parseInt(doc.getDocumentElement().getElementsByTagName("PlayerLocation").item(0)
        .getAttributes().getNamedItem("width").getNodeValue());
    Item[][] grid = new Item[x][y];
    NodeList gridItemList = doc.getDocumentElement().getElementsByTagName("GridItem");
    for (int i = 0; i < gridItemList.getLength(); i++) {
      String name = gridItemList.item(i).getAttributes().getNamedItem("name").getNodeValue();
      String description = gridItemList.item(i).getAttributes().getNamedItem("description")
          .getNodeValue();
      String type = gridItemList.item(i).getAttributes().getNamedItem("type").getNodeValue();
      String filepath = gridItemList.item(i).getAttributes()
          .getNamedItem("filepath").getNodeValue();
      int gridItemX = Integer
          .parseInt(gridItemList.item(i).getAttributes().getNamedItem("x").getNodeValue());
      int gridItemY = Integer
          .parseInt(gridItemList.item(i).getAttributes().getNamedItem("y").getNodeValue());
      if (type.equals("Treasure")) {
        grid[gridItemX][gridItemY] = new Treasure(name, description, filepath);
      } else if (type.equals("Key")) {
        grid[gridItemX][gridItemY] = new Key();
      } else if (type.equals("decoration")) {
        grid[gridItemX][gridItemY] = new Decoration(name,description,filepath);
      }

    }
    doc.getDocumentElement().getElementsByTagName("exits");
    Boolean[] exits = new Boolean[4];
    for (int i = 0; i < 2; i++) {
      int exit = Integer.parseInt(doc.getDocumentElement().getElementsByTagName("exit").item(i)
          .getAttributes().getNamedItem("exitdirection").getNodeValue());
      exits[exit] = true;
    }

    Location playerLocation = new Location(exits, grid);

    return playerLocation;
  }

  /**
   * gets the player by reading the player element from the game Xml load file.
   * @param node the game node
   * @param doc the document we are reading from
   * @return player elements
   */
  public static Player getPlayer(Node node, Document doc) {
    Player player = new Player(getPlayerLocation(node, doc));
    getInventory(node, doc, player);
    return player;
  }


}
