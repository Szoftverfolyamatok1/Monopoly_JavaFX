package Monopoly.Model.Cards;

/**
 * User: Benjamin
 * Date: 2013.11.24.
 * Time: 0:45
 */
public class RailingPropertyCard extends PropertyCard {
	private Integer valueOnePropertyOwned;
	private Integer valueTwoPropertiesOwned;
	private Integer valueThreePropertiesOwned;
	private Integer valueFourPropertiesOwned;

	public RailingPropertyCard( Integer id
								,String name
								,Integer cost
								,Integer valueOnePropertyOwned
								,Integer valueTwoPropertiesOwned
							    ,Integer valueThreePropertiesOwned
							   	,Integer valueFourPropertiesOwned
								,Integer mortgageValue)
	{
		this.setId(id);
		this.setCardName(name);
		this.setCost(cost);
		this.valueOnePropertyOwned = valueOnePropertyOwned;
		this.valueTwoPropertiesOwned = valueTwoPropertiesOwned;
		this.valueThreePropertiesOwned = valueThreePropertiesOwned;
		this.valueFourPropertiesOwned = valueFourPropertiesOwned;
		this.setMortgageValue(mortgageValue);
		this.setPropertyType(PropertyType.RAILING);
        this.setIsOwned(false);
	}

	public Integer getValueOnePropertyOwned() {
		return valueOnePropertyOwned;
	}

	public Integer getValueTwoPropertiesOwned() {
		return valueTwoPropertiesOwned;
	}

	public Integer getValueThreePropertiesOwned() {
		return valueThreePropertiesOwned;
	}

	public Integer getValueFourPropertiesOwned() {
		return valueFourPropertiesOwned;
	}
}
