package Monopoly.Controller;

import Monopoly.Model.Cards.*;
import Monopoly.Model.LoadObjects.LoadObject;
import Monopoly.Model.LoadObjects.LoadObjectProperty;
import Monopoly.Model.Objects.Bank;
import Monopoly.Model.Objects.BoardElement;
import Monopoly.Model.Players.AIPlayer;
import Monopoly.Model.Players.HumanPlayer;
import Monopoly.Model.Players.Player;
import Monopoly.Model.XML.XMLParser;
import Monopoly.Model.XML.XMLWriter;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import static Monopoly.Logger.LoggerClass.doLog;

public class GameController {
	@FXML public Pane main;

	public Bank bank;
	private BoardElement boardElement;

	public ArrayList<Player> playerList;
    private ArrayList<PropertyCard> propertyCardList;
    private ArrayList<ChanceCard> chanceCardList;
	private ArrayList<CommunityChestCard> communityChestCardList;
	private int playerNumber;
    private String playerName;
    private String lastChanceCard,lastCommunityCard;
	private int playerCounter;
    private Integer playerWhoThrown;
    private PropertyCard pawnPc;
	public void initialize()
    {
        boardElement = new BoardElement();
        initializeBank();
        playerWhoThrown = -1;
        playerNumber = 1;
        playerName="DefaultName";
        CardController cc = new CardController();
        propertyCardList = cc.processPropertyCards();
        chanceCardList = cc.processChanceCards();
        communityChestCardList = cc.processCommunityCards();
        playerCounter = 0;
	}

	private void initializeBank()
    {
		bank = new Bank();
	}

	private void initializePlayers()
	{
        doLog(Level.INFO,"Játékosok inícializálása");
		playerCounter = 0;
		playerList = new ArrayList<Player>();
		HumanPlayer humanPlayer = new HumanPlayer(playerName);
        bank.giveMoneyToPlayer(humanPlayer,1000);
		playerList.add(humanPlayer);
        System.out.print(playerName);
		for ( int i = 1; i <= playerNumber; ++i )
		{
			AIPlayer aiPlayer = new AIPlayer("Computer Player " + i);

            bank.giveMoneyToPlayer(aiPlayer,1000);
			playerList.add(aiPlayer);
            doLog(Level.INFO, "Computer player " + i + " elkészült.");
		}
        System.out.print("\n");
	}

	//If we are going to overflow from playerList, set the counter to 0
	private int getPlayerCounter_CheckForOrderValidity() {
		if ( playerList.size() <= playerCounter)
			playerCounter = 0;
		return playerCounter;
	}

    public Integer currentPlayer(){
        return playerCounter;
    }

    public String getPlayerName(Integer p){
        return playerList.get(p).getPlayerName();
    }

    public Integer getPlayerCash(Integer p){
        return playerList.get(p).getPlayerCash();
    }

//	//sets the next player
	private void nextPlayer(){
		++playerCounter;
	}


	public void newGame(){
        doLog(Level.INFO, "Új játék kezdődött.");
        initializePlayers();
	}

	public void saveGame(){
        doLog(Level.INFO, "Játék mentése.");
		XMLWriter writer = new XMLWriter();
		writer.writeToXML(playerList, bank);
        doLog(Level.INFO, "A játék elmentve.");
	}

    public void exitGame(){
        doLog(Level.INFO, "Vége a játéknak.");
    }

	public void saveSettings(String name,Integer number){
        doLog(Level.INFO, "A beállítások elmentve, játékos neve: " +name+ ", AI darabszám: " +number);
		playerNumber = number;
        playerName=name;
	}

	public String getField(String id){
		return boardElement.getElementById(Integer.parseInt(id));
	}

    public Integer getCurrentPlace(Integer player){
        doLog(Level.INFO, "Player: " + player);
        return playerList.get(player).getCurrentPlace();
    }

