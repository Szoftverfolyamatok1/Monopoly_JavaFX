package Monopoly.Model.XML;

import Monopoly.Model.Objects.BoardElement;
import Monopoly.Model.Cards.*;
import Monopoly.Model.LoadObjects.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import static Monopoly.Logger.LoggerClass.doLog;

public class XMLParser{

	private ArrayList<BoardElement> boardElements;
	private ArrayList<Card> cardElements;
	private LoadObject loadObject;

	public ArrayList<BoardElement> handleBoardElementXML(String xmlFileName)
	{
		boardElements = new ArrayList<BoardElement>();
		getBoardElementCoordinates(xmlFileName);
        doLog(Level.INFO, "BoardElementek betöltve, darab: " + boardElements.size());
		return boardElements;
	}

	//generic function which returns an array of a card based type
	public <T> T handleCardsXML(String xmlFileName, String typeName, Class<T> type)
	{
		cardElements = new ArrayList<Card>();
		getCardData(xmlFileName, typeName);
        doLog(Level.INFO, typeName + " betöltve, darab: " + cardElements.size());
		return type.cast(cardElements);
	}

	private void getCardData(String xmlFileName, String type)
	{
		try {
			NodeList nList = getNodeListFromFile(xmlFileName);
			addCardElementsToList(nList, type);
		} catch (Exception e) {
            doLog(Level.WARNING, e.getMessage());
//			throw new NullPointerException("Cannot initialize document with wrong location!");
		}
	}

	private void getBoardElementCoordinates(String xmlFileName)
	{
		try {
			NodeList nList = getNodeListFromFile(xmlFileName);
			addBoardElementsToList(nList);
		} catch (Exception e) {
            doLog(Level.WARNING, e.getMessage());
//			throw new NullPointerException("Cannot initialize document with wrong location!");
		}
	}

