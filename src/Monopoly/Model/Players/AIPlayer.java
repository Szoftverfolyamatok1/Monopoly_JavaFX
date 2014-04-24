package Monopoly.Model.Players;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

import Monopoly.Model.Cards.PlotPropertyCard;
import Monopoly.Model.Cards.PropertyCard;
import Monopoly.Model.Cards.RailingPropertyCard;
import Monopoly.Model.Cards.UtilityPropertyCard;
import static Monopoly.Logger.LoggerClass.doLog;

public class AIPlayer extends Player {

    private int decisionNo;
    private int entrepreneurship;
    private Random generator;

    public AIPlayer(String name) {
        decisionNo = 0;
        entrepreneurship = 0;
        generator = new Random(19580427);
        setPlayerName(name);
        initializePlayer();
        this.isInGame = true;
    }

    @Override
    public boolean step(PropertyCard propertyCard, Player player, ArrayList<PropertyCard> propertyCardList) {
        if (propertyCard == null) return false;

        if (propertyCard.getIsOwned() && player != null) {
            checkForOwnedProperty(propertyCard, player);
            return false;
        } else {
            if (propertyCard.getPropertyType() == PropertyCard.PropertyType.SIMPLE) {
                return steppedOnNotOwnedSimpleProperty((PlotPropertyCard) propertyCard, propertyCardList);
            } else if (propertyCard.getPropertyType() == PropertyCard.PropertyType.UTILITY) {
                return steppedOnNotOwnedUtilityProperty((UtilityPropertyCard) propertyCard, propertyCardList);
            } else if (propertyCard.getPropertyType() == PropertyCard.PropertyType.RAILING) {
                return steppedOnNotOwnedRailingProperty((RailingPropertyCard) propertyCard, propertyCardList);
            }
        }

        return false;
    }

    private int getOwnedPlotNoByColor(PlotPropertyCard plotPropertyCard, ArrayList<PropertyCard> propertyCardList) {
        int plotNo = 0;
        for (PropertyCard pc : propertyCardList) {
            if (pc != null && pc.getPropertyType() == PropertyCard.PropertyType.SIMPLE) {
                PlotPropertyCard ppc = (PlotPropertyCard) pc;
                if (ppc != null && ppc.getColourType() == plotPropertyCard.getColourType() && ppc.getIsOwned() == false) {
                    plotNo++;
                }
            }
        }
        return plotNo;
    }

    private int getOwnedPropertyNo(PropertyCard.PropertyType propertyType,ArrayList<PropertyCard> propertyCardList){
        int propertyNo=0;
        for (PropertyCard pc : propertyCardList) {
            if (pc != null && pc.getPropertyType()==propertyType && pc.getIsOwned()==false){
                propertyNo++;
            }
        }

        return propertyNo;
    }

    private boolean getNoByColorAndPlayer(PlotPropertyCard plotPropertyCard)
    {
        int no=0;
        for(PropertyCard pc: getPropertyList())
        {
            if (pc != null && pc.getPropertyType() == PropertyCard.PropertyType.SIMPLE) {
                PlotPropertyCard ppc = (PlotPropertyCard) pc;
                if (ppc.getColourType()==plotPropertyCard.getColourType())
                    no++;
            }
        }

        if(plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.BROWN ||
                plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.BLUE)
        {
            return no==1?true:false;
        }
        else
        {
            return (no==1 || no==2)?true:false;
        }
    }

    private void checkForOwnedProperty(PropertyCard propertyCard, Player player) {
        if (propertyCard.getPropertyType() == PropertyCard.PropertyType.SIMPLE) {
            steppedOnOwnedSimpleProperty((PlotPropertyCard) propertyCard, player);
        } else if (propertyCard.getPropertyType() == PropertyCard.PropertyType.RAILING) {
            payToPlayer(player, ((RailingPropertyCard) propertyCard).getValueOnePropertyOwned());
        } else if (propertyCard.getPropertyType() == PropertyCard.PropertyType.UTILITY) {
            payToPlayer(player, ((UtilityPropertyCard) propertyCard).getValueOnePropertyOwned());
        }
    }

    private boolean steppedOnNotOwnedRailingProperty(RailingPropertyCard propertyCard, ArrayList<PropertyCard> propertyCardList) {
        boolean buy;
        buy = makeRailwayDecision(propertyCard, propertyCardList);
        if (buy) {
            doLog(Level.INFO, "Az AI játékos megveszi a vasutat.");
            return true;
        } else {
            doLog(Level.INFO, "Az AI játékos nem veszi meg a vasutat.");
            return false;
        }
    }

