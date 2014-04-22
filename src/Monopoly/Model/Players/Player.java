package Monopoly.Model.Players;

import Monopoly.Model.Cards.Card;
import Monopoly.Model.Cards.PropertyCard;
import Monopoly.Model.Objects.Dices;
import static Monopoly.Logger.LoggerClass.doLog;
import java.util.ArrayList;
import java.util.logging.Level;
import javafx.util.Pair;

public abstract class Player {
	final private int MAX_DOUBLE_THROW_NUMBER_IN_A_ROW = 3;
	final private int TURNS_TO_GET_OUT_FROM_JAIL = 3;

	private String playerName;
	protected int playerCash;
	protected Integer playerLocation;
	private boolean isPlayerInJail;
	private boolean canPlayerThrowOneMore;
	private int doubleThrowCount;
	private Dices currentThrowResult;
	private int playerTurnsInJail;
	private int currentThrowSum;
	protected int turnNumber;
    public Integer brownPropertyNo;
    protected Integer lightBluePropertyNo;
    protected Integer purplePropertyNo;
    protected Integer orangePropertyNo;
    protected Integer redPropertyNo;
    protected Integer yellowPropertyNo;
    protected Integer greenPropertyNo;
    protected Integer bluePropertyNo;
	public Boolean isInGame;

	protected ArrayList<Card> playerCardList;
	protected ArrayList<PropertyCard> playerPropertyList;

    public abstract boolean step(PropertyCard propertyCard,Player player, ArrayList<PropertyCard> propertyCardList); //I think it will need few parameters


    public void payToPlayer(Player player,Integer amount)
    {
        doLog(Level.INFO, "Játékosnak fizetve. Név: " + player.getPlayerName() + ", összeg: " + amount);
        player.setPlayerCash(amount);
        this.playerCash-=amount;
    }


	//needs more outwork
	//Needs to be public
	public void initializePlayer() {
		this.turnNumber = 0;
		this.playerTurnsInJail = 0;
		this.playerCash = 0;
		this.playerLocation = 0;
		this.playerPropertyList = new ArrayList<PropertyCard>();
		this.playerCardList = new ArrayList<Card>();
		this.isPlayerInJail = false;
		this.canPlayerThrowOneMore = false;
		this.doubleThrowCount = 0;
		this.currentThrowResult = new Dices();
		this.currentThrowSum = 0;
        this.brownPropertyNo=0;
        this.bluePropertyNo=0;
        this.yellowPropertyNo=0;
        this.redPropertyNo=0;
        this.greenPropertyNo=0;
        this.purplePropertyNo=0;
        this.orangePropertyNo=0;
        this.lightBluePropertyNo=0;
	}

	public String getPlayerName() {
		return playerName;
	}

	//Needs to be public
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	//simply roll the dice, and check if it is double
    //VIVI
    //returns both numbers as a Pair, so that we can display the dices
	public Pair<Integer,Integer> rollTheDice() {
		if ( doubleThrowCount == 0 || doubleThrowCount == -1 ){
            ++turnNumber;
        }

        Pair<Integer,Integer> throwResult = currentThrowResult.rollTheDice();
		currentThrowSum = throwResult.getKey()+throwResult.getValue();

		checkIfItWasDouble();
		if (!getIsPlayerInJail()){
            //step();
            playerLocation=(playerLocation+currentThrowSum)%40;
        }
        else{
            playerLocation=10;
        }

        return throwResult;
    }

	public Integer getCurrentPlace(){
        return playerLocation;
    }

	public void setCurrentPlace(Integer placeNumber)
	{
		this.playerLocation = placeNumber;
	}

	//void instead of boolean return
	//need to get, if a throw was double
	//if there were 3 doubles in a row, then the player MUST go to jail
	//if the player was in jail, and threw double in 3 turns, then able to come out
	//else has to pay 50$s to the Bank
	//if the player threw double, then able to throw one more, while has fewer double throws than 3
    private void checkIfItWasDouble() {
		if (currentThrowResult.isDoubled())
		{
			handleIfThrowWasDouble();
		}
		else
		{
			handleIfThrowWasNotDouble();
		}
	}

