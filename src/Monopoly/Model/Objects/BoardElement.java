package Monopoly.Model.Objects;
import Monopoly.Model.XML.XMLParser;

import java.util.ArrayList;
import java.lang.*;

public class BoardElement {

	private Integer id;
	private String elementName;

	private ArrayList<BoardElement> boardElementList;

	final String boardXMLPath = "XML/BoardElements.xml";

	public BoardElement()
	{
		boardElementList = new ArrayList<BoardElement>(new XMLParser().handleBoardElementXML(boardXMLPath));
	}

	public BoardElement(Integer id, String elementName)
	{
		this.id = id;
		this.elementName = elementName;
	}

	private String getElementName(BoardElement e)
	{
		return e.elementName;
	}

	public String getElementById(Integer id)
	{
		for ( BoardElement be : boardElementList )
		{
			if (be.getId().equals(id))
			{
				return be.getElementName(be);
			}
		}
		return null;
	}

	public Integer getElementByName(String elementName)
	{
		for ( BoardElement be : boardElementList )
		{
			if ( be.getElementName(be).equals(elementName) )
			{
				return be.getId();
			}
		}
		return null;
	}

	public Integer getId()
	{
		return id;
	}
}