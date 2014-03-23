package Monopoly.Model.Cards;

/**
 * User: Benjamin
 * Date: 2013.11.11.
 * Time: 13:32
 */
//Parent class for FieldCard and ChanceCard
public abstract class Card {
	public enum Type {	OUT_OF_JAIL
						,PAY
						,GET
						,ADVANCE_SPECIFIC
						,ADVANCE_SPECIFIC_TWICE
						,ADVANCE_SPECIFIC_RICH
						,ADVANCE_SPECIFIC_POOR
						,ADVANCE_NEGATIVE }

	private String cardName;
	private Type type;
	private String description;
	private Integer id;

	public String getCardName() {
		return cardName;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}
}
