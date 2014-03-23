package Monopoly.Model.Cards;

/**
 * User: Benjamin
 * Date: 2013.11.19.
 * Time: 21:48
 */
//Maybe abstraction will be needed to pay in/off cards
public class CommunityChestCard extends Card {
	//Represents the community chest cards
	//has name, type, description
	//Types:
	/*
	-Get money
		-from everyone
	-Pay money
    -Get out of jail
    -advance
    	-do not collect GO money
   		-simple advance
	 */
	private String amount;

	public CommunityChestCard(Integer id, String name, String description, Type type, String nullableAmount){
		this.setId(id);
		this.setCardName(name);
		this.setDescription(description);
		this.setType(type);
		if ( nullableAmount != null)
			this.amount = nullableAmount;
	}

	public String getAmount() {
		return amount;
	}
}