	//gets the node list from the file ( nList )
	private NodeList getNodeListFromFile(String xmlFileName) throws ParserConfigurationException, SAXException, IOException {
		try{
			//sample XMLParser code, nothing to change
			File fXmlFile = new File(xmlFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			return doc.getElementsByTagName("element");
		} catch ( Throwable throwable ) {
		    System.out.println(throwable.getMessage());
		}
		return null;
	}

	private void addBoardElementsToList(NodeList nList)
	{
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
					BoardElement element = createBoardElement(eElement);
					boardElements.add(element);
			}
		}
	}

	private void addCardElementsToList(NodeList nList, String type) {
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

                //if it isn't working, just change equals to ==
				if (type.equals("CommunityChest")) {
					cardElements.add(createCommunityChestElement(eElement));
				}
				else if (type.equals("Chance")) {
					cardElements.add(createChanceElement(eElement));
				}
				else if (type.equals("Property")) {
					cardElements.add(createPropertyElement(eElement));
				}
			}
		}
	}

	//get the node's elements and create a Property card
	private PropertyCard createPropertyElement(Element eElement) {
		if ( PropertyCard.PropertyType.valueOf(eElement.getElementsByTagName("type").item(0).getTextContent()) == PropertyCard.PropertyType.UTILITY )
		{
            return createUtilityElement(eElement);
        }
		else if (PropertyCard.PropertyType.valueOf(eElement.getElementsByTagName("type").item(0).getTextContent()) == PropertyCard.PropertyType.RAILING )
		{
            return createRailingElement(eElement);
        }
		else if ( PropertyCard.PropertyType.valueOf(eElement.getElementsByTagName("type").item(0).getTextContent()) == PropertyCard.PropertyType.SIMPLE )
		{
            return createPlotElement(eElement);
        }
		return null;
	}

    private PropertyCard createPlotElement(Element eElement) {
        return new PlotPropertyCard(
            Integer.parseInt(eElement.getAttribute("id"))
            ,eElement.getElementsByTagName("name").item(0).getTextContent()
            ,PlotPropertyCard.Colour_Type.valueOf(eElement.getElementsByTagName("colour").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("cost").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue0").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue1").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue2").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue3").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue4").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValueHotel").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("mortgageValue").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("houseCost").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("hotelCost").item(0).getTextContent())
        );
    }

    private PropertyCard createRailingElement(Element eElement) {
        return new RailingPropertyCard(
            Integer.parseInt(eElement.getAttribute("id"))
            ,eElement.getElementsByTagName("name").item(0).getTextContent()
            ,Integer.parseInt(eElement.getElementsByTagName("cost").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue1").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue2").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue3").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue4").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("mortgageValue").item(0).getTextContent())
        );
    }

    private PropertyCard createUtilityElement(Element eElement) {
        return new UtilityPropertyCard(
            Integer.parseInt(eElement.getAttribute("id"))
            ,eElement.getElementsByTagName("name").item(0).getTextContent()
            ,Integer.parseInt(eElement.getElementsByTagName("cost").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("mortgageValue").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue1").item(0).getTextContent())
            ,Integer.parseInt(eElement.getElementsByTagName("rentalValue2").item(0).getTextContent())
        );
    }

    //get the node's elements and create a Chance card
	private ChanceCard createChanceElement(Element eElement) {
		return new ChanceCard(
			Integer.parseInt(eElement.getAttribute("id"))
			,eElement.getElementsByTagName("name").item(0).getTextContent()
			,eElement.getElementsByTagName("description").item(0).getTextContent()
			,Card.Type.valueOf(eElement.getElementsByTagName("type").item(0).getTextContent())
			,eElement.getElementsByTagName("amount").item(0).getTextContent()
		);
	}

	//get the node's elements and create a Community card
	private CommunityChestCard createCommunityChestElement(Element eElement) {
		return new CommunityChestCard(
			Integer.parseInt(eElement.getAttribute("id"))
			,eElement.getElementsByTagName("name").item(0).getTextContent()
			,eElement.getElementsByTagName("description").item(0).getTextContent()
			,Card.Type.valueOf(eElement.getElementsByTagName("type").item(0).getTextContent())
			,eElement.getElementsByTagName("amount").item(0).getTextContent()
		);
	}

	//get the node's elements and create a Board element
	private BoardElement createBoardElement(Element eElement) {
		return new BoardElement(
			Integer.parseInt(eElement.getAttribute("id"))
			,eElement.getElementsByTagName("name").item(0).getTextContent()
		);
	}

	public LoadObject handleLoadXML(String xmlFileName)
	{
		loadObject = new LoadObject();
		getLoadObject(xmlFileName);
        doLog(Level.INFO, "A LoadObjectek sikeresen be lettek töltve.");
		return loadObject;
	}

	private void getLoadObject(String xmlFileName)
	{
		try {
			NodeList nList = getNodeListFromLoadFile(xmlFileName);
			NodeList bList = getBankNodeListFromLoadFile(xmlFileName);
			addElementsToLoadObject(nList);
			addBankElementsToLoadObject(bList);
		} catch (Exception ignored) {
		}
	}

	private void addBankElementsToLoadObject(NodeList bList) {
		for (int temp = 0; temp < bList.getLength(); temp++)
		{
			Node nNode = bList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				ArrayList<LoadObjectBankProperty> lObjPropertyList = new ArrayList<LoadObjectBankProperty>();
				NodeList propertyList = eElement.getElementsByTagName("propertyElement");
                doLog(Level.INFO, "Beolvasás: A bank területek száma: " + propertyList.getLength());
                doLog(Level.INFO, "Beolvasás: A bank pénze: " + eElement.getElementsByTagName("bankMoney").item(0).getTextContent());
				for ( int i = 0; i < propertyList.getLength(); ++i )
				{
					Node pNode = propertyList.item(i);
					if (nNode.getNodeType() == Node.ELEMENT_NODE)
					{
						Element pElement = (Element)pNode;

                        LoadObjectBankProperty lObjBankProperty = new LoadObjectBankProperty(
							Integer.parseInt(pElement.getAttribute("pId"))
							,Boolean.valueOf(pElement.getElementsByTagName("isOwned").item(0).getTextContent())
						);

                        doLog(Level.INFO, "Beolvasás: pId: " + pElement.getAttribute("pId"));
                        doLog(Level.INFO, "Beolvasás: isOwned: " + pElement.getElementsByTagName("isOwned").item(0).getTextContent());

						lObjPropertyList.add(lObjBankProperty);
					}
				}

				LoadObjectBank lObjBank = new LoadObjectBank(Integer.parseInt(eElement.getElementsByTagName("bankMoney").item(0).getTextContent())
															,lObjPropertyList);

				loadObject.setLoadObjectBank(lObjBank);
			}
		}
	}

	private NodeList getNodeListFromLoadFile(String xmlFileName) throws ParserConfigurationException, SAXException, IOException {
		try{
			File fXmlFile = new File("Saves/" + xmlFileName);
            doLog(Level.INFO, "Filenév: " + xmlFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			return doc.getElementsByTagName("playerElement");
		} catch ( Throwable throwable ) {
			System.out.println(throwable.getMessage());
		}
		return null;
	}

	private NodeList getBankNodeListFromLoadFile(String xmlFileName) throws ParserConfigurationException, SAXException, IOException {
		try{
			File fXmlFile = new File("Saves/" + xmlFileName);
            doLog(Level.INFO, "Filenév: " + xmlFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			return doc.getElementsByTagName("bankElement");
		} catch ( Throwable throwable ) {
			System.out.println(throwable.getMessage());
		}
		return null;
	}

	private void addElementsToLoadObject(NodeList nList)
	{
		for (int temp = 0; temp < nList.getLength(); temp++)
		{
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				ArrayList<LoadObjectProperty> lObjPropertyList = new ArrayList<LoadObjectProperty>();
				NodeList propertyList = eElement.getElementsByTagName("propertyElement");
				for ( int i = 0; i < propertyList.getLength(); ++i )
				{
					Node pNode = propertyList.item(i);
					if (nNode.getNodeType() == Node.ELEMENT_NODE)
					{
						Element pElement = (Element)pNode;
						LoadObjectProperty lObjProperty = null;

                        lObjProperty = checkForPropertyType(eElement, pElement, lObjProperty);

                        lObjPropertyList.add(lObjProperty);
					}
				}

				ArrayList<LoadObjectCard> lObjCardList = new ArrayList<LoadObjectCard>();
				NodeList cardList = eElement.getElementsByTagName("cardElement");
				for ( int i = 0; i < cardList.getLength(); ++i )
				{
					Node cNode = cardList.item(i);
					if (cNode.getNodeType() == Node.ELEMENT_NODE)
					{
						Element cElement = (Element)cNode;
						LoadObjectCard lObjCard = new LoadObjectCard(Integer.parseInt(cElement.getAttribute("cId")));
						lObjCardList.add(lObjCard);
					}
				}

				LoadObjectPlayer lObjPlayer = new LoadObjectPlayer(eElement.getElementsByTagName("name").item(0).getTextContent()
					,Integer.parseInt(eElement.getElementsByTagName("cash").item(0).getTextContent())
					,Integer.parseInt(eElement.getElementsByTagName("location").item(0).getTextContent())
					,lObjPropertyList
					,lObjCardList);

				loadObject.addLoadObjectPlayer(lObjPlayer);
			}
		}
	}

    private LoadObjectProperty checkForPropertyType(Element eElement, Element pElement, LoadObjectProperty lObjProperty) {
        if ( PropertyCard.PropertyType.valueOf(pElement.getElementsByTagName("pType").item(0).getTextContent() ) == PropertyCard.PropertyType.SIMPLE )
        {
            lObjProperty = new LoadObjectProperty(Integer.parseInt(pElement.getAttribute("pId"))
                ,PropertyCard.PropertyType.valueOf(pElement.getElementsByTagName("pType").item(0).getTextContent())
                ,Integer.parseInt(eElement.getElementsByTagName("houseNumber").item(0).getTextContent())
                ,Boolean.valueOf(eElement.getElementsByTagName("hasHotel").item(0).getTextContent()));
        }
        else if ( PropertyCard.PropertyType.valueOf(eElement.getElementsByTagName("pType").item(0).getTextContent() ) == PropertyCard.PropertyType.UTILITY )
        {
            lObjProperty = new LoadObjectProperty(Integer.parseInt(pElement.getAttribute("pId"))
                ,PropertyCard.PropertyType.valueOf(pElement.getElementsByTagName("pType").item(0).getTextContent())
                ,null
                ,null);
        }
        else if ( PropertyCard.PropertyType.valueOf(eElement.getElementsByTagName("pType").item(0).getTextContent() ) == PropertyCard.PropertyType.RAILING )
        {
            lObjProperty = new LoadObjectProperty(Integer.parseInt(pElement.getAttribute("pId"))
                ,PropertyCard.PropertyType.valueOf(pElement.getElementsByTagName("pType").item(0).getTextContent())
                ,null
                ,null);
        }
        return lObjProperty;
    }
}
