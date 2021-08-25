
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import game.Game;
import game.Player;
import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    Label[] scoreLabels = new Label[5];
    Button[] buttonRack = new Button[7];
    ArrayList<Player> playerArray = new ArrayList<>();
    TextField[][] squares = new TextField[15][15];
    String transparentBackground = "-fx-background-image: url(https://rozup.ir/up/98ax1/Pictures/1/tabiat.1200.jpg)";

    Game game;
    private boolean p3Flag = false;
    private boolean p4Flag = false;
    int numTurns = 0;
    int roundNumber;
    int i = 0;

    public void start(final Stage primaryStage) {

        /*
         *in here we made a Codes for the first page of app
         *
         */

        primaryStage.setTitle("GameIsMyLife");
        primaryStage.getIcons().add(new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-84QJaofvWOR-Y_pUqut53hJ_-tkYiRBbWA&usqp=CAU"));
        Button beginButton = new Button();
        beginButton.setText("Start the Game !");
        beginButton.setStyle("-fx-font-family: \"Javanese Text\"");
        changeTexts(beginButton);
        Label welcomeLabel = new Label("Let's play together !1");

        welcomeLabel.setStyle("-fx-font-family: 'Javanese Text'");
        welcomeLabel.setFont(new Font("Cambria", 75));
        Alert alert2 = new Alert(AlertType.INFORMATION);
        beginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                beginButtonClicked(primaryStage);
                alert2.setHeaderText("***********Before starting Please read the LOGIC of Game***************");
                alert2.setContentText("#####  let's play ####");
                alert2.showAndWait();
            }
        });

        StackPane root = new StackPane();
        root.setStyle(transparentBackground);
        root.getChildren().addAll(beginButton, welcomeLabel);
        root.setPadding(new Insets(100, 0, 0, 0));
        StackPane.setAlignment(beginButton, Pos.CENTER);
        StackPane.setAlignment(welcomeLabel, Pos.TOP_CENTER);
        primaryStage.setScene(new Scene(root, 800, 800, Color.LIGHTSKYBLUE));
        primaryStage.show(); //*****
    }

    /*
     *in here we made a page that when we clicked it Open New Page of App
     */
    public void beginButtonClicked(final Stage primaryStage) {
        StackPane sp = new StackPane();
        sp.setStyle("-fx-background-image: url(https://cdn.wallpapersafari.com/38/78/OZVPnY.jpg)");
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-image: url(https://cdn.wallpapersafari.com/38/78/OZVPnY.jpg)");
        Button letsPlayBtn = new Button("Let's Play !");

        Label firstPlayerLabel = new Label("PlayerNameIs:");
        final TextField firstPlayerTxt = new TextField();
        firstPlayerLabel.setStyle("-fx-font-family: 'Javanese Text'");
        Label secondPlayerLabel = new Label("PlayerNameIS:");
        final TextField secondPlayerTxt = new TextField();
        secondPlayerLabel.setStyle("-fx-font-family: 'Javanese Text'");

        Label errorLabel = new Label("");
        Label l = new Label("Please enter player names:");

        changeTexts(firstPlayerLabel);
        changeTexts(firstPlayerTxt);
        changeTexts(secondPlayerLabel);
        changeTexts(secondPlayerTxt);

        changeTexts(l);
        changeTexts(letsPlayBtn);

        GridPane.setMargin(firstPlayerLabel, new Insets(10));
        GridPane.setMargin(firstPlayerTxt, new Insets(10));
        GridPane.setMargin(secondPlayerLabel, new Insets(10));
        GridPane.setMargin(secondPlayerTxt, new Insets(10));

        GridPane.setMargin(letsPlayBtn, new Insets(10));
        GridPane.setMargin(errorLabel, new Insets(10));

        grid.add(l, 3, 0);
        grid.add(firstPlayerLabel, 2, 1);
        grid.add(firstPlayerTxt, 3, 1);
        grid.add(secondPlayerLabel, 2, 2);
        grid.add(secondPlayerTxt, 3, 2);

        grid.add(errorLabel, 3, 3);
        grid.add(letsPlayBtn, 3, 5);
        GridPane.setColumnSpan(errorLabel, 2);
        GridPane.setColumnSpan(letsPlayBtn, 2);


        /*
         * in here we check the inputs of names of player ;
         */
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(firstPlayerTxt.textProperty(), secondPlayerTxt.textProperty()
                );
            }

            @Override
            protected boolean computeValue() {
                return firstPlayerTxt.getText().isEmpty() || secondPlayerTxt.getText().isEmpty();
            }
        };

        letsPlayBtn.disableProperty().bind(bb);

        /*
         * get info and Stored them in an array;
         */
        letsPlayBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                game = Game.getInstance();

                int numPlayers = getNumPlayers(firstPlayerTxt, secondPlayerTxt);

                if (numPlayers == 3) {
                    p3Flag = true;
                }
                if (numPlayers == 4) {
                    p4Flag = true;
                }

                while (numPlayers > 0) {
                    playerArray.add(new Player());
                    numPlayers--;
                }

                playerArray.get(0).setName(firstPlayerTxt.getText());
                playerArray.get(1).setName(secondPlayerTxt.getText());
                playerArray.get(0).setPlayerSet(game.getTileSet().getWholeSet());
                playerArray.get(1).setPlayerSet(game.getTileSet().getWholeSet());

                if (p3Flag) {

                    playerArray.get(2).setPlayerSet(game.getTileSet().getWholeSet());
                }
                if (p4Flag) {

                    playerArray.get(2).setPlayerSet(game.getTileSet().getWholeSet());
                    playerArray.get(3).setPlayerSet(game.getTileSet().getWholeSet());
                }

                playTurn(primaryStage, playerArray.get(0));
            }
        });

        grid.setAlignment(Pos.CENTER);
        sp.getChildren().add(grid);
        StackPane.setAlignment(grid, Pos.BOTTOM_RIGHT);
        primaryStage.setScene(new Scene(sp, 800, 800, Color.LIGHTSKYBLUE));
        primaryStage.show();
    }

    // In here we get char that enter in the board and will use it ;
    public char[][] saveNewBoard() {
        char[][] newBoard = new char[15][15];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (!squares[i][j].getText().isEmpty()) {
                    char temp = squares[i][j].getText().charAt(0);
                    newBoard[i][j] = temp;
                } else {
                    newBoard[i][j] = ' ';
                }
            }
        }
        return newBoard;
    }

    // graphic board in java '
    public void createJavaFXBoard(GridPane gp) {
        System.setProperty("prism.text", "t2k");
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                final int finalI = i;
                final int finalJ = j;
                squares[i][j] = new TextField("");
                squares[i][j].setPrefSize(30, 30);
                squares[i][j].textProperty().addListener((ov, oldValue, newValue) ->
                        squares[finalI][finalJ].setText(newValue.toUpperCase())
                );
                gp.add(squares[i][j], i, j);
            }
        }
    }

    public void createButtons(Player p, HBox bottomRack, int numTurns) {
        ArrayList<Tile> playerArr = p.getPlayerSet();
        for (int i = 0; i < 7; i++) {
            if (numTurns == 1) {
                buttonRack[i] = new Button();
            }

            buttonRack[i].wrapTextProperty().setValue(true);
            if (playerArr.get(i).getLetter() == '_' || playerArr.get(i).getLetter() == ' ') {
                buttonRack[i].setText(Character.toString(playerArr.get(i).getLetter()).toUpperCase());
                buttonRack[i].setMinWidth(50);
                buttonRack[i].setMinHeight(50);
            } else if (playerArr.get(i).getLetter() != ' ') {
                buttonRack[i].setText(Character.toString(playerArr.get(i).getLetter()).toUpperCase() + " (" + (game.getTileSet().getLetterValue(playerArr.get(i).getLetter())) + ")");
                buttonRack[i].setMinWidth(50);
                buttonRack[i].setMinHeight(50);
            }
            if (numTurns == 1) {
                bottomRack.getChildren().add(buttonRack[i]);
            }
        }
    }


    // in here wd  made a Func for turn of players
    public void playTurn(Stage primaryStage, Player p) {
        numTurns++;
        roundNumber = 1;


        HBox bottomRack = new HBox();
        bottomRack.setAlignment(Pos.CENTER);
        bottomRack.setPadding(new Insets(5, 20, 5, 20));
        Button makeMoveButton = new Button("Move It baby!!");
        Button passButton = new Button("oh I can't!");
        makeMoveButton.setAlignment(Pos.CENTER_RIGHT);

        Label errors = new Label();
        GridPane gp = new GridPane();

        VBox scoreBox = createScoreBoard();
        Label topHeading = new Label("It's " + p.getName() + "'s Turn!");
        topHeading.setAlignment(Pos.TOP_CENTER);
        Label roundNumberLabel = new Label("turn's: " + roundNumber);
        roundNumberLabel.setStyle("-fx-font-size: 20");
        roundNumberLabel.setAlignment(Pos.CENTER_RIGHT);
        changeTexts(topHeading);
        gp.setPadding(new Insets(50, 50, 25, 50));

        createJavaFXBoard(gp);

        gp.setAlignment(Pos.CENTER);

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                squares[i][j].setTextFormatter(new TextFormatter<String>((TextFormatter.Change change) -> {
                    String newText = change.getControlNewText();
                    if (newText.length() > 1) {
                        return null;
                    } else {
                        return change;
                    }
                }));
            }
        }

        createButtons(p, bottomRack, numTurns);


        /// Options for Button Make MOve in the Game
        makeMoveButton.setOnAction(new EventHandler<ActionEvent>() {
            Alert alert = new Alert(AlertType.ERROR);

            public void handle(ActionEvent event) {

                char[][] newBoard = saveNewBoard();


                boolean valid = game.makeFinalMove(newBoard, playerArray.get(i));

                if (valid == true) {

                    disableGridSquares();
                    roundNumber++;
                    numTurns++;
                    scoreLabels[i].
                            setText(playerArray.get(i).getName() + ": " + playerArray.get(i).getPoints());

                    if (i + 1 == playerArray.size()) {
                        i = 0;
                    } else {
                        i++;
                    }

                    topHeading.setText("It's " + playerArray.get(i).getName() + "'s Turn!");
                    roundNumberLabel.setText("Round: " + String.valueOf(roundNumber));
                    createButtons(playerArray.get(i), bottomRack, numTurns);
                    errors.setText("Reedy Bande Khoda !!!");
                }
                else {

                    alert.setHeaderText("Error reedy Hajiii");
                    alert.setHeaderText("An error has been encountered");
                    alert.setContentText(game.getErrors().get(game.getErrors().size() - 1));
                    alert.showAndWait();


                    //                    errors.setStyle("" + "-fx-font-size: 15px;" + "-fx-font-family: 'Javanese Text';");
                }
            }
        });

        HBox moveAndPass = new HBox();
        moveAndPass.setPadding(new Insets(10));
        moveAndPass.setSpacing(10);
        moveAndPass.setAlignment(Pos.CENTER);
        HBox biggerLayout = new HBox();
        HBox bigLayout = new HBox();
        VBox layout = new VBox();
        scoreBox.setStyle("-fx-background-color: transparent;");
        scoreBox.setAlignment(Pos.CENTER_LEFT);
        scoreBox.setPadding(new Insets(0, 0, 0, 10));
        bottomRack.setPadding(new Insets(0, 0, 10, 0));
        moveAndPass.getChildren().addAll(passButton, makeMoveButton);
        layout.getChildren().addAll(topHeading, roundNumberLabel, errors, scoreBox, gp, bottomRack, moveAndPass);
        layout.setStyle(transparentBackground);
        layout.setAlignment(Pos.CENTER);
        bigLayout.getChildren().addAll(scoreBox, layout);
        bigLayout.setStyle(transparentBackground);
        biggerLayout.getChildren().addAll(bigLayout);
        biggerLayout.setStyle(transparentBackground);
        primaryStage.setScene(new Scene(biggerLayout, 900, 700, Color.LIGHTSKYBLUE));
        primaryStage.show();
    }

    public void disableGridSquares() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (!squares[i][j].getText().isEmpty()) {
                    squares[i][j].setDisable(true);
                }
            }
        }
    }



    // creating Information  stage here
    public VBox createScoreBoard() {
        VBox scoreBox = new VBox();
        scoreLabels[0] = new Label("**********Points*********");
        scoreLabels[0].setStyle("-fx-underline: true;");
        changeTexts(scoreLabels[0]);
        scoreLabels[0].setAlignment(Pos.CENTER);
        // scoreBox.getChildren().addAll(scoreLabels[0]);
        for (int i = 0; i < playerArray.size(); i++) {
            scoreLabels[i] = new Label(
                    "playerName->" + playerArray.get(i).getName() + ": " + playerArray.get(i).getPoints());
            changeTexts(scoreLabels[i]);
            scoreBox.getChildren().addAll(scoreLabels[i]);
        }
        return scoreBox;
    }


    public void changeTexts(Node n) {
        n.setStyle("" + "-fx-font-size: 30px;" + "-fx-font-family: Cambria;");
    }

    public int getNumPlayers(TextField p1, TextField p2) {
        int numPlayers = 0;
        if (!p1.getText().isEmpty()) {
            numPlayers++;
        }
        if (!p2.getText().isEmpty()) {
            numPlayers++;
        }
        return numPlayers;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