    //VIVI
    //returns the Pair of the thrown numbers as well, so that we can display the dices
	public Pair<Boolean,Pair<Integer,Integer>> rollTheDice(){
        playerWhoThrown = currentPlayer();
        doLog(Level.INFO, "A(z)" + (playerWhoThrown+1) + ". játékos lépett" );
		if ( !checkGameOver() )
		{
			if(currentPlayer()!=0)
			{
				buyHouseAIPlayer();
                buyHotelAIPlayer();

			}

			Integer prevPlace=playerList.get(getPlayerCounter_CheckForOrderValidity()).getCurrentPlace();
            Pair<Integer,Integer> throwResult = playerList.get(getPlayerCounter_CheckForOrderValidity()).rollTheDice();

			String currentElement=  boardElement.getElementById(playerList.get(getPlayerCounter_CheckForOrderValidity()).getCurrentPlace());
			if (currentElement.substring(0,currentElement.length()-1).equals("Community_Chest_"))
			{
                lastCommunityCard = processCommunityChestCard(playerList.get(getPlayerCounter_CheckForOrderValidity()));
			}
			if(currentElement.substring(0,currentElement.length()-1).equals("Chance_"))
			{
                lastChanceCard = processChanceCard(playerList.get(getPlayerCounter_CheckForOrderValidity()));
			}

			checkIfHasToPayToBank(currentElement);

			Boolean buy=false;
			if ( boardElement.getElementById(playerList.get(getPlayerCounter_CheckForOrderValidity()).getCurrentPlace()).equals("GO_TO_JAIL") )
			{
				playerList.get(getPlayerCounter_CheckForOrderValidity()).setIsPlayerInJail(true);
				playerList.get(getPlayerCounter_CheckForOrderValidity()).setCurrentPlace(boardElement.getElementByName("JAIL"));
				if(currentPlayer()!=0)
				{
					AIPlayer aiPlayer=(AIPlayer)playerList.get(getPlayerCounter_CheckForOrderValidity());
					if(aiPlayer!=null)
					{
						Boolean getOutOfJail= aiPlayer.makeJailDecision();
						if(getOutOfJail)
						{
							aiPlayer.setIsPlayerInJail(false);
						}
					}
				}
				nextPlayer();
				getPlayerCounter_CheckForOrderValidity();
				return new Pair<Boolean,Pair<Integer,Integer>>(false,throwResult);
			}

			if( prevPlace > playerList.get(getPlayerCounter_CheckForOrderValidity()).getCurrentPlace()
				&& !playerList.get(getPlayerCounter_CheckForOrderValidity()).getIsPlayerInJail())
			{
				playerList.get(getPlayerCounter_CheckForOrderValidity()).setPlayerCash(200);
			}
			String name = boardElement.getElementById(playerList.get(getPlayerCounter_CheckForOrderValidity()).getCurrentPlace());
			PropertyCard pc=  getPropertyCardByName(name);

			if ( !playerList.get(getPlayerCounter_CheckForOrderValidity()).getIsCurrentThrowDoubled()
				|| playerList.get(getPlayerCounter_CheckForOrderValidity()).getIsPlayerInJail()) {

				if(currentPlayer()==0)
				{
					checkPayToPlayer(playerList.get(getPlayerCounter_CheckForOrderValidity()),pc);
				}

				buy=playerList.get(getPlayerCounter_CheckForOrderValidity()).step(pc,hasPropertyCard(pc), getPropertyCardList());
				buyProperty(name, buy);
				checkIfPlayerLost(currentPlayer());
				nextPlayer();
				getPlayerCounter_CheckForOrderValidity();

				return new Pair<Boolean,Pair<Integer,Integer>>(buy,throwResult);
			}

			buy= playerList.get(getPlayerCounter_CheckForOrderValidity()).step(pc,hasPropertyCard(pc),getPropertyCardList());
			buyProperty(name, buy);
			return new Pair<Boolean,Pair<Integer,Integer>>(buy,throwResult);
		}

		//fel kell dobni egy ablakot, hogy vége a játéknak
		showGameOverWindow();
		return null;
	}

