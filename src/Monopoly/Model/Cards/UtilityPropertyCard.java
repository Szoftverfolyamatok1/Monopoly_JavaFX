package Monopoly.Model.Cards;

/**
 * User: Benjamin
 * Date: 2013.11.21.
 * Time: 0:55
 */
public class UtilityPropertyCard extends PropertyCard {
	private Integer valueOnePropertyOwned;
	private Integer valueTwoPropertiesOwned;

	public UtilityPropertyCard( Integer id
								,String name
								,Integer cost
								,Integer mortgage
								,Integer valueOnePropertyOwned
								,Integer valueTwoPropertiesOwned)
	{
		this.setId(id);
		this.setCardName(name);
		this.setCost(cost);
		this.setMortgageValue(mortgage);
		this.valueOnePropertyOwned = valueOnePropertyOwned;
		this.valueTwoPropertiesOwned = valueTwoPropertiesOwned;
		this.setPropertyType(PropertyType.UTILITY);
        this.setIsOwned(false);
	}

	public Integer getValueTwoPropertiesOwned() {
		return valueTwoPropertiesOwned;
	}

	public Integer getValueOnePropertyOwned() {
		return valueOnePropertyOwned;
	}
}
