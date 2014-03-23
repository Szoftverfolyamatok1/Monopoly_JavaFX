package Monopoly.Model.Objects;

import java.util.Random;

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

	private int getDiceSum()
	{
		return firstDice + secondDice;
	}

	public boolean isDoubled()
	{
		return firstDice == secondDice;
	}

	//maybe this return will not be needed
	public int rollTheDice()
	{
		Random rand = new Random();
		firstDice = rand.nextInt(6)+1;
		secondDice = rand.nextInt(6)+1;
		return getDiceSum();
	}
}
