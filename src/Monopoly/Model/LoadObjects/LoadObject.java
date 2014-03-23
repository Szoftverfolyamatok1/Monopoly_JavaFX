package Monopoly.Model.LoadObjects;

import java.util.ArrayList;

/**
 * User: Benjamin
 * Date: 2013.12.02.
 * Time: 21:50
 */
public class LoadObject {
	private ArrayList<LoadObjectPlayer> lObjPlayerList;
	private LoadObjectBank lObjBank;

	public LoadObject()
	{
		lObjPlayerList = new ArrayList<LoadObjectPlayer>();
		lObjBank = new LoadObjectBank();
	}

	public void addLoadObjectPlayer(LoadObjectPlayer lObjPlayer)
	{
		lObjPlayerList.add(lObjPlayer);
	}

	public void setLoadObjectBank(LoadObjectBank lObjBank)
	{
		this.lObjBank = lObjBank;
	}

	public ArrayList<LoadObjectPlayer> getPlayerObjs()
	{
		return lObjPlayerList;
	}

	public LoadObjectBank getBankObject()
	{
		return lObjBank;
	}
}
