package Monopoly.Model.Players;

import Monopoly.Model.Cards.PropertyCard;

import java.util.ArrayList;
import java.util.logging.Level;

import static Monopoly.Logger.LoggerClass.doLog;

public class HumanPlayer extends Player {

	public HumanPlayer(String name)
	{
		setPlayerName(name);
		initializePlayer();
		this.isInGame = true;
	}

	@Override
	public boolean step(PropertyCard propertyCard,Player player, ArrayList<PropertyCard> propertyCardList) {
        doLog(Level.INFO, "Az emberi játékos lépett.");
        return false;
	}
}
