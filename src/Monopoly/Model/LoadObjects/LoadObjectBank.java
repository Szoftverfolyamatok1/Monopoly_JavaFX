package Monopoly.Model.LoadObjects;

import java.util.ArrayList;

/**
 * User: Benjamin
 * Date: 2013.12.03.
 * Time: 0:36
 */
public class LoadObjectBank {
	private ArrayList<LoadObjectBankProperty> propertyList;
	private Integer bankMoney;

	public LoadObjectBank(){}

	public LoadObjectBank(Integer bankMoney
						  ,ArrayList<LoadObjectBankProperty> propertyList)
	{
		this.bankMoney = bankMoney;
		this.propertyList = propertyList;
	}

	public Integer getBankMoney() {
		return bankMoney;
	}

	public ArrayList<LoadObjectBankProperty> getPropertyList() {
		return propertyList;
	}
}
