package Monopoly.Controller;

import Monopoly.Model.Cards.ChanceCard;
import Monopoly.Model.Cards.CommunityChestCard;
import Monopoly.Model.Cards.PropertyCard;
import Monopoly.Model.XML.XMLParser;

import java.util.ArrayList;

/**
 * User: Benjamin
 * Date: 2013.11.19.
 * Time: 21:50
 */
public class CardController {
	//Parse from XML
	//return specific vector of cards
	//getCommunityChestCards()
	//getPropertyCards()
	//getChanceCards()
	private ArrayList<CommunityChestCard> communityChestCards;
	private ArrayList<PropertyCard> propertyCards;
	private ArrayList<ChanceCard> chanceCards;
	private XMLParser xmlParser;

	final String communityXMLPath = "XML/CommunityChestCards.xml";
	final String chanceXMLPath = "XML/ChanceCards.xml";
	final String propertyXMLPath = "XML/PropertyCards.xml";

	public CardController(){
		xmlParser = new XMLParser();
		communityChestCards = new ArrayList<CommunityChestCard>();
		propertyCards = new ArrayList<PropertyCard>();
		chanceCards = new ArrayList<ChanceCard>();
	}

	//get the Community cards form XML
	public ArrayList<CommunityChestCard> processCommunityCards(){
		return communityChestCards = xmlParser.handleCardsXML(communityXMLPath
														,"CommunityChest"
														,communityChestCards.getClass());
	}

	//get the Chance cards from XML
	public ArrayList<ChanceCard> processChanceCards(){
		return chanceCards = xmlParser.handleCardsXML(chanceXMLPath
												,"Chance"
												,chanceCards.getClass());
	}

	//get the Property cards from XML
	public ArrayList<PropertyCard> processPropertyCards(){
		return propertyCards = xmlParser.handleCardsXML(propertyXMLPath
												  ,"Property"
												  ,propertyCards.getClass());
	}
}
