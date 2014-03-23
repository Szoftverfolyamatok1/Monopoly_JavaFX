package Monopoly.Model.XML;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import Monopoly.Model.Objects.Bank;
import Monopoly.Model.Cards.Card;
import Monopoly.Model.Cards.PlotPropertyCard;
import Monopoly.Model.Cards.PropertyCard;
import Monopoly.Model.Players.Player;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static Monopoly.Logger.LoggerClass.doLog;

/**
 * User: Benjamin
 * Date: 2013.11.30.
 * Time: 22:49
 */
public class XMLWriter {

	public XMLWriter(){}

	public void writeToXML(ArrayList<Player> players, Bank bank)
	{
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("saveGame");
			doc.appendChild(rootElement);

			//Játékosok írása
			for ( Player p : players )
			{
                Element player = createPlayerElement(players, doc, rootElement, p);

				//Property lista kiírása
				for (PropertyCard pc :  p.getPropertyList())
				{
                    createPropertyElementForPlayer(doc, player, pc);
				}

				//Kártyák kiírása
				for (Card pc :  p.getCardList())
				{
                    createCardElementForPlayer(doc, player, pc);
				}
			}

			//Bank
            createBankElementForPlayer(bank, doc, rootElement);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("Saves/" + getFileNameForSave() + ".xml"));

			transformer.transform(source, result);

            doLog(Level.INFO, "File elmentve!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

    private void createBankElementForPlayer(Bank bank, Document doc, Element rootElement) {
        Element eBank = doc.createElement("bankElement");
        rootElement.appendChild(eBank);

        Element eBankMoney = doc.createElement("bankMoney");
        eBankMoney.appendChild(doc.createTextNode(Integer.toString(bank.getBankMoney())));
        eBank.appendChild(eBankMoney);

        for (PropertyCard pc :  bank.propertyCardList )
        {
            Element property = doc.createElement("propertyElement");
            eBank.appendChild(property);

            Attr pattr = doc.createAttribute("pId");
            pattr.setValue(Integer.toString(pc.getId()));
            property.setAttributeNode(pattr);

            Element isOwned = doc.createElement("isOwned");
            isOwned.appendChild(doc.createTextNode(Boolean.toString(pc.getIsOwned())));
            property.appendChild(isOwned);
        }
    }

    private void createCardElementForPlayer(Document doc, Element player, Card pc) {
        Element card = doc.createElement("cardElement");
        player.appendChild(card);

        Attr cattr = doc.createAttribute("cId");
        cattr.setValue(Integer.toString(pc.getId()));
        card.setAttributeNode(cattr);
    }

    private void createPropertyElementForPlayer(Document doc, Element player, PropertyCard pc) {
        Element property = doc.createElement("propertyElement");
        player.appendChild(property);

        Attr pattr = doc.createAttribute("pId");
        pattr.setValue(Integer.toString(pc.getId()));
        property.setAttributeNode(pattr);

        if ( pc.getPropertyType() == PropertyCard.PropertyType.SIMPLE )
        {
            PlotPropertyCard ppc = (PlotPropertyCard)pc;

            Element pType = doc.createElement("pType");
            pType.appendChild(doc.createTextNode(PropertyCard.PropertyType.SIMPLE.toString()));
            property.appendChild(pType);

            Element houseNo = doc.createElement("houseNumber");
            houseNo.appendChild(doc.createTextNode(Integer.toString(ppc.getHouseNo())));
            property.appendChild(houseNo);

            Element hasHotel = doc.createElement("hasHotel");
            if ( ppc.getHasHotel() )
                hasHotel.appendChild(doc.createTextNode("TRUE"));
            else
                hasHotel.appendChild(doc.createTextNode("FALSE"));
            property.appendChild(hasHotel);
        }
        else if ( pc.getPropertyType() == PropertyCard.PropertyType.RAILING )
        {
            Element pType = doc.createElement("pType");
            pType.appendChild(doc.createTextNode(PropertyCard.PropertyType.UTILITY.toString()));
            property.appendChild(pType);
        }
        else if ( pc.getPropertyType() == PropertyCard.PropertyType.UTILITY )
        {
            Element pType = doc.createElement("pType");
            pType.appendChild(doc.createTextNode(PropertyCard.PropertyType.RAILING.toString()));
            property.appendChild(pType);
        }
    }

    private Element createPlayerElement(ArrayList<Player> players, Document doc, Element rootElement, Player p) {
        Element player = doc.createElement("playerElement");
        rootElement.appendChild(player);

        Attr attr = doc.createAttribute("id");
        attr.setValue(Integer.toString(players.indexOf(p)));
        player.setAttributeNode(attr);

        Element firstname = doc.createElement("name");
        firstname.appendChild(doc.createTextNode(p.getPlayerName()));
        player.appendChild(firstname);

        // nickname elements
        Element nickname = doc.createElement("cash");
        nickname.appendChild(doc.createTextNode(Integer.toString(p.getPlayerCash())));
        player.appendChild(nickname);

        // salary elements
        Element salary = doc.createElement("location");
        salary.appendChild(doc.createTextNode(Integer.toString(p.getCurrentPlace())));
        player.appendChild(salary);
        return player;
    }

    public String getFileNameForSave()
	{
		File folder = new File("Saves");
		File[] listOfFiles = folder.listFiles();
		Integer max = 0;
		Integer counter = 0;

        assert listOfFiles != null;
        for (File listOfFile : listOfFiles) {
            doLog(Level.INFO, "File " + listOfFile.getName());

            if (listOfFile.getName().contains("_")) {
                String[] splitUnder = listOfFile.getName().split("_");
                String[] splitPoint = splitUnder[1].split("\\.");
                counter = (Integer.parseInt(splitPoint[0]) > max) ? Integer.parseInt(splitPoint[0]) : max;
            }
        }
		String fileName = "save_" + (++counter);

        doLog(Level.INFO, "Filenév: " + fileName);
		return fileName;
	}
}
