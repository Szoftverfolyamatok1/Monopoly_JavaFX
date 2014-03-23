package Monopoly.Model.LoadObjects;

import java.util.ArrayList;

/**
 * User: Benjamin
 * Date: 2013.12.03.
 * Time: 0:00
 */
public class LoadObjectPlayer {
	private String name;
	private Integer cash;
	private Integer location;
	private ArrayList<LoadObjectProperty> propertyList;
	private ArrayList<LoadObjectCard> cardList;

	public LoadObjectPlayer(String name
							,Integer cash
							,Integer location
							,ArrayList<LoadObjectProperty> propertyList
							,ArrayList<LoadObjectCard> cardList)
	{
		this.name = name;
		this.cash = cash;
		this.location = location;
		this.propertyList = propertyList;
		this.cardList = cardList;
	}

	public String getName() {
		return name;
	}

	public Integer getCash() {
		return cash;
	}

	public Integer getLocation() {
		return location;
	}

	public ArrayList<LoadObjectProperty> getPropertyList() {
		return propertyList;
	}

	public ArrayList<LoadObjectCard> getCardList() {
		return cardList;
	}
}