	private void handleIfThrowWasNotDouble() {
        //player is in not jail, or this is the 4. turn in jail
        if ( getIsPlayerInJail() )
        {
            checkForInJailTurns();
        }

        setCanPlayerThrowOneMore(false);
        doubleThrowCount = 0;
	}

	//check if time spent in jail is < than 3
	private void checkForInJailTurns() {
		if ( playerTurnsInJail < TURNS_TO_GET_OUT_FROM_JAIL )
		{
			++playerTurnsInJail;
		}
		else
		{
			setIsPlayerInJail(false);
			playerTurnsInJail = 0;
		}
	}

	private void handleIfThrowWasDouble() {
        doLog(Level.INFO, "A " + this.getPlayerName() + " nevű játékos duplát dobott.");
		if ( !getCanPlayerThrowOneMore() && !getIsPlayerInJail() )
		{
			setCanPlayerThrowOneMore(true);
		}

       	else if ( getIsPlayerInJail() )
		{
			setIsPlayerInJail(false);
			setCanPlayerThrowOneMore(false);
			doubleThrowCount = -1;
		}

		++doubleThrowCount;
		if ( checkIfItWasThreeDoublesInARow() )
		{
           doLog(Level.INFO, "A " + this.getPlayerName() + " nevű játékos háromszor dobott duplát egyhuzamban!");
		   if ( !getIsPlayerInJail() )
		   {
			   setIsPlayerInJail(true);
			   playerTurnsInJail = 0;
               setCanPlayerThrowOneMore(false);
		   }
		}
	}


    //check if the player threw 3 doubles in a row ( definitely "csalik" )
	//needs to be public
	public boolean checkIfItWasThreeDoublesInARow()
	{
		return this.doubleThrowCount == MAX_DOUBLE_THROW_NUMBER_IN_A_ROW;
	}

	public boolean getIsPlayerInJail() {
			return this.isPlayerInJail;
	}

	public void setIsPlayerInJail(boolean playerInJail) {
		this.isPlayerInJail = playerInJail;
		setCanPlayerThrowOneMore(false);
//		doubleThrowCount = 0;
	}

	public boolean getCanPlayerThrowOneMore() {
		return canPlayerThrowOneMore;
	}

	private void setCanPlayerThrowOneMore(boolean canPlayerThrowOneMore) {
		this.canPlayerThrowOneMore = canPlayerThrowOneMore;
	}

	public boolean getIsCurrentThrowDoubled() {
		return currentThrowResult.isDoubled();
	}

	public int getPlayerCash() {
		return playerCash;
	}

	public void setPlayerCash(Integer amount)
	{
//		System.out.println("Player's money bf: " + playerCash);
		this.playerCash += amount;
//		System.out.println("Player's money af: " + playerCash);
	}

    //this might be useful for future features
//	public int getCurrentThrowSum(){
//		return currentThrowSum;
//	}

	public void addLuckyCardToPlayer(Card card) {
		this.playerCardList.add(card);
	}

	public void addPropertyToPlayer(PropertyCard card) {
		this.playerPropertyList.add(card);
	}

    //this might be useful for future features
//	public void removePropertyFromPlayer(PropertyCard card)
//	{
//		this.playerPropertyList.remove(card);
//	}

	public int getPropertyCount()
	{
		return playerPropertyList.size();
	}

    public boolean getDoIHaveThisCard(PropertyCard propertyCard)
    {
        for(PropertyCard pc:playerPropertyList)
        {
            if(pc.getCardName().equals(propertyCard.getCardName()) )
            {
                return true;
            }
        }
        return false;
    }

	public ArrayList<PropertyCard> getPropertyList()
	{
		return playerPropertyList;
	}

	public ArrayList<Card> getCardList()
	{
		return playerCardList;
	}

	public void movePlayer(int steps)
	{
		playerLocation=(playerLocation+steps)%40;
	}
	public void setPlayerLocation(int location)
	{
		playerLocation=location;
	}
}