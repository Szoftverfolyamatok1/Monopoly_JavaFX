package Monopoly.Model.Objects;

import java.util.Random;
import javafx.util.Pair;

public class Dices {
	private int firstDice;
	private int secondDice;

	public Dices() {}

	//Maybe, this won't be needed
	public Dices(int first, int second)
	{
		this.firstDice = first;
		this.firstDice = first;
		this.secondDice = second;
	}

    //VIVI
    //We don't need this anymore. The rollTheDice() function returns both numbers separately, not the sum
	private int getDiceSum()
	{
		return firstDice + secondDice;
	}

	public boolean isDoubled()
	{
		return firstDice == secondDice;
	}

    //maybe this return will not be needed
    //VIVI
    //this IS needed :D (for the new dice button)
	public Pair<Integer,Integer> rollTheDice()
	{
		Random rand = new Random();
		firstDice = rand.nextInt(6)+1;
		secondDice = rand.nextInt(6)+1;

		return new Pair<Integer, Integer>(firstDice,secondDice);
	}
}