    private boolean steppedOnNotOwnedUtilityProperty(UtilityPropertyCard propertyCard, ArrayList<PropertyCard> propertyCardList) {
        boolean buy;
        buy = makeUtilityDecision(propertyCard, propertyCardList);
        if (buy) {
            doLog(Level.INFO, "Az AI játékos megveszi a művet.");
            return true;
        } else {
            doLog(Level.INFO, "Az AI játékos nem veszi meg a művet.");
            return false;
        }
    }

    private boolean steppedOnNotOwnedSimpleProperty(PlotPropertyCard propertyCard, ArrayList<PropertyCard> propertyCardList) {
        boolean buy;
        buy = makePlotDecision(propertyCard, propertyCardList);
        if (buy) {
            doLog(Level.INFO, "Az AI játékos megveszi a telket.");
            incrementBoughtPropertyTypeCount(propertyCard);

            return true;
        } else {
            doLog(Level.INFO, "Az AI játékos nem veszi meg a telket.");
            return false;
        }
    }

    private void incrementBoughtPropertyTypeCount(PlotPropertyCard plotPropertyCard) {
        if (plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.BLUE) {
            bluePropertyNo++;
        } else if (plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.LIGHT_BLUE) {
            lightBluePropertyNo++;
        } else if (plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.ORANGE) {
            orangePropertyNo++;
        } else if (plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.PURPLE) {
            purplePropertyNo++;
        } else if (plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.GREEN) {
            greenPropertyNo++;
        } else if (plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.BROWN) {
            brownPropertyNo++;
        } else if (plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.RED) {
            redPropertyNo++;
        } else if (plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.YELLOW) {
            yellowPropertyNo++;
        }
    }

    private void steppedOnOwnedSimpleProperty(PlotPropertyCard propertyCard, Player player) {
        if (propertyCard.getHouseNo() == 0) {
            payToPlayer(player, propertyCard.getRentalValueNoHouses());
        }

        if (propertyCard.getHouseNo() > 0 && !propertyCard.getHasHotel()) {

            if (propertyCard.getHouseNo() == 1) {
                payToPlayer(player, propertyCard.getRentalValueOneHouse());
            } else if (propertyCard.getHouseNo() == 2) {
                payToPlayer(player, propertyCard.getRentalValueTwoHouses());
            } else if (propertyCard.getHouseNo() == 3) {
                payToPlayer(player, propertyCard.getRentalValueThreeHouses());
            } else {
                payToPlayer(player, propertyCard.getRentalValueFourHouses());
            }
        }

        if (propertyCard.getHasHotel()) {
            payToPlayer(player, propertyCard.getRentalValueHotel());
        }
    }

    private int isPropertyOwned(PlotPropertyCard plotPropertyCard, ArrayList<PropertyCard> propertyCardList)
    {
        int plotNo=getOwnedPlotNoByColor(plotPropertyCard, propertyCardList);

        if(plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.RED ||
                plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.YELLOW ||
                plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.BLUE ||
                plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.GREEN)
        {
            if(plotNo==0)
                return 2;
            else
                return 0;
        }
        else
        {
            if (plotNo == 0)
                return 1;
            else
                return -1;
        }


    }

