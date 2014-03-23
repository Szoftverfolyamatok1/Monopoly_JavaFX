package Monopoly.Model.Cards;

/**
 * User: Benjamin
 * Date: 2013.11.19.
 * Time: 21:48
 */
//Maybe abstraction will be needed to pay in/off cards
public class ChanceCard extends Card {
	//represents the chance cards
	//has name, type, description, amount
	//Types:
	/*
	-Advance
	-Pay in
		-pay for specific person
		-pay for everyone
	-Pay off ( receive money )
	-Get out of jail
	 */
	private String amount;

	public ChanceCard(Integer id, String name, String description, Type type, String nullableAmount){
		setId(id);
		setCardName(name);
		setDescription(description);
		setType(type);
		if (nullableAmount != null)
			this.amount = nullableAmount;
	}

	public String getAmount() {
		return amount;
	}
}
