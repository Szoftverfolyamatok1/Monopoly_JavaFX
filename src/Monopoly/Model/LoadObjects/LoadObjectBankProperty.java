package Monopoly.Model.LoadObjects;

/**
 * User: Benjamin
 * Date: 2013.12.03.
 * Time: 2:17
 */
public class LoadObjectBankProperty {
	private Integer pId;
	private Boolean isOwned;

	public LoadObjectBankProperty(Integer pId
							  ,Boolean isOwned)
	{
		this.pId = pId;
		this.isOwned = isOwned;
	}

	public Integer getpId() {
		return pId;
	}

	public Boolean getOwned() {
		return isOwned;
	}
}
