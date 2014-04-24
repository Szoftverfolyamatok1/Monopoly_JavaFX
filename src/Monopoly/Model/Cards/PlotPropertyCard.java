package Monopoly.Model.Cards;

/**
 * User: Benjamin
 * Date: 2013.11.21.
 * Time: 0:54
 */
public class PlotPropertyCard extends PropertyCard {
    private Integer rentalValueNoHouses;
    private Integer rentalValueOneHouse;
    private Integer rentalValueTwoHouses;
    private Integer rentalValueThreeHouses;
    private Integer rentalValueFourHouses;
    private Integer rentalValueHotel;

    private Integer houseCost;
    private Integer hotelCost;
    private Boolean hasHouse;
    private Boolean hasHotel;
    private Integer houseNo;

    public enum Colour_Type {
        BROWN
        ,RED
        ,LIGHT_BLUE
        ,BLUE
        ,YELLOW
        ,PURPLE
        ,GREEN
        ,ORANGE
    }

    private Colour_Type colourType;

    public PlotPropertyCard(Integer id
            ,String name
            ,Colour_Type cType
            ,Integer cost
            ,Integer rentalZero
            ,Integer rentalOne
            ,Integer rentalTwo
            ,Integer rentalThree
            ,Integer rentalFour
            ,Integer rentalHotel
            ,Integer houseCost
            ,Integer hotelCost
            ,Integer mortgage)
    {
        this.setId(id);
        this.setCardName(name);
        this.colourType = cType;
        this.setCost(cost);
        this.rentalValueNoHouses = rentalZero;
        this.rentalValueOneHouse = rentalOne;
        this.rentalValueTwoHouses = rentalTwo;
        this.rentalValueThreeHouses = rentalThree;
        this.rentalValueFourHouses = rentalFour;
        this.rentalValueHotel = rentalHotel;
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
        this.setMortgageValue(mortgage);
        this.setPropertyType(PropertyType.SIMPLE);

        this.hasHouse=false;
        this.hasHotel=false;
        this.houseNo=0;
//        this.isOwned=false;
        this.setIsOwned(false);
    }

    public Integer getRentalValueNoHouses() {
        return rentalValueNoHouses;
    }

    public Integer getRentalValueOneHouse() {
        return rentalValueOneHouse;
    }

    public Integer getRentalValueTwoHouses() {
        return rentalValueTwoHouses;
    }

    public Integer getRentalValueThreeHouses() {
        return rentalValueThreeHouses;
    }

    public Integer getRentalValueFourHouses() {
        return rentalValueFourHouses;
    }

    public Integer getRentalValueHotel() {
        return rentalValueHotel;
    }

    public Integer getHouseCost() {
        return houseCost;
    }

    public Integer getHotelCost() {
        return hotelCost;
    }

    public Boolean getHasHouse() {
        return hasHouse;
    }

    public Boolean getHasHotel() {
        return hasHotel;
    }

    public Integer getHouseNo() {
        return houseNo;
    }

    public Colour_Type getColourType()     //G.
    {
        return colourType;
    }

    public void increaseHouseNo()
    {
        houseNo++;
    }
    public void decreaseHouseNo()
    {
        houseNo--;
    }
	public String getColorTypeString(){
		switch (colourType)
		{
			case BROWN: return "brown";
			case RED: return "red";
			case LIGHT_BLUE: return "lightblue";
			case BLUE: return "blue";
			case YELLOW: return "yellow";
			case PURPLE: return "purple";
			case GREEN: return "green";
			case ORANGE: return "orange";
			default:
				break;
		}
		return "";
	}

    public void setHasHotel(Boolean b)
    {
        hasHotel=b;
    }
}
