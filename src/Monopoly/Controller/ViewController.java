package Monopoly.Controller;

import Monopoly.Model.Players.Player;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import Monopoly.Model.Cards.PlotPropertyCard;
import Monopoly.Model.Cards.PropertyCard;
import Monopoly.Model.Cards.RailingPropertyCard;
import Monopoly.Model.Cards.UtilityPropertyCard;
import static Monopoly.Logger.LoggerClass.doLog;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

public class ViewController {

    public GameController game;

	@FXML public Pane main;
	public Pane boardPane,settingsPane,helpPane,propertyPane,communityChestPane;
    public Pane buildmain;
    private Stage buildStage;
    private Label communityDescriptionLabel;
    //main menu
    public Button newGameButton,settingsButton,helpButton,loadButton,exitButton,playerBuyHousesButton,closeCommunityChest;

    //game
    public Button dice,ingameHelpButton;
    public Label diceResult;
    public TilePane infoPane;
    public ArrayList<Label> playerCashLabels,playerNameLabels;
    public ArrayList<TilePane> propertyPanes;
    public ArrayList<TilePane> fieldList;
    public ArrayList<Button> figures;

    //settings menu
    public Label playerNameLabel,aiNumberLabel;
    public TextField playerName;
    public HBox figLayout;
    public ArrayList<ToggleButton> toggleList;
    public ToggleGroup figGroup;
    public ComboBox<Integer> aiNumber;
	public Integer playerFigureNo;
	public Boolean selectedFigure;

	//help menu
	public Label helpText;

    public Button saveButton,backButton;

	//////
	//Ideiglenes koordináta számláló
	public Label coordLabel;
	//////

