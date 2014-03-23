package Monopoly.Model.LoadObjects;

import Monopoly.Model.Cards.PropertyCard;

/**
 * User: Benjamin
 * Date: 2013.12.02.
 * Time: 23:59
 */
public class LoadObjectProperty {
	private Integer pId;
	private Integer houseNumber;
	private Boolean hasHotel;
	private PropertyCard.PropertyType pType;

	public LoadObjectProperty(Integer pId
							  ,PropertyCard.PropertyType pType
							  ,Integer houseNumber
							  ,Boolean hasHotel)
	{
		this.pId = pId;
		this.pType = pType;
		this.houseNumber = houseNumber;
		this.hasHotel = hasHotel;
	}

	public Integer getpId() {
		return pId;
	}

	public Integer getHouseNumber() {
		return houseNumber;
	}

	public Boolean getHasHotel() {
		return hasHotel;
	}
}