	private void checkIfPlayerLost(Integer currentPlayer) {
		while ( playerList.get(currentPlayer).getPlayerCash() <= 0 && playerList.get(currentPlayer).isPlayerHasProperty())
		{
            if (currentPlayer != 0) //AI zálog része
            {
                PropertyCard pc =((AIPlayer) playerList.get(currentPlayer)).makePawnDecision();
                if (pc.getPropertyType() == PropertyCard.PropertyType.SIMPLE)
                {
                    if (((PlotPropertyCard)pc).getHasHotel())
                        sellHotelPlayer(((PlotPropertyCard) pc).getColourType(), false);
                    while (((PlotPropertyCard)pc).getHouseNo() > 0 && playerList.get(currentPlayer).getPlayerCash() <= 0)
                    {
                        sellHouseFromPlayer(((PlotPropertyCard) pc).getColourType(), false);

                    }
                }
                if (playerList.get(currentPlayer).getPlayerCash() <= 0)
                {
                    playerList.get(currentPlayer).setPlayerCash(+(pc.getMortgageValue()));
                    playerList.get(currentPlayer).movePropertyToPawn(pc);
                }
            }
            else                    //Human játékos AI része
            {
                ArrayList<PropertyCard> playersProperties = getPlayerPropertyCards(currentPlayer);
                final Stage st = new Stage();
                Pane propertiesWindowPane = new Pane();
                propertiesWindowPane.setPrefSize(800,700);
                st.setScene(new Scene(propertiesWindowPane,800,700));
                st.setTitle(getPlayerName(currentPlayer) + " Területei:      Tartozás:" + playerList.get(currentPlayer).getPlayerCash());
                st.initModality(Modality.APPLICATION_MODAL);
                st.initStyle(StageStyle.UTILITY);
                int i = 0;

                for (PropertyCard pc : playersProperties)
                {
                    final String currentCardName = pc.getCardName();
                    Pane p = new Pane();
                    p.setStyle("-fx-background-color: grey");
                    p.setMaxSize(130,200);
                    p.setPrefSize(130,200);
                    Pane colorPane = new Pane();
                    colorPane.setStyle("-fx-background-color: white");
                    colorPane.setPrefSize(126,30);
                    colorPane.relocate(2,2);
                    if (pc.getPropertyType() == PropertyCard.PropertyType.SIMPLE)
                    {
                        colorPane.setStyle("-fx-background-color: " + ((PlotPropertyCard)pc).getColorTypeString());
                        colorPane.setPrefSize(128,32);
                        colorPane.relocate(1,1);
                        if (((PlotPropertyCard)pc).getHasHotel())
                        {
                            Label ht = new Label("HOTEL");
                            ht.setStyle("-fx-font: bold 16pt Arial");
                            ht.relocate(30,7);
                            colorPane.getChildren().add(ht);
                        }
                        else
                        {
                            for (int j = 0;j < ((PlotPropertyCard)pc).getHouseNo();j++)
                            {
                                Button houseButton = new Button();
                                houseButton.setPrefSize(10,10);
                                houseButton.setStyle("-fx-background-color: yellow;");
                                houseButton.relocate(j*25+5,5);
                                colorPane.getChildren().add(houseButton);
                            }
                        }

                    }
                    p.getChildren().add(colorPane);
                    Label pCardName = new Label(pc.getCardName());
                    pCardName.setStyle("-fx-font: bold 12pt Arial");
                    pCardName.relocate(2,40);
                    p.getChildren().add(pCardName);

                    //Zálogba adás button-ja
                    Button zalogButton = new Button("Zálogba ad!");
                    zalogButton.setPrefSize(126,15);
                    zalogButton.setStyle("-fx-background-color: yellow;");
                    zalogButton.relocate(2,100);
                    zalogButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                            new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {

                                    setPawnPc(currentCardName);
                                    st.close();
                                }
                            });
                    p.getChildren().add(zalogButton);
                    p.relocate((i % 5) * 135,(i / 5) * 215);
                    propertiesWindowPane.getChildren().add(p);
                    i++;
                }
                st.showAndWait();
                doLog(Level.INFO, "Zálogba ad" + pawnPc.getCost().toString());
                if (pawnPc.getPropertyType() == PropertyCard.PropertyType.SIMPLE)
                    if (((PlotPropertyCard) pawnPc).getHasHotel())
                        sellHotelPlayer(((PlotPropertyCard) pawnPc).getColourType(), true);

                    while (((PlotPropertyCard)pawnPc).getHouseNo() > 0 && playerList.get(currentPlayer).getPlayerCash() <= 0)
                    {
                        sellHouseFromPlayer(((PlotPropertyCard) pawnPc).getColourType(), true);
                    }

                if (playerList.get(currentPlayer).getPlayerCash() <= 0)
                {
                    playerList.get(currentPlayer).setPlayerCash(+(pawnPc.getMortgageValue()));
                    playerList.get(currentPlayer).movePropertyToPawn(pawnPc);
                }
            }
		}
        if (playerList.get(currentPlayer).getPlayerCash() <= 0)
        {
            playerList.get(currentPlayer).isInGame = false;
        }
	}

	private Boolean checkGameOver()
	{
		Integer inGameCounter = 0;
		for ( Player p : playerList)
		{
			if (p.isInGame)
				++inGameCounter;
		}

        return inGameCounter <= 1;
    }

	private void checkPayToPlayer(Player player,PropertyCard propertyCard)
	{
		if(propertyCard==null) return;

		if(propertyCard.getIsOwned())
		{
			if(hasPropertyCard(propertyCard)!=null)
			{
				if(propertyCard.getPropertyType()==PropertyCard.PropertyType.SIMPLE)
				{
					if(((PlotPropertyCard) propertyCard).getHouseNo() > 0 && !((PlotPropertyCard)propertyCard).getHasHotel())
					{
						if(((PlotPropertyCard)propertyCard).getHouseNo()==1)
						{
							playerList.get(getPlayerCounter_CheckForOrderValidity()).payToPlayer(hasPropertyCard(propertyCard),((PlotPropertyCard)propertyCard).getRentalValueOneHouse());
						}
						else  if(((PlotPropertyCard)propertyCard).getHouseNo()==2)
						{
							playerList.get(getPlayerCounter_CheckForOrderValidity()).payToPlayer(hasPropertyCard(propertyCard),((PlotPropertyCard)propertyCard).getRentalValueTwoHouses());
						}
						else  if(((PlotPropertyCard)propertyCard).getHouseNo()==3)
						{
							playerList.get(getPlayerCounter_CheckForOrderValidity()).payToPlayer(hasPropertyCard(propertyCard),((PlotPropertyCard)propertyCard).getRentalValueThreeHouses());
						}
						else  if(((PlotPropertyCard)propertyCard).getHouseNo()==4)
						{
							playerList.get(getPlayerCounter_CheckForOrderValidity()).payToPlayer(hasPropertyCard(propertyCard),((PlotPropertyCard)propertyCard).getRentalValueFourHouses());
						}

					}
					else if(((PlotPropertyCard) propertyCard).getHasHotel())
					{
						playerList.get(getPlayerCounter_CheckForOrderValidity()).payToPlayer(hasPropertyCard(propertyCard),((PlotPropertyCard)propertyCard).getRentalValueHotel());
					}
					else
					{
						playerList.get(getPlayerCounter_CheckForOrderValidity()).payToPlayer(hasPropertyCard(propertyCard),((PlotPropertyCard)propertyCard).getRentalValueNoHouses());
					}
				}
				else if (propertyCard.getPropertyType()==PropertyCard.PropertyType.RAILING)
				{
					playerList.get(getPlayerCounter_CheckForOrderValidity()).payToPlayer(hasPropertyCard(propertyCard),((RailingPropertyCard)propertyCard).getValueOnePropertyOwned());
				}
				else if (propertyCard.getPropertyType()==PropertyCard.PropertyType.UTILITY)
				{
					playerList.get(getPlayerCounter_CheckForOrderValidity()).payToPlayer(hasPropertyCard(propertyCard),((UtilityPropertyCard)propertyCard).getValueOnePropertyOwned());
				}
			}
		}
	}

	public String processChanceCard(Player player)
	{

		Random generator = new Random();
		Integer currentCardId=generator.nextInt(14);
		System.out.println(chanceCardList.get(currentCardId).getDescription());

		if(chanceCardList.get(currentCardId).getType()==ChanceCard.Type.OUT_OF_JAIL)
		{
			player.addLuckyCardToPlayer(chanceCardList.get(currentCardId));
		}
		else if(chanceCardList.get(currentCardId).getType()==ChanceCard.Type.PAY)
		{
			bank.getMoneyFromPlayer(player,Integer.parseInt(chanceCardList.get(currentCardId).getAmount()));

		}
		else if (chanceCardList.get(currentCardId).getType()==ChanceCard.Type.GET)
		{
			bank.giveMoneyToPlayer(player,Integer.parseInt(chanceCardList.get(currentCardId).getAmount()));
		}
		else if (chanceCardList.get(currentCardId).getType()==ChanceCard.Type.ADVANCE_SPECIFIC_TWICE)
		{
			PropertyCard pc=getPropertyCardByName(chanceCardList.get(currentCardId).getAmount()) ;
			if(hasPropertyCard(pc)!=null)
				player.payToPlayer(hasPropertyCard(pc),pc.getCost()*2);
			player.setPlayerLocation(Integer.parseInt(chanceCardList.get(currentCardId).getAmount()));
		}
		else if(chanceCardList.get(currentCardId).getType()==ChanceCard.Type.ADVANCE_SPECIFIC)
		{
			PropertyCard pc=getPropertyCardByName(chanceCardList.get(currentCardId).getAmount()) ;
			if(hasPropertyCard(pc)!=null)
				player.payToPlayer(hasPropertyCard(pc),pc.getCost());
			player.setPlayerLocation(Integer.parseInt(chanceCardList.get(currentCardId).getAmount()));
		}
		else if(chanceCardList.get(currentCardId).getType()==ChanceCard.Type.ADVANCE_SPECIFIC_RICH)
		{
			player.setPlayerLocation(0);
			bank.giveMoneyToPlayer(player,200);
		}
		else if(chanceCardList.get(currentCardId).getType()==ChanceCard.Type.ADVANCE_NEGATIVE)
		{
			PropertyCard pc=getPropertyCardByName(chanceCardList.get(currentCardId).getAmount()) ;
			player.movePlayer(-3);
			if(hasPropertyCard(pc)!=null)
				player.payToPlayer(hasPropertyCard(pc),pc.getCost());
		}
		else if(chanceCardList.get(currentCardId).getType()==ChanceCard.Type.ADVANCE_SPECIFIC_POOR)
		{
			player.setPlayerLocation(10);
			player.setIsPlayerInJail(true);
		}

		return chanceCardList.get(currentCardId).getDescription();
	}

	public String processCommunityChestCard(Player player)
	{

		Random generator = new Random();
		Integer currentCardId=generator.nextInt(14);
		System.out.println(communityChestCardList.get(currentCardId).getDescription());
		if(communityChestCardList.get(currentCardId).getType()==ChanceCard.Type.OUT_OF_JAIL)
		{
			player.addLuckyCardToPlayer(chanceCardList.get(currentCardId));
		}
		else if(communityChestCardList.get(currentCardId).getType()==ChanceCard.Type.PAY)
		{
			bank.getMoneyFromPlayer(player,Integer.parseInt(communityChestCardList.get(currentCardId).getAmount()));

		}
		else if (communityChestCardList.get(currentCardId).getType()==ChanceCard.Type.GET)
		{
			bank.giveMoneyToPlayer(player,Integer.parseInt(communityChestCardList.get(currentCardId).getAmount()));
		}
		else if (communityChestCardList.get(currentCardId).getType()==ChanceCard.Type.ADVANCE_SPECIFIC_TWICE)
		{
			PropertyCard pc=getPropertyCardByName(communityChestCardList.get(currentCardId).getAmount()) ;
			if(hasPropertyCard(pc)!=null)
				player.payToPlayer(hasPropertyCard(pc),pc.getCost()*2);
			player.setPlayerLocation(Integer.parseInt(communityChestCardList.get(currentCardId).getAmount()));
		}
		else if(communityChestCardList.get(currentCardId).getType()==ChanceCard.Type.ADVANCE_SPECIFIC)
		{
			PropertyCard pc=getPropertyCardByName(communityChestCardList.get(currentCardId).getAmount()) ;
			if(hasPropertyCard(pc)!=null)
				player.payToPlayer(hasPropertyCard(pc),pc.getCost());
			player.setPlayerLocation(Integer.parseInt(communityChestCardList.get(currentCardId).getAmount()));
		}
		else if(communityChestCardList.get(currentCardId).getType()==ChanceCard.Type.ADVANCE_SPECIFIC_RICH)
		{
			player.setPlayerLocation(0);
			bank.giveMoneyToPlayer(player,200);
		}
		else if(communityChestCardList.get(currentCardId).getType()==ChanceCard.Type.ADVANCE_NEGATIVE)
		{
			PropertyCard pc=getPropertyCardByName(communityChestCardList.get(currentCardId).getAmount()) ;
			player.movePlayer(-3);
			if(hasPropertyCard(pc)!=null)
				player.payToPlayer(hasPropertyCard(pc),pc.getCost());
		}
		else if(communityChestCardList.get(currentCardId).getType()==ChanceCard.Type.ADVANCE_SPECIFIC_POOR)
		{
			player.setPlayerLocation(10);
			player.setIsPlayerInJail(true);
		}

		return communityChestCardList.get(currentCardId).getDescription();
	}

	private void checkIfHasToPayToBank(String elementName)
	{
		if(elementName.equals("Luxury Tax"))
		{
			bank.getMoneyFromPlayer(playerList.get(getPlayerCounter_CheckForOrderValidity()),75);
		}

		if(elementName.equals("Income Tax"))
		{
			if(playerList.get(getPlayerCounter_CheckForOrderValidity()).getPlayerCash()>2000)
			{
				bank.getMoneyFromPlayer(playerList.get(getPlayerCounter_CheckForOrderValidity()),200);
			}
			else
			{
				bank.getMoneyFromPlayer(playerList.get(getPlayerCounter_CheckForOrderValidity()),(int)(playerList.get(getPlayerCounter_CheckForOrderValidity()).getPlayerCash()*0.1));
			}
		}
	}

	public boolean isCurrentPlayerOwnProperty(PropertyCard pc){
		//return playerList.get(currentPlayer()).isPlayerOwnProperty(pc);
		return playerList.get(currentPlayer()).getDoIHaveThisCard(pc);
	}
    public void sellHouseFromPlayer(PlotPropertyCard.Colour_Type ct, boolean isHuman){
        if(ct!=null)
        {
            PlotPropertyCard propertyCard;
            if (isHuman)
                propertyCard= decreasePropertyHouseNumberByColour(ct,0);
            else
                propertyCard= decreasePropertyHouseNumberByColour(ct,getPlayerCounter_CheckForOrderValidity());
            Integer no;
            if(ct == PlotPropertyCard.Colour_Type.BROWN || ct == PlotPropertyCard.Colour_Type.BLUE)
                no=2;
            else
                no=3;

            if (isHuman)
                bank.takeHouseFromPlayer(playerList.get(0), propertyCard, no);
            else
                bank.giveHouseToPlayer(playerList.get(getPlayerCounter_CheckForOrderValidity()), propertyCard, no);
        }
    }
    private void buyHousePlayer(PlotPropertyCard.Colour_Type colour_type, boolean isHumanBuys)
    {
        if(colour_type!=null)
        {
            Integer no;
            if(colour_type == PlotPropertyCard.Colour_Type.BROWN || colour_type == PlotPropertyCard.Colour_Type.BLUE)
                no=2;
            else
                no=3;
            PlotPropertyCard propertyCard;
            if (isHumanBuys)
                propertyCard = getPropertyCardByColour(colour_type,0,no);
            else
                propertyCard = getPropertyCardByColour(colour_type,getPlayerCounter_CheckForOrderValidity(),no);


            if (isHumanBuys)
                bank.giveHouseToPlayer(playerList.get(0), propertyCard, no);
            else
                bank.giveHouseToPlayer(playerList.get(getPlayerCounter_CheckForOrderValidity()), propertyCard, no);


            doLog(Level.INFO, "Házat vett a " + colour_type + " színű mezőre");
            doLog(Level.INFO, "Ennyi háza van a  " + colour_type + " színű mezőn: " + propertyCard.getHouseNo());
        }
    }

    private PlotPropertyCard getPropertyCardByColour(PlotPropertyCard.Colour_Type colour_type, Integer PlayerId, Integer houseNo)
    {
        PlotPropertyCard plotPropertyCard = null;
        PlotPropertyCard resultProperty = null;
        for(PropertyCard pc : playerList.get(PlayerId).getPropertyList())
        {
            if(pc.getPropertyType()==PropertyCard.PropertyType.SIMPLE)
            {
                plotPropertyCard=(PlotPropertyCard)pc;
                if(plotPropertyCard.getColourType() == colour_type )
                {
                    if ( plotPropertyCard.getHouseNo() < 4 && (plotPropertyCard.getHouseCost() * houseNo) < playerList.get(PlayerId).getPlayerCash())
                        plotPropertyCard.increaseHouseNo();
                    resultProperty = plotPropertyCard;
                }
            }
        }
        return resultProperty;
    }

    private PlotPropertyCard decreasePropertyHouseNumberByColour(PlotPropertyCard.Colour_Type colour_type, Integer PlayerId)
    {
        PlotPropertyCard plotPropertyCard = null;
        PlotPropertyCard resultProperty = null;
        for(PropertyCard pc : playerList.get(PlayerId).getPropertyList())
        {
            if(pc.getPropertyType()==PropertyCard.PropertyType.SIMPLE)
            {
                plotPropertyCard=(PlotPropertyCard)pc;
                if(plotPropertyCard.getColourType() == colour_type )
                {
                    if ( plotPropertyCard.getHouseNo() > 0 )
                        plotPropertyCard.decreaseHouseNo();
                    resultProperty = plotPropertyCard;
                }
            }
        }
        return resultProperty;
    }
    private void buyHouseAIPlayer()  {   
        AIPlayer aiPlayer= (AIPlayer) playerList.get(getPlayerCounter_CheckForOrderValidity());

        PlotPropertyCard.Colour_Type colour_type=aiPlayer.makeHouseDecision(getPropertyCardList());


        buyHousePlayer(colour_type,false);
    }

    public void buyHotelPlayer(PlotPropertyCard.Colour_Type colour_type, boolean isHumanBuys)
    {
        Boolean canBuyMore=false;
        if(colour_type!=null)
        {
            PlotPropertyCard propertyCard=null;
            Integer playerId;
            if (isHumanBuys)
                playerId = 0;
            else
                playerId = getPlayerCounter_CheckForOrderValidity();

            for(PropertyCard pc:playerList.get(playerId).getPropertyList())
            {
                if(pc.getPropertyType()==PropertyCard.PropertyType.SIMPLE)
                {
                    PlotPropertyCard plotPropertyCard=(PlotPropertyCard)pc;
                    if(plotPropertyCard.getColourType()== colour_type )
                    {
                        if(!plotPropertyCard.getHasHotel())
                        {
                            plotPropertyCard.setHasHotel(true);
                            canBuyMore=true;
                        }
                        propertyCard=plotPropertyCard;
                    }
                }
            }

            if(propertyCard!=null && canBuyHotel(propertyCard))
            {
                Integer no;
                if(colour_type== PlotPropertyCard.Colour_Type.BROWN || colour_type== PlotPropertyCard.Colour_Type.BLUE)
                    no=2;
                else
                    no=3;

                if(canBuyMore)
                {
                    if (isHumanBuys)
                        bank.giveHotelToPlayer(playerList.get(0),propertyCard,no);
                    else
                        bank.giveHotelToPlayer(playerList.get(getPlayerCounter_CheckForOrderValidity()),propertyCard,no);
                    doLog(Level.INFO, "Hotelt vett a " + colour_type + " színű mezőre");
                }
            }
        }
    }
    public void sellHotelPlayer(PlotPropertyCard.Colour_Type colour_type, boolean isHumanBuys)
    {
        Boolean canBuyMore=false;
        if(colour_type!=null)
        {
            PlotPropertyCard propertyCard=null;
            Integer playerId;
            if (isHumanBuys)
                playerId = 0;
            else
                playerId = getPlayerCounter_CheckForOrderValidity();

            for(PropertyCard pc:playerList.get(playerId).getPropertyList())
            {
                if(pc.getPropertyType()==PropertyCard.PropertyType.SIMPLE)
                {
                    PlotPropertyCard plotPropertyCard=(PlotPropertyCard)pc;
                    if(plotPropertyCard.getColourType()== colour_type )
                    {
                        if(plotPropertyCard.getHasHotel())
                        {
                            plotPropertyCard.setHasHotel(false);
                            canBuyMore=true;
                        }
                        propertyCard=plotPropertyCard;
                    }
                }
            }

            if(propertyCard!=null)
            {
                Integer no;
                if(colour_type== PlotPropertyCard.Colour_Type.BROWN || colour_type== PlotPropertyCard.Colour_Type.BLUE)
                    no=2;
                else
                    no=3;

                if(canBuyMore)
                {
                    if (isHumanBuys)
                        bank.takeHotelFromPlayer(playerList.get(0), propertyCard, no);
                    else
                        bank.takeHotelFromPlayer(playerList.get(getPlayerCounter_CheckForOrderValidity()), propertyCard, no);
                    doLog(Level.INFO, "Hotelt eladott a " + colour_type + " színű mezőre");
                }
            }
        }
    }
    private boolean canBuyHotel(PlotPropertyCard propertyCard)
    {
        return playerList.get(getPlayerCounter_CheckForOrderValidity()).getPlayerCash() > propertyCard.getHotelCost();
    }

    public void buyHotelAIPlayer()
    {
        AIPlayer aiPlayer= (AIPlayer) playerList.get(getPlayerCounter_CheckForOrderValidity());
        PlotPropertyCard.Colour_Type colour_type=aiPlayer.makeHotelDecision();

        buyHotelPlayer(colour_type,false);
    }

    private void buyProperty(String name, boolean buy) {
        if(buy)
        {
            bank.givePropertyToPlayer(playerList.get(playerWhoThrown),name);
        }
    }

    public PropertyCard getPropertyCardByName(String name)
    {
        for ( PropertyCard pc : bank.propertyCardList )
        {
            if(pc.getCardName().equals(name))
            {
                return pc;
            }
        }
        return null;
    }

    public Player hasPropertyCard(PropertyCard propertyCard)
    {
        if(propertyCard==null)
            return null;
        for(Player p:playerList)
        {
            doLog(Level.INFO, "Játékos neve: " + p.getPlayerName() + ", Ellenorzes erre:" + propertyCard.getCardName());
           if( p.getDoIHaveThisCard(propertyCard) )
           {
               doLog(Level.INFO, "Játékos neve: " + p.getPlayerName() + ", isOwned: " + propertyCard.getIsOwned());
               return p;
           }
        }
        return null;
    }

    public Integer getPlayerNumber(){
        return playerNumber;
    }

	public ArrayList<Player> loadGame(String fileName) {
		XMLParser xmlParser = new XMLParser();
		LoadObject load = xmlParser.handleLoadXML(fileName);

		playerList = new ArrayList<Player>();
		playerNumber = load.getPlayerObjs().size()-1;

		//Human játékos
		HumanPlayer humanPlayer = new HumanPlayer(load.getPlayerObjs().get(0).getName());
		humanPlayer.setPlayerCash(load.getPlayerObjs().get(0).getCash());
		humanPlayer.setCurrentPlace(load.getPlayerObjs().get(0).getLocation());
		ArrayList<LoadObjectProperty> lObjPropertyList = load.getPlayerObjs().get(0).getPropertyList();
		for ( int i = 0; i < load.getPlayerObjs().get(0).getPropertyList().size(); ++i )
		{
			Integer pId = lObjPropertyList.get(i).getpId();
			PropertyCard pc = getPropertyCardById(pId);
			humanPlayer.addPropertyToPlayer(pc);
		}
		playerList.add(humanPlayer);

		//AI Játékosok
		for ( int i = 1; i < load.getPlayerObjs().size(); ++i )
		{
			AIPlayer aiPlayer = new AIPlayer(load.getPlayerObjs().get(i).getName());
			aiPlayer.setPlayerCash(load.getPlayerObjs().get(i).getCash());
			aiPlayer.setCurrentPlace(load.getPlayerObjs().get(i).getLocation());
			Integer propertySize = load.getPlayerObjs().get(i).getPropertyList().size();
			lObjPropertyList = load.getPlayerObjs().get(i).getPropertyList();
			for ( int j = 0; j < propertySize; ++j )
			{
				Integer pId = lObjPropertyList.get(j).getpId();
				PropertyCard pc = getPropertyCardById(pId);
				aiPlayer.addPropertyToPlayer(pc);
			}
			playerList.add(aiPlayer);
		}

		//Bank
		bank = new Bank();
		bank.load(load.getBankObject().getBankMoney()
					,load.getBankObject().getPropertyList());

		return playerList;
	}

	private PropertyCard getPropertyCardById(Integer id)
	{
		for ( PropertyCard pc : propertyCardList )
		{
			if ( pc.getId() == id )
			{
				return pc;
			}
		}
		return null;
	}

	private void showGameOverWindow()
	{
		try{
            doLog(Level.INFO, "Vége a játéknak!");
			FXMLLoader loader = new FXMLLoader();
			Parent p = loader.load(getClass().getResource("../View/card.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Vége a játéknak!");
			Scene scene = new Scene(p, 320, 120);
			stage.setScene(scene);
			stage.show();
		} catch (Throwable ex) {
			//Location is required exception: class.getResource() will return null
			//if it does not find the resource.
			System.out.println(ex.getMessage());
		}
	}

    public PropertyCard getPropertyByNameFromBank(String propertyName){
        return bank.getPropertyByName(propertyName);
    }

    public ArrayList<PropertyCard> getPlayerPropertyCards(Integer playerId)
    {
        return playerList.get(playerId).getPropertyList();
    }
    public ArrayList<PropertyCard> getPlayerPawnCards(Integer playerId)
    {
        return playerList.get(playerId).getPawnList();
    }
    public boolean buyPropertyForPlayer(String propertyName)
    {
        if (playerList.get(0).getPlayerCash() >= bank.getPropertyByName(propertyName).getCost())
        {
            buyProperty(propertyName,true);
            return true;
        }
        else
        {
            doLog(Level.INFO, "A " + playerList.get(getPlayerNoWhoThrown()).getPlayerName() + " nevű játékos nem tudta megvenni a telket, mert nem volt elég pénze!");
            return false;
        }
    }

    public boolean canPlayerBuyProperty(String propertyName)
    {
        return playerList.get(0).getPlayerCash() >= bank.getPropertyByName(propertyName).getCost();
    }



    public ArrayList<PlotPropertyCard.Colour_Type> getHumanPlayerBuildableColors()
    {
        return bank.getPlayerBuildableColors(playerList.get(0));
    }

    public Integer getPlayerNoWhoThrown(){
        return playerWhoThrown;
    }

//    public void writeOutPlayersProperties(){
//        System.out.println("Itt");
//        for (PropertyCard pc : playerList.get(0).getPropertyList())
//            System.out.println("Player own:" + pc.getCardName() + "\n");
//    }

    public boolean buyHouseForHumanPlayer(PlotPropertyCard.Colour_Type ct){
        buyHousePlayer(ct,true);
        return true;
    }

    public boolean buyHotelForHumanPlayer(PlotPropertyCard.Colour_Type ct){
        buyHotelPlayer(ct,true);
        return true;
    }

    public boolean isFourHouseOnColor(PlotPropertyCard.Colour_Type ct)
    {
        if(ct!=null)
        {
            for(PropertyCard pc : bank.propertyCardList)
            {
                if(pc.getPropertyType()==PropertyCard.PropertyType.SIMPLE)
                {
                    PlotPropertyCard plotPropertyCard=(PlotPropertyCard)pc;
                    if(plotPropertyCard.getColourType()== ct )
                    {
                         return plotPropertyCard.getHouseNo()==4;
                    }
                }
            }
        }
        return  false;
    }

    public boolean isHotelOnColor(PlotPropertyCard.Colour_Type ct)
    {
        if(ct!=null)
        {
            for(PropertyCard pc : bank.propertyCardList)
            {
                if(pc.getPropertyType()==PropertyCard.PropertyType.SIMPLE)
                {
                    PlotPropertyCard plotPropertyCard=(PlotPropertyCard)pc;
                    if( plotPropertyCard.getColourType()== ct )
                    {
                        return plotPropertyCard.getHasHotel();
                    }
                }
            }
        }
        return false;
    }

    public String getLastChanceCard(){
        return lastChanceCard;
    }

    public String getLastCommunityCard(){
        return lastCommunityCard;
    }

    public ArrayList<PropertyCard> getPropertyCardList()
    {
        return bank.getPropertyCardList();
    }
    private void setPawnPc(String name)
    {
        pawnPc = getPropertyCardByName(name);
    }
    public void buyBackFromPawn(PropertyCard pc, Integer playerId)
    {
        if (playerList.get(playerId).getPlayerCash() > (pc.getMortgageValue() * 1.1))
        {
            playerList.get(playerId).movePawnBackToProperty(pc);
            playerList.get(playerId).setPlayerCash(-(int)(pc.getMortgageValue() * 1.1));
        }
    }
    //****************************************************
    public void buyBackFromPawnAI(Player player)
    {
        AIPlayer aiPlayer=(AIPlayer)player;
        if(aiPlayer!=null) {
            PropertyCard pc = aiPlayer.getPropertyBack(aiPlayer.getPawnList());
            if (player.getPlayerCash() > (pc.getMortgageValue() * 1.1) && !aiPlayer.getPawnList().isEmpty())
            {
               aiPlayer.movePawnBackToProperty(pc);
                aiPlayer.setPlayerCash(-(int)(pc.getMortgageValue() * 1.1));
            }
        }
    }
    public boolean isPlayerHavePawn(Integer playerId)
    {
        return !(playerList.get(playerId).getPawnList().isEmpty());
    }
}
