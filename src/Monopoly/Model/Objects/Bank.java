package Monopoly.Model.Objects;

import Monopoly.Controller.CardController;
import Monopoly.Model.Cards.*;
import Monopoly.Model.LoadObjects.LoadObjectBankProperty;
import Monopoly.Model.Objects.BoardElement;
import Monopoly.Model.Players.Player;
import com.sun.deploy.uitoolkit.impl.fx.ui.FXMessageDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;

import static Monopoly.Logger.LoggerClass.doLog;

/**
 * User: Benjamin
 * Date: 2013.11.11.
 * Time: 13:31
 */
public class Bank {

	private int bankMoney;
	final int MAXIMUM_HOUSE_NUMBER = 32;
	final int MAXIMUM_HOTEL_NUMBER = 12;
	public ArrayList<PropertyCard> propertyCardList;
	private ArrayList<ChanceCard> chanceCardList;
	private ArrayList<CommunityChestCard> communityChestCardList;

	public Bank(){
		CardController cc = new CardController();
		propertyCardList = cc.processPropertyCards();
		communityChestCardList = cc.processCommunityCards();
		chanceCardList = cc.processChanceCards();

		this.bankMoney = 100000;
	}

	public void load(Integer bankMoney
				,ArrayList<LoadObjectBankProperty> propertyCardList)
	{
		this.bankMoney = bankMoney;
		for ( int i = 0; i < this.propertyCardList.size(); ++i )
		{
			this.propertyCardList.get(i).setIsOwned(propertyCardList.get(i).getOwned());
		}
	}

	public void giveMoneyToPlayer(Player player, int amount) {
		player.setPlayerCash(amount);
		this.bankMoney -= amount;
	}


	public void givePropertyToPlayer(Player player, String propertyName){
        doLog(Level.INFO, "A játékos aki meg akarja venni: " + player.getPlayerName() + ", ezt a telket: " + propertyName);

        //for God's sake, don't refactor it to foreach! it causes interference and shit
        for(int i = 0;i<propertyCardList.size();i++ )
        {
			if ( propertyName.contentEquals(propertyCardList.get(i).getCardName()) )
			{
				if ( propertyCardList.get(i).getPropertyType() == PropertyCard.PropertyType.SIMPLE )
				{
                    buySimpleProperty(player, i);
                }
				else if ( propertyCardList.get(i).getPropertyType() == PropertyCard.PropertyType.UTILITY )
				{
                    buyUtilityProperty(player, i);
                }
				else if ( propertyCardList.get(i).getPropertyType() == PropertyCard.PropertyType.RAILING )
				{
                    buyRailingProperty(player, i);
                }
            return;
			}
		}
	}


    private void buyRailingProperty(Player player, int i) {
        if ( player.getPlayerCash() - propertyCardList.get(i).getCost() > 0 )
        {
            player.setPlayerCash(-propertyCardList.get(i).getCost());
            RailingPropertyCard rpc = (RailingPropertyCard)propertyCardList.get(i);
            propertyCardList.get(i).setIsOwned(true);
            player.addPropertyToPlayer(rpc);
        }
    }

    private void buyUtilityProperty(Player player, int i) {
        if ( player.getPlayerCash() - propertyCardList.get(i).getCost() > 0 )
        {
            player.setPlayerCash(-propertyCardList.get(i).getCost());
            UtilityPropertyCard upc = (UtilityPropertyCard)propertyCardList.get(i);
            propertyCardList.get(i).setIsOwned(true);
            player.addPropertyToPlayer(upc);
        }
    }

    private void buySimpleProperty(Player player, int i) {
        if ( player.getPlayerCash() - propertyCardList.get(i).getCost() > 0 )
        {
            player.setPlayerCash(-propertyCardList.get(i).getCost());
            PlotPropertyCard ppc = (PlotPropertyCard)propertyCardList.get(i);
            propertyCardList.get(i).setIsOwned(true);
            player.addPropertyToPlayer(ppc);
        }
    }

    public int getBankMoney() {
		return bankMoney;
	}

    public void giveHouseToPlayer(Player player, PlotPropertyCard plotPropertyCard,int no)
    {
        if( player.getPlayerCash() >= plotPropertyCard.getHouseCost()*no)
        {
            bankMoney+=plotPropertyCard.getHouseCost()*no;
            player.setPlayerCash(-(plotPropertyCard.getHouseCost()*no));
        }
    }

	public PropertyCard getPropertyByName(String name)
	{
		for(PropertyCard pc : propertyCardList)
		{
			if (pc.getCardName().equals(name))
			{
				return pc;
			}
		}
		return null;
	}

    public ArrayList<PlotPropertyCard.Colour_Type> getPlayerBuildableColors(Player player) {
        ArrayList<PlotPropertyCard.Colour_Type> returnList = new ArrayList<PlotPropertyCard.Colour_Type>();

        Collections.addAll(returnList, PlotPropertyCard.Colour_Type.values());

        //return returnList;  //Ha tesztelni akarjátok akkor ezt kommentezzétek vissza és akkor megjelenik az összes szín
        for (PropertyCard aPropertyCardList : propertyCardList) {
            if (aPropertyCardList.getPropertyType() == PropertyCard.PropertyType.SIMPLE) {
                PlotPropertyCard currentCard = (PlotPropertyCard) aPropertyCardList;
                if (!player.getDoIHaveThisCard(aPropertyCardList)) {
                    for (int j = 0; j < returnList.size(); j++) {
                        if (currentCard.getColourType() == returnList.get(j)) {
                            returnList.remove(j);
                            j--;
                        }
                    }
                }
            }
        }
        return returnList;
    }

    public void getMoneyFromPlayer(Player player,int amount)
	{
		bankMoney+=amount;
		player.setPlayerCash(-amount);
	}

    public void giveHotelToPlayer(Player player, PlotPropertyCard propertyCard, Integer no)
    {
        player.setPlayerCash(-propertyCard.getHotelCost()*no);
        bankMoney+=propertyCard.getHotelCost()*no;
    }
}