    public boolean makeJailDecision() {

        if (playerCardList.size() != 0) {
            playerCardList.remove(playerCardList.size() - 1);
            return false;
        } else {
            if (playerCash > 250) {
                playerCash -= 50;
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean makePlotDecision(PlotPropertyCard plotPropertyCard, ArrayList<PropertyCard> propertyCardList)
    {
        decisionNo=0;
        entrepreneurship=generator.nextInt(2);

        if(!plotPropertyCard.getIsOwned() && playerCash>plotPropertyCard.getCost())
        {
            decisionNo+=isPropertyOwned(plotPropertyCard, propertyCardList);

            if(playerCash>plotPropertyCard.getCost()+plotPropertyCard.getCost()/2)
            {
                decisionNo++;
                if(getNoByColorAndPlayer(plotPropertyCard))
                    return true;
            }
            if(playerLocation+10<39 && playerCash<plotPropertyCard.getCost()+plotPropertyCard.getCost()/2)
            {
                decisionNo++;
            }
            if(plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.BLUE ||
                    plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.RED ||
                    plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.YELLOW ||
                    plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.GREEN )
            {
                decisionNo+=2;
            }
            if(plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.PURPLE ||
                    plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.LIGHT_BLUE ||
                    plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.ORANGE ||
                    plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.BROWN)
            {

                decisionNo+=1;
            }
            if(4*turnNumber>20 && playerCash>plotPropertyCard.getCost())
            {
                decisionNo++;
            }
            if(playerCash>600)
            {
                decisionNo+=2;
            }
            if(entrepreneurship==1)
            {
                decisionNo+=2;
            }

        }

        return decisionNo > 5;
    }

    private Boolean makeRailwayDecision(RailingPropertyCard railingPropertyCard, ArrayList<PropertyCard> propertyCardList)
    {
        Integer railingCount=0;
        Integer propertyCount=0;
        decisionNo=0;
        entrepreneurship=generator.nextInt(2);
        if(!railingPropertyCard.getIsOwned() && playerCash>railingPropertyCard.getCost())
        {
            if(getOwnedPropertyNo(PropertyCard.PropertyType.RAILING, propertyCardList)==0)
                decisionNo++;
            if(playerCash>railingPropertyCard.getCost()+railingPropertyCard.getCost()/2)
            {
                decisionNo++;
            }
            if(playerCash>railingPropertyCard.getCost()*3)
            {
                decisionNo+=2;
            }
            for(PropertyCard propertyCard: playerPropertyList)
            {
                if(propertyCard.getPropertyType()== PropertyCard.PropertyType.RAILING)
                {
                    railingCount++;
                }
                propertyCount++;
            }
            if(railingCount==1)
            {
                decisionNo++;
            }
            if(railingCount==2)
            {
                decisionNo+=2;
            }
            if(4*turnNumber>20 && propertyCount<5 && playerCash>=railingPropertyCard.getCost())
            {
                decisionNo++;
            }
            if(entrepreneurship==1)
            {
                decisionNo+=3;
            }

        }

        Integer randomDecision=generator.nextInt(2);
        return decisionNo > 2 || randomDecision == 1;
    }

    private Boolean makeUtilityDecision(UtilityPropertyCard utilityPropertyCard, ArrayList<PropertyCard> propertyCardList)
    {
        Integer utilityCount=0;
        Integer propertyCount=0;
        decisionNo=0;
        entrepreneurship=generator.nextInt(2);
        if(!utilityPropertyCard.getIsOwned() && playerCash>utilityPropertyCard.getCost())
        {
            if(getOwnedPropertyNo(PropertyCard.PropertyType.UTILITY, propertyCardList)==0)
                decisionNo++;
            if(playerCash>utilityPropertyCard.getCost()+utilityPropertyCard.getCost()/2)
            {
                decisionNo++;
            }
            for(PropertyCard propertyCard: playerPropertyList)
            {
                if(propertyCard.getPropertyType()== PropertyCard.PropertyType.UTILITY)
                {
                    utilityCount++;
                }
                propertyCount++;
            }
            if(utilityCount==1)
            {
                decisionNo++;
            }
            if(4*turnNumber>20 && propertyCount<5 && playerCash>=utilityPropertyCard.getCost())
            {
                decisionNo++;
            }
            if(entrepreneurship==1)
            {
                decisionNo+=3;
            }

        }

        Integer randomDecision=generator.nextInt(2);
        return decisionNo >= 4 || randomDecision == 1 && decisionNo == 3;
    }

    public PlotPropertyCard.Colour_Type makeHouseDecision(ArrayList<PropertyCard> propertyCardList)
    {
        decisionNo=0;
        entrepreneurship=generator.nextInt(2);

        for(int i=0;i<6;i++)
        {
            if(propertyCardList.get((playerLocation+i)%propertyCardList.size()).getPropertyType()== PropertyCard.PropertyType.SIMPLE)
            {
                PlotPropertyCard ppc=(PlotPropertyCard) propertyCardList.get((playerLocation+i)%propertyCardList.size());
                if(!ppc.getHasHouse())
                    decisionNo++;
                else
                    decisionNo--;
            }
        }

        if(entrepreneurship==1 && playerCash>=200)
        {
            decisionNo+=3;
        }
        if(playerLocation+10<39 && playerCash>=700)
        {
            decisionNo+=2;
        }
        if(playerCash>=1000)
        {
            decisionNo+=2;
        }

        if(decisionNo>=2)
        {

            if(!getHasHotel(propertyCardList, PlotPropertyCard.Colour_Type.BROWN) && brownPropertyNo==2 && playerCash>=250 )
            {
                return PlotPropertyCard.Colour_Type.BROWN;
            }
            if(!getHasHotel(propertyCardList, PlotPropertyCard.Colour_Type.LIGHT_BLUE) && lightBluePropertyNo==3 && playerCash>=300 )
            {
                return  PlotPropertyCard.Colour_Type.LIGHT_BLUE;
            }
            if(!getHasHotel(propertyCardList, PlotPropertyCard.Colour_Type.PURPLE) && purplePropertyNo==3 && playerCash>=400 )
            {
                return PlotPropertyCard.Colour_Type.PURPLE;
            }
            if(!getHasHotel(propertyCardList, PlotPropertyCard.Colour_Type.ORANGE) && orangePropertyNo==3 && playerCash>=420)
            {
                return PlotPropertyCard.Colour_Type.ORANGE;
            }
            if(!getHasHotel(propertyCardList, PlotPropertyCard.Colour_Type.YELLOW) && yellowPropertyNo==3 && playerCash>=400 )
            {
                return PlotPropertyCard.Colour_Type.YELLOW;
            }
            if(!getHasHotel(propertyCardList, PlotPropertyCard.Colour_Type.RED) && redPropertyNo==3 && playerCash>=550 )
            {
                return PlotPropertyCard.Colour_Type.RED;
            }
            if(!getHasHotel(propertyCardList, PlotPropertyCard.Colour_Type.BLUE) && bluePropertyNo==2 && playerCash>=400 )
            {
                return PlotPropertyCard.Colour_Type.BLUE;
            }
            if(!getHasHotel(propertyCardList, PlotPropertyCard.Colour_Type.GREEN) && greenPropertyNo==3 && playerCash>=700 )
            {
                return PlotPropertyCard.Colour_Type.GREEN;
            }
        }
        return null;

    }

    public PlotPropertyCard.Colour_Type makeHotelDecision()
    {
        decisionNo=0;
        entrepreneurship=generator.nextInt(2);
        if(entrepreneurship==1 && playerCash>=200)
        {
            decisionNo+=3;
        }
        if(playerLocation+10<39 && playerCash>=700)
        {
            decisionNo+=2;
        }
        if(playerCash>=1000)
        {
            decisionNo+=2;
        }

        if(decisionNo>=2)
        {
            for(PropertyCard pc:playerPropertyList)
            {
                if(pc.getPropertyType()==PropertyCard.PropertyType.SIMPLE)
                {
                    if(((PlotPropertyCard)pc).getHouseNo()==4 && !((PlotPropertyCard) pc).getHasHotel())
                    {
                        if(playerCash+200>=((PlotPropertyCard)pc).getHotelCost())
                        {
                            return  ((PlotPropertyCard)pc).getColourType();
                        }
                    }
                }
            }
        }
        return null;
    }

    public PropertyCard makePawnDecision()
    {
        for(PropertyCard pc: playerPropertyList) {
            if (pc != null && pc.getPropertyType() == PropertyCard.PropertyType.UTILITY) {
                return pc;
            }
        }
        for(PropertyCard pc: playerPropertyList) {
            if (pc != null && pc.getPropertyType() == PropertyCard.PropertyType.RAILING) {
                return pc;
            }
        }
        for(PropertyCard pc: playerPropertyList) {
            if (pc != null && pc.getPropertyType() == PropertyCard.PropertyType.SIMPLE) {
                PlotPropertyCard plotPropertyCard = (PlotPropertyCard) pc;
                if (plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.PURPLE ||
                        plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.LIGHT_BLUE ||
                        plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.ORANGE ||
                        plotPropertyCard.getColourType() == PlotPropertyCard.Colour_Type.BROWN) {
                    return pc;
                }
            }
        }
        for(PropertyCard pc: playerPropertyList) {
            if(pc!=null && pc.getPropertyType() == PropertyCard.PropertyType.SIMPLE) {
                PlotPropertyCard plotPropertyCard = (PlotPropertyCard) pc;
                if(plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.BLUE ||
                   plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.RED ||
                   plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.YELLOW ||
                   plotPropertyCard.getColourType()== PlotPropertyCard.Colour_Type.GREEN )
                {
                    return pc;
                }
            }

        }
        return null;
    }

    private boolean getHasHotel(ArrayList<PropertyCard> propertyCardList, PlotPropertyCard.Colour_Type colourType)
    {
            for(PropertyCard pc: propertyCardList) {
                if (pc != null && pc.getPropertyType() == PropertyCard.PropertyType.SIMPLE) {
                    PlotPropertyCard ppc = (PlotPropertyCard) pc;
                    if (((PlotPropertyCard) pc).getColourType() == colourType && ((PlotPropertyCard) pc).getHasHotel())
                        return true;

                }
            }
        return false;
    }
}