	public void initialize(){

		selectedFigure = false;
		game=new GameController();
		game.initialize();
        buildmain = new Pane();
        buildStage = new Stage();
        buildStage.setScene(new Scene(buildmain,400,250));
        buildStage.setTitle("Építés");
        buildStage.setResizable(false);

        //Start nem game button
		newGameButton=new Button();
		newGameButton.setStyle("-fx-background-color:transparent");
		newGameButton.relocate(305, 205);
		newGameButton.setPrefSize(281,68);

        //Settings menu button
		settingsButton=new Button();
		settingsButton.setStyle("-fx-background-color:transparent");
		settingsButton.relocate(305, 282);
		settingsButton.setPrefSize(281,68);

        //Load menu button
        loadButton=new Button();
        loadButton.setStyle("-fx-background-color:transparent");
        loadButton.relocate(305,357);
        loadButton.setPrefSize(281,68);

        //Help menu button
		helpButton=new Button();
		helpButton.setStyle("-fx-background-color:transparent");
		helpButton.relocate(305,433);
		helpButton.setPrefSize(281,68);

        //Quit game button
		exitButton=new Button();
		exitButton.setStyle("-fx-background-color:transparent");
		exitButton.relocate(305,508);
		exitButton.setPrefSize(281,68);

        //New game button click event
		newGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {

                    game.newGame();
					gameWindow();

				}
			});

        //Settings button click event
		settingsButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {

                    settingsWindow();
				}});

        //Load button click event
        loadButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
						loadWindow();
                    }});

        //Help button click event
		helpButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {
                    helpWindow();
				}});

        //Exit button click event
		exitButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {

					//megerősítés felugró ablakban (biztos hogy kilépsz?)

					Stage stage = (Stage) exitButton.getScene().getWindow();
					stage.close();
				}
			});

		////////
		//Ideiglenes koordináta számláló és eventje
		coordLabel=new Label();
		main.addEventHandler(MouseEvent.MOUSE_MOVED,
			new EventHandler<MouseEvent>(){
				@Override
				public void handle(MouseEvent mouseEvent) {
					coordLabel.setText(Double.toString(mouseEvent.getX()) + " " + Double.toString(mouseEvent.getY()));
				}
			});
		////////

		main.getChildren().addAll(newGameButton,settingsButton,helpButton,exitButton,coordLabel, loadButton);

	}

	//loads an XML file from dialog
	public void loadWindow() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog((Stage) loadButton.getScene().getWindow());
		if ( file != null && file.getName().contains("save_") && file.getName().contains(".xml"))
		{
			ArrayList<Player> playerList = game.loadGame(file.getName());
			gameWindow();
			for ( int i = 0; i < playerList.size(); ++i )
			{
				loadStep(i, playerList.get(i).getCurrentPlace());
				for (PropertyCard pc : playerList.get(i).getPropertyList())
				{
					 loadAddPropertyRecordToPanel(i, pc.getCardName());
				}
			}
		}
	}

	private void enableMainButtons() {
        newGameButton.setVisible(true);
        settingsButton.setVisible(true);
        loadButton.setVisible(true);
        helpButton.setVisible(true);
        exitButton.setVisible(true);
    }

    private void disableMainButtons() {
        newGameButton.managedProperty().bind(newGameButton.visibleProperty());
        newGameButton.setVisible(false);
        settingsButton.managedProperty().bind(settingsButton.visibleProperty());
        settingsButton.setVisible(false);
        helpButton.managedProperty().bind(settingsButton.visibleProperty());
        loadButton.setVisible(false);
        loadButton.managedProperty().bind(loadButton.visibleProperty());
        helpButton.setVisible(false);
        exitButton.managedProperty().bind(exitButton.visibleProperty());
        exitButton.setVisible(false);
    }

	public void gameWindow(){

        disableMainButtons();

		boardPane=new Pane();
		boardPane.setPrefSize(600,600);
		boardPane.setId("board");

        communityChestPane = new Pane();
        communityChestPane.setPrefSize(350, 350);
        communityChestPane.setId("communityChest");
        communityChestPane.relocate(120, 200);
        communityChestPane.setVisible(false);
        communityDescriptionLabel = new Label("");
        communityDescriptionLabel.setId("communitylabel");
        communityDescriptionLabel.relocate(30, 60);
        closeCommunityChest = new Button();
        closeCommunityChest.setStyle("-fx-background-color:transparent");
        closeCommunityChest.setPrefSize(100,37);
        closeCommunityChest.relocate(121, 183);
        closeCommunityChest.setText("");
        closeCommunityChest.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        closeCommunityChestPane();
                    }
                });
        communityChestPane.getChildren().addAll(closeCommunityChest, communityDescriptionLabel);

        //Right pane for player information
		infoPane=new TilePane();
		infoPane.setPrefSize(303,600);
        infoPane.setMaxSize(303,600);
		infoPane.relocate(600,0);
		infoPane.setId("info");

        //Save game button
		saveButton=new Button();
		saveButton.setStyle("-fx-background-color:transparent");
		saveButton.relocate(614, 557);
		saveButton.setPrefSize(98,40);

        //Exit to menu button
		backButton=new Button();
		backButton.setStyle("-fx-background-color:transparent");
		backButton.relocate(707, 557);
		backButton.setPrefSize(98,40);

        //Ingame help button
		ingameHelpButton=new Button();
		ingameHelpButton.setStyle("-fx-background-color:transparent");
		ingameHelpButton.relocate(812, 557);
		ingameHelpButton.setPrefSize(98,40);

        //Dice button
		dice=new Button();
		dice.setId("dice");
		dice.setPrefSize(90, 90);
		dice.relocate(100, 100);

        diceResult=new Label();
        diceResult.relocate(200, 100);

        //Setting up fields
        fieldList = new ArrayList<TilePane>();
        Integer j=0;
        for( int i = 0; i < 40; ++i )
        {
            final TilePane t=new TilePane();
            t.setPrefSize(48, 74);
            t.setPrefColumns(2);
            t.setPrefTileWidth(24);
            t.setPrefTileHeight(24);
            t.setId("" + j + "");
            t.setStyle("-fx-background-color:transparent");

            if(j==0){
                t.setPrefSize(74, 74);
                t.relocate(516,516);

                //Setting up the players' figures and the infoPane for the players' properties
                figures=new ArrayList<Button>();
                propertyPanes=new ArrayList<TilePane>();
                playerCashLabels=new ArrayList<Label>();
                playerNameLabels=new ArrayList<Label>();
                for(Integer f=0;f<game.getPlayerNumber()+1;f++){

                    Integer currentFigureNo;
                    if(selectedFigure)
                    {
                        if(f==0)
                        {
                            currentFigureNo = playerFigureNo + 1;
                        }
                        else
                        {
                            currentFigureNo = (playerFigureNo+f) % 8 + 1;
                        }
                    }
                    else
                    {
                        currentFigureNo = f + 1;
                    }
                    {
                        Button b=new Button();
                        b.setStyle("-fx-background-color:rgb(128,255,0);");
                        b.setId("fig"+currentFigureNo);
                        figures.add(b);
                        t.getChildren().add(b);
                    }

                    //pane for the player's properties
                    Pane playerPane=new Pane();
                    playerPane.setTranslateX(10);
                    playerPane.setMaxSize(295, 136);
                    playerPane.setMinSize(295,136);


                    //player's name label
                    Label name=new Label(game.getPlayerName(f));
                    name.setFont(Font.font("Arial", 18));
                    name.setPrefSize(295, 20);
                    name.setMaxSize(295, 20);
                    playerNameLabels.add(name);

                    //player's cash label
                    Label cash=new Label("Pénz: $ "+game.getPlayerCash(f));
                    cash.setFont(Font.font("Arial", 18));
                    cash.setPrefSize(295, 20);
                    cash.setMaxSize(295,20);
                    cash.setTranslateY(20);
                    playerCashLabels.add(cash);

                    //player's figure on info sheet

                    Button playersFigureButton = new Button();
                    playersFigureButton.setStyle("-fx-background-color:rgb(128,255,0);");
                    playersFigureButton.setId("fig" + currentFigureNo);
                    playersFigureButton.relocate(250, 0);
                    playersFigureButton.setPrefSize(40, 40);

                    Button showPropertiesButton = new Button("TERÜLETEK");
                    showPropertiesButton.setId("propbutton");
                    showPropertiesButton.setPrefSize(145,20);
                    showPropertiesButton.setMaxSize(145, 20);
                    showPropertiesButton.setTranslateY(106);
                    final Integer currentPlayerNumber = f;
                    showPropertiesButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                            new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    showPlayerProperty(currentPlayerNumber);
                                }
                            });
                    //player's properties
                    TilePane propertyPane=new TilePane();
                    propertyPane.setOrientation(Orientation.VERTICAL);
                    propertyPane.setPrefSize(295, 66);
                    propertyPane.setMaxHeight(66);
                    propertyPane.setTranslateY(40);
                    propertyPane.setPrefTileWidth(70);
                    propertyPane.setStyle("-fx-background-color:rgb(232,233,235);");
                    propertyPanes.add(propertyPane);

                    if (f == 0)
                    {
                        playerBuyHousesButton = new Button("HÁZAK VÁSÁRLÁSA");
                        playerBuyHousesButton.setPrefSize(145,20);
                        playerBuyHousesButton.setMaxSize(145, 20);
                        playerBuyHousesButton.setTranslateY(106);
                        playerBuyHousesButton.setTranslateX(150);
                        playerBuyHousesButton.setDisable(false);
                        playerBuyHousesButton.setId("propbutton");
                        playerBuyHousesButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                                new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        BuildWindow();
                                    }
                                });
                        playerPane.getChildren().addAll(name, cash, propertyPane,showPropertiesButton,playerBuyHousesButton,playersFigureButton);
                    }
                    else
                        playerPane.getChildren().addAll(name, cash, propertyPane,showPropertiesButton,playersFigureButton);


                    infoPane.getChildren().add(playerPane);
                }
            }
            else if(j==10){
                t.setPrefSize(74,74);
                t.relocate(10,516);
            }
            else if(j==20){
                t.setPrefSize(74,74);
                t.relocate(10,10);
            }
            else if(j==30){
                t.setPrefSize(74,74);
                t.relocate(516,10);
            }
            else{
                if(j<10){
                    t.setPrefSize(48,74);
                    t.relocate(468-((j%10)-1)*48,516);
                }
                else if(j>10&&j<20){
                    t.setPrefSize(74,48);
                    t.relocate(10,468-((j%10)-1)*48);
                }
                else if(j>20&&j<30){
                    t.setPrefSize(48,74);
                    t.relocate(84+((j%10)-1)*48,10);

                }
                else{
                    t.setPrefSize(74,48);
                    t.relocate(516,84+((j%10)-1)*48);
                }
            }

            //Click on field event
            t.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent e) {

                            //Open card in new window
                            //with the owner's name or a Buy button on it
                            //(Buy->add to properties)
//                            Button prop=new Button(game.getField(t.getId()));
//                            prop.setFont(Font.font("Arial",6));
//                            prop.setPrefSize(70,11);
//                            prop.setMaxSize(70, 11);
//                            propertyPanes.get(game.currentPlayer()).getChildren().add(prop);
							if (propertyPane != null)propertyPane.setVisible(false);
							propertyWindow(game.getField(t.getId()), false);
                        }
                    });

            fieldList.add(t);
            boardPane.getChildren().add(t);
            j++;
        }

        boardPane.getChildren().addAll(dice, diceResult,communityChestPane);
		main.getChildren().addAll(boardPane, infoPane, saveButton, backButton, ingameHelpButton);
		coordLabel.toFront();

        //Dice click event
        dice.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                        diceRoll();
                    }
                });

        //Save button click event
		saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {

					//játékállás mentése IDE
					game.saveGame();

					enableMainButtons();

					main.getChildren().remove(boardPane);
					main.getChildren().remove(infoPane);
					main.getChildren().remove(saveButton);
					main.getChildren().remove(backButton);
					main.getChildren().remove(ingameHelpButton);
				}
			});

        //Back button click event
		backButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {

					//megerősítés felugró ablakban (kilépés mentés nélkül)

					enableMainButtons();

					main.getChildren().remove(boardPane);
					main.getChildren().remove(infoPane);
					main.getChildren().remove(saveButton);
					main.getChildren().remove(backButton);
					main.getChildren().remove(ingameHelpButton);

                    game.exitGame();
				}
			});

        //Help button click event
		ingameHelpButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {

					helpWindow();
				}
			});
	}

    private void closeCommunityChestPane() {
        communityChestPane.setVisible(false);
    }

    public void diceRoll(){
		if (propertyPane != null)propertyPane.setVisible(false);
        communityChestPane.setVisible(false);
        Integer currentPlayer = game.currentPlayer();
        Integer currentField = game.getCurrentPlace(currentPlayer);
        Pair<Boolean,Integer> currentPair = game.rollTheDice();
        Integer nextField = game.getCurrentPlace(currentPlayer);
        if(nextField != null && !nextField.equals(currentField)){
            step(currentPlayer,currentField,nextField);
        }

		propertyWindow(game.getField("" + nextField), true);
        communityChestWindow(game.getField(""+nextField));
        if (currentPair.getKey())
        {
			addPropertyRecordToPanel(currentPlayer, nextField);
        }

		diceResult.setText(playerNameLabels.get(currentPlayer).getText()+"'s throw result: "+currentPair.getValue());
    }

	private void addPropertyRecordToPanel(Integer currentPlayer, Integer nextField) {
		Button prop=new Button(game.getField(Integer.toString(nextField)));
		prop.setFont(Font.font("Arial", 8));
		prop.setPrefSize(70,11);
		prop.setMaxSize(70, 11);
		propertyPanes.get(currentPlayer).getChildren().add(prop);
	}

	private void loadAddPropertyRecordToPanel(Integer currentPlayer, String propertyName) {
		Button prop=new Button(propertyName);
		prop.setFont(Font.font("Arial", 8));
		prop.setPrefSize(70,11);
		prop.setMaxSize(70, 11);
		propertyPanes.get(currentPlayer).getChildren().add(prop);
	}

	public void loadStep(Integer player, Integer currentField)
	{
		fieldList.get(0).getChildren().remove(player);
		fieldList.get(currentField).getChildren().add(figures.get(player));
	}

    public void step(Integer player,Integer currentField,Integer nextField){

        fieldList.get(currentField).getChildren().remove(player);
        fieldList.get(nextField).getChildren().add(figures.get(player));
		for(int playerCounter = 0; playerCounter < game.playerList.size(); ++playerCounter )
		{
			playerCashLabels.get(playerCounter).setText("Pénz: $ "+game.getPlayerCash(playerCounter));
		}
    }

	public void settingsWindow(){

        disableMainButtons();

		settingsPane=new Pane();
		settingsPane.setPrefSize(903, 600);
		settingsPane.setId("settings");

        //Filed for players name (with default value)
		playerNameLabel=new Label("Játékos neve:");
		playerNameLabel.relocate(350,100);
		playerName=new TextField("Játékos01");
		playerName.relocate(450,100);

        //Layout for figure buttons
		figLayout=new HBox();
		figLayout.relocate(65,200);
		figLayout.setSpacing(10);
		figGroup=new ToggleGroup();

        //Buttons for choosing player's figure
		toggleList=new ArrayList<ToggleButton>();
        for(Integer i=1;i<9;i++){
            final ToggleButton t=new ToggleButton();
            t.setPrefSize(90,90);
            t.setId("fig" + i);
            t.setToggleGroup(figGroup);
            toggleList.add(t);
            figLayout.getChildren().add(t);
			t.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					@Override public void handle(MouseEvent e) {
						playerFigureNo=Integer.parseInt(t.getId().substring(t.getId().length()-1))-1;
						selectedFigure=true;
					}
				});
        }

        //ComboBox for choosing number of AI players
		aiNumberLabel=new Label("Gépi játékosok:");
		aiNumberLabel.relocate(350,350);
		aiNumber=new ComboBox<Integer>();
		aiNumber.getItems().addAll(1,2,3);
        aiNumber.getSelectionModel().selectFirst();
		aiNumber.relocate(450,350);

        //Save settings button
		saveButton=new Button();
		saveButton.setStyle("-fx-background-color:transparent");
		saveButton.relocate(219,470);
		saveButton.setPrefSize(233,75);

        //Back to main menu button
		backButton=new Button();
		backButton.setStyle("-fx-background-color:transparent");
		backButton.relocate(450,470);
		backButton.setPrefSize(233, 75);

		settingsPane.getChildren().addAll(playerNameLabel,playerName,figLayout,aiNumberLabel,aiNumber,saveButton,backButton);
		main.getChildren().add(settingsPane);
		coordLabel.toFront();

        //Save button click event
		saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {

					//beállítások mentése IDE
					game.saveSettings(playerName.getText(),aiNumber.getValue());

					enableMainButtons();

					main.getChildren().remove(settingsPane);
				}
			});

        //Back button click event
		backButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {

					enableMainButtons();

					main.getChildren().remove(settingsPane);
				}
			});

	}

	public void helpWindow(){

        disableMainButtons();

		helpPane=new Pane();
		helpPane.setPrefSize(903, 600);
		helpPane.setId("help");

        //Text of the help menu
		helpText=new Label("Monopoly alkalmazás működése\n" +
                "-----\n" +
                "\n" +
                "Beállítások\n" +
                "-----\n" +
                "Játék megkezdése előtt lehetőség van a játékos nevének megadására, a bábu kiválasztására, és a gépi játékosok számának beállítására. (Beállítások nélkül is elindíthatjuk a játékot, ilyen esetben alapértelmezett értékek állítódnak be.) Ennek lépései:\n" +
                "1. Lépjünk a \"Beállítások\" képernyőre\n" +
                "2. Írjuk be nevünket a \"Játékos neve\" szövegmezőbe\n" +
                "3. Kattintással jelöljük ki a választott bábut\n" +
                "4. Legördülő listából válasszuk ki a gépi játékosok számát (alapértelmezetten 1)\n" +
                "5. A beállítások mentéséhez kattintsunk a \"Mentés\" gombra, ekkor visszajutunk a főmenübe\n" +
                "(6. Ha nem szeretnénk menteni a beállításokat, kattintsunk a \"Vissza\" gombra a főmenübe lépéshez)\n"+
                "Betöltés\n" +
                "-----\n" +
                "A betöltés gombra kattintva egy előzőleg mentett játékállást tölthetünk be és folytathatunk.\n" +
                "\n" +
                "Játék\n" +
                "-----\n" +
                "A játék indításához kattintsunk a főmenüben az \"Új játék\" gombra, ekkor betöltődik a játéktábla és a játékosok adatait/tulajdonait megjelenítő sáv.\n" +
                "Lépés:\n" +
                "A játékban a táblán lévő dobókockára kattintva dobhatunk (és léphetünk).\n" +
                "Telkek:\n" +
                "A telkek információinak megjelenítéséhez vagy vásárláshoz az adott telekhez tartozó mezőre kell kattintatnunk. Ekkor felugró ablakban jelennek meg a megfelelő információk és a lehetséges lépések (pl. vásárlás).\n" +
                "Tulajdonok:\n" +
                "A játékosok tulajdonai a jobb oldali sávban jelennek meg, az egyes tulajdonokhoz tartozó információk pedig rájuk kattintva tekinthetők meg felugró ablakban.\n" +
                "Mentés:\n" +
                "A játékállás mentésére a jobb alsó sarokban lévő \"Mentés\" gombra kattintva van lehetőség.\n" +
                "Kilépés:\n" +
                "A játék bezárására, és a főmenübe lépésre a jobb alsó sarokban lévő \"Kilépés\" gombbal van lehetőség.");
		helpText.relocate(50, 10);
		helpText.setPrefWidth(800);
		helpText.setWrapText(true);

        //Back to main menu button
		backButton=new Button();
		backButton.setStyle("-fx-background-color:transparent");
		backButton.relocate(335,525);
		backButton.setPrefSize(233, 75);

		helpPane.getChildren().addAll(helpText,backButton);
		main.getChildren().add(helpPane);
		coordLabel.toFront();

        //Back button click event
		backButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
			new EventHandler<MouseEvent>() {
				@Override public void handle(MouseEvent e) {

					if(!main.getChildren().contains(boardPane))
					{
						enableMainButtons();
					}

					main.getChildren().remove(helpPane);
				}
			});

	}

	public void propertyWindow(String propertyName, boolean isAfterStep)
	{
        PropertyCard currentCard = game.getPropertyByNameFromBank(propertyName);
        if (boardPane.isVisible() && (currentCard != null))
		{
			propertyPane = new Pane();
			propertyPane.setPrefSize(340,300);
			propertyPane.relocate(140,200);
			propertyPane.setId("prop");
			Pane titlePane = new Pane();
			titlePane.setPrefSize(296,29);
			titlePane.relocate(20,4);
			Label propertyNameLabel = new Label();
			titlePane.setStyle("-fx-background-color: white");
			propertyNameLabel.setStyle("-fx-font: bold italic 20pt \"Arial\"");
			propertyNameLabel.setText(currentCard.getCardName());
			titlePane.getChildren().add(propertyNameLabel);


			switch (currentCard.getPropertyType())
			{
				case SIMPLE:
				{
					PlotPropertyCard currentCardSimple = (PlotPropertyCard)currentCard;
					titlePane.setStyle("-fx-background-color: " + currentCardSimple.getColorTypeString());
					{
						Label rent, house1, house2, house3, house4, hotel, mortgage, houseprise, hotelprise;
						rent = new Label();
						rent.setStyle("-fx-font: bold 16pt \"Arial\"");
						rent.setText("Rent: "+currentCardSimple.getRentalValueNoHouses() + "$");
						rent.relocate(138,54);
						propertyPane.getChildren().add(rent);

						house1 = new Label();
						house1.setText("With one house:      "+currentCardSimple.getRentalValueOneHouse() + "$");
						house1.setStyle("-fx-font: bold 16pt \"Arial\"");
						house1.relocate(80,83);
						propertyPane.getChildren().add(house1);

						house2 = new Label();
						house2.setText("With two houses:    "+currentCardSimple.getRentalValueTwoHouses() + "$");
						house2.setStyle("-fx-font: bold 16pt \"Arial\"");
						house2.relocate(80,102);
						propertyPane.getChildren().add(house2);

						house3 = new Label();
						house3.setText("With three houses:  "+currentCardSimple.getRentalValueThreeHouses() + "$");
						house3.setStyle("-fx-font: bold 16pt \"Arial\"");
						house3.relocate(80,121);
						propertyPane.getChildren().add(house3);

						house4 = new Label();
						house4.setText("With four houses:    "+currentCardSimple.getRentalValueFourHouses() + "$");
						house4.setStyle("-fx-font: bold 16pt \"Arial\"");
						house4.relocate(80,140);
						propertyPane.getChildren().add(house4);

						hotel = new Label();
						hotel.setText("With Hotel:              "+currentCardSimple.getRentalValueHotel() + "$");
						hotel.setStyle("-fx-font: bold 16pt \"Arial\"");
						hotel.relocate(80,159);
						propertyPane.getChildren().add(hotel);

						mortgage = new Label();
						mortgage.setText("Mortgage:   "+currentCardSimple.getMortgageValue() + "$");
						mortgage.setStyle("-fx-font: bold 16pt \"Arial\"");
						mortgage.relocate(100,188);
						propertyPane.getChildren().add(mortgage);

						houseprise = new Label();
						houseprise.setText("House costs: "+currentCardSimple.getHouseCost() + "$");
						houseprise.setStyle("-fx-font: bold 16pt \"Arial\"");
						houseprise.relocate(100,207);
						propertyPane.getChildren().add(houseprise);

						hotelprise = new Label();
						hotelprise.setText("Hotel costs: "+currentCardSimple.getHotelCost() + "$");
						hotelprise.setStyle("-fx-font: bold 16pt \"Arial\"");
						hotelprise.relocate(100,226);
						propertyPane.getChildren().add(hotelprise);
					}
					break;
				}
				case RAILING:
				{
					Pane railPicPane = new Pane();
					railPicPane.setPrefSize(56,41);
					railPicPane.relocate(140,50);
					railPicPane.setId("railproppic");
					propertyPane.getChildren().add(railPicPane);
					RailingPropertyCard currentCardRailing = (RailingPropertyCard) currentCard;
					{
						Label rent,with2,with3,with4,cost1,cost2,cost3,cost4, mortgage;
						rent = new Label();
						rent.setStyle("-fx-font: bold 16pt \"Arial\"");
						rent.setText("Rent: ");
						rent.relocate(60,100);
						propertyPane.getChildren().add(rent);

						with2 = new Label();
						with2.setText("With two railroad owned: ");
						with2.setStyle("-fx-font: bold 16pt \"Arial\"");
						with2.relocate(60,120);
						propertyPane.getChildren().add(with2);

						with3 = new Label();
						with3.setText("With three railroad owned: ");
						with3.setStyle("-fx-font: bold 16pt \"Arial\"");
						with3.relocate(60,140);
						propertyPane.getChildren().add(with3);

						with4 = new Label();
						with4.setText("With four railroad owned: ");
						with4.setStyle("-fx-font: bold 16pt \"Arial\"");
						with4.relocate(60,160);
						propertyPane.getChildren().add(with4);

						cost1 = new Label();
						cost1.setText(""+currentCardRailing.getValueOnePropertyOwned() + "$");
						cost1.setStyle("-fx-font: bold 16pt \"Arial\"");
						cost1.relocate(280,100);
						propertyPane.getChildren().add(cost1);

						cost2 = new Label();
						cost2.setText(""+currentCardRailing.getValueTwoPropertiesOwned() + "$");
						cost2.setStyle("-fx-font: bold 16pt \"Arial\"");
						cost2.relocate(280,120);
						propertyPane.getChildren().add(cost2);

						cost3 = new Label();
						cost3.setText(""+currentCardRailing.getValueThreePropertiesOwned() + "$");
						cost3.setStyle("-fx-font: bold 16pt \"Arial\"");
						cost3.relocate(280,140);
						propertyPane.getChildren().add(cost3);

						cost4 = new Label();
						cost4.setText(""+currentCardRailing.getValueFourPropertiesOwned() + "$");
						cost4.setStyle("-fx-font: bold 16pt \"Arial\"");
						cost4.relocate(280,160);
						propertyPane.getChildren().add(cost4);

						mortgage = new Label();
						mortgage.setText("Mortgage:   "+currentCardRailing.getMortgageValue() + "$");
						mortgage.setStyle("-fx-font: bold 16pt \"Arial\"");
						mortgage.relocate(120,190);
						propertyPane.getChildren().add(mortgage);

					}
					break;
				}
				case UTILITY:
				{
					UtilityPropertyCard currentCardUtility = (UtilityPropertyCard) currentCard;
					{
						Label company, rent, with2, mortgage;

						company = new Label();
						company.setStyle("-fx-font: bold 24pt \"Arial\"");
						company.setText("UTILITY");
						company.relocate(120, 50);
						propertyPane.getChildren().add(company);

						rent = new Label();
						rent.setStyle("-fx-font: bold 16pt \"Arial\"");
						rent.setText("If one \"UTILITY\" is owned, rent is \n" + currentCardUtility.getValueOnePropertyOwned() + " times the amount shown on the dice");
						rent.relocate(20, 100);
						propertyPane.getChildren().add(rent);

						with2 = new Label();
						with2.setText("If both \"UTILITY\" is owned, rent is \n" + currentCardUtility.getValueTwoPropertiesOwned() + " times the amount shown on the dice");
						with2.setStyle("-fx-font: bold 16pt \"Arial\"");
						with2.relocate(20,160);
						propertyPane.getChildren().add(with2);

						mortgage = new Label();
						mortgage.setText("Mortgage: "+currentCardUtility.getMortgageValue() + "$");
						mortgage.setStyle("-fx-font: bold 16pt \"Arial\"");
						mortgage.relocate(120,210);
						propertyPane.getChildren().add(mortgage);

					}
					break;
				}
			}
			if (isAfterStep)
			{
                final String currentFieldName = game.getField(""+game.getCurrentPlace(game.getPlayerNoWhoThrown()));
				if (game.hasPropertyCard(currentCard) == null && (game.getPlayerNoWhoThrown() == 0) && game.canPlayerBuyProperty(currentFieldName))
				{
                    final Button buyButton = new Button("MEGVÁSÁRLÁS");
                    buyButton.setId("propbutton");
                    buyButton.setPrefSize(110, 30);
                    buyButton.relocate(60, 250);
                    buyButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                            new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    Button prop=new Button(currentFieldName);//game.getField(""+game.getCurrentPlace((game.currentPlayer() - 1)%game.getPlayerNumber())));
                                    prop.setFont(Font.font("Arial", 8));
                                    prop.setPrefSize(70, 11);
                                    prop.setMaxSize(70, 11);
                                    //propertyPanes.get((game.currentPlayer() - 1)%game.getPlayerNumber()).getChildren().add(prop);

                                    if (game.buyPropertyForPlayer(currentFieldName))
                                    {
                                        buyButton.setVisible(false);
                                        propertyPanes.get(game.getPlayerNoWhoThrown()).getChildren().add(prop);
                                        doLog(Level.INFO, "A " + game.playerList.get(game.getPlayerNoWhoThrown()).getPlayerName() + " nevű játékos megvette a telket.");
                                    }
                                    refreshHumanPlayerMoney();
                                }
                            });
                    propertyPane.getChildren().add(buyButton);

                }
			}

			Button propExitButton = new Button();
			propExitButton.setId("propbutton");
			propExitButton.setPrefSize(110, 30);
			propExitButton.relocate(190, 250);
			propExitButton.setText("KILÉPÉS");
			propExitButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
						propertyPane.setVisible(false);
					}
				});
			propertyPane.getChildren().add(propExitButton);
			propertyPane.getChildren().add(titlePane);
			boardPane.getChildren().add(propertyPane);
		}

	}

    private void communityChestWindow(String propertyName)
    {
        if (propertyName.substring(0,propertyName.length()-1).equals("Community_Chest_"))
        {
            communityDescriptionLabel.setText(game.getLastCommunityCard());
            communityChestPane.setVisible(true);
        }
        if(propertyName.substring(0,propertyName.length()-1).equals("Chance_"))
        {
            communityDescriptionLabel.setText(game.getLastChanceCard());
            communityChestPane.setVisible(true);
        }

    }

    private void showPlayerProperty(Integer playerNo)
    {
        ArrayList<PropertyCard> playersProperties = game.getPlayerPropertyCards(playerNo);
        Stage st = new Stage();
        Pane propertiesWindowPane = new Pane();
        propertiesWindowPane.setPrefSize(800,700);
        st.setScene(new Scene(propertiesWindowPane,800,700));
        st.setTitle(game.getPlayerName(playerNo) + " Területei:");
        int i = 0;

        for (PropertyCard pc : playersProperties)
        {
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
            p.relocate((i % 5) * 135,(i / 5) * 215);
            propertiesWindowPane.getChildren().add(p);
            i++;
        }
        st.show();
    }

    private void BuildWindow()
    {
        try
        {
            buildmain.getChildren().clear();
            buildStage.show();
        }
        catch (Throwable ex)
        {
            doLog(Level.WARNING, ex.getMessage());
        }
        ArrayList<PlotPropertyCard.Colour_Type> buildableColorList = game.getHumanPlayerBuildableColors();
        for (int i = 0; i < buildableColorList.size();i++)
        {

            Pane p = new Pane();
            p.setStyle("-fx-background-color: " + getColorTypeStringByType(buildableColorList.get(i)));
            doLog(Level.INFO, getColorTypeStringByType(buildableColorList.get(i)));
            final PlotPropertyCard.Colour_Type currentColor = buildableColorList.get(i);
            p.setPrefSize(90,30);
            p.relocate((i % 4)*95 + 5,(i / 4)*90 + 10);
            if (!game.isHotelOnColor(currentColor))
            {
                doLog(Level.INFO, "in:"+getColorTypeStringByType(buildableColorList.get(i)));
                if (game.isFourHouseOnColor(currentColor))
                {
                    Button buyHotelButton = new Button("HOTELT VESZ");
                    buyHotelButton.relocate((i % 4)*95 + 5,(i / 4)*90 + 40);
                    buyHotelButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                            new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent mouseEvent) {
                                    if (game.buyHotelForHumanPlayer(currentColor))
                                        refreshHumanPlayerMoney();
                                }
                            });
                    p.setPrefSize(90,30);
                    buildmain.getChildren().addAll(p,buyHotelButton);
                }
                else
                {
                    Button buyHouseButton = new Button("HÁZAT VESZ");
                    buyHouseButton.relocate((i % 4)*95 + 5,(i / 4)*90 + 40);
                    buyHouseButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                if (game.buyHouseForHumanPlayer(currentColor))
                                    refreshHumanPlayerMoney();
                            }
                        });
                    p.setPrefSize(90,30);
                    buildmain.getChildren().addAll(p,buyHouseButton);
                }
            }
        }
    }
    private String getColorTypeStringByType(PlotPropertyCard.Colour_Type inColorType){
        switch (inColorType)
        {
            case BROWN: return "brown";
            case RED: return "red";
            case LIGHT_BLUE: return "lightblue";
            case BLUE: return "blue";
            case YELLOW: return "yellow";
            case PURPLE: return "purple";
            case GREEN: return "green";
            case ORANGE: return "orange";
            default:
                break;
        }
        return "";
    }

    public void refreshHumanPlayerMoney()
    {
        playerCashLabels.get(0).setText("Pénz: $ "+game.getPlayerCash(0));
    }
}
