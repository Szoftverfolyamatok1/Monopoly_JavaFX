/*package Monopoly.Test;

import Monopoly.Model.Objects.Bank;
import Monopoly.Model.Players.HumanPlayer;
import Monopoly.Model.XML.XMLParser;
import junit.framework.TestCase;*/

/**
 * User: Benjamin
 * Date: 2013.10.26.
 * Time: 18:36
 */

/*
public class TestClass extends TestCase {

	public void testPlayer(){
		HumanPlayer player = new HumanPlayer("Alfonz");
        assertTrue(!player.getIsPlayerInJail());
		assertEquals(player.getPlayerCash(),0);

		for ( int i = 0; i < 5000; ++i )
		{
			System.out.println("-------------------------NEW ROLL START------------------------------");
			player.rollTheDice();
			if ( player.getIsCurrentThrowDoubled() )
			{
				if ( player.checkIfItWasThreeDoublesInARow() )
				{
					assertTrue(player.getIsPlayerInJail());
					System.out.println("In jail");
				}
				else
				{
					assertTrue(!player.getIsPlayerInJail());
				}
			}
			System.out.println("-------------------------ROLL END------------------------------");
		}
	}

	//needs outwork
//	public void testParserWithWrongLocation(){
//		try{
//			XMLParser parser = new XMLParser();
//			parser.handleBoardElementXML("");
//			assertTrue("Exception wasn't thrown", false);
//		} catch (NullPointerException npe) {
//			System.out.println(npe.getMessage());
//			assertEquals("Content is not allowed in prolog.", npe.getMessage());
//		}
//	}

	public void testParserWithCorrectLocation(){
		XMLParser parser = new XMLParser();
		assertNotNull(parser.handleBoardElementXML("C:\\Users\\Benjamin\\IdeaProjects\\JavaFXCanvas\\XML\\BoardElements.xml"));
	}

	public void testMoneyTransaction(){
		Bank bank = new Bank();
		HumanPlayer humanPlayer = new HumanPlayer("Józsi");
		assertTrue(humanPlayer.getPlayerCash() == 0);
		assertTrue(bank.getBankMoney() == 100000);
		bank.giveMoneyToPlayer(humanPlayer, 200);
		assertTrue(humanPlayer.getPlayerCash() == 200);
		assertTrue(bank.getBankMoney() == 99800);
	}

	public void testPropertyTransactionWithNotEnoughMoney(){
		Bank bank = new Bank();
		HumanPlayer humanPlayer = new HumanPlayer("Józsi");

		assertTrue(humanPlayer.getPropertyCount() == 0);
		bank.givePropertyToPlayer(humanPlayer, "Electric company");
		assertTrue(humanPlayer.getPropertyCount() == 0);
	}

	public void testPropertyTransactionWithEnoughMoney(){
		Bank bank = new Bank();
		HumanPlayer humanPlayer = new HumanPlayer("Józsi");
		bank.giveMoneyToPlayer(humanPlayer, 200); //give has been already tested

		assertTrue(humanPlayer.getPropertyCount() == 0);
		bank.givePropertyToPlayer(humanPlayer, "Electric Company");
		assertTrue(humanPlayer.getPropertyCount() == 1);
	}

}
*/