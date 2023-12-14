import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Objects;

import static java.lang.System.exit;


public class GuiClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    String Category;

    int counter = 1;

    Client clientConnection;
    ListView<String> listItems2;
    ArrayList<String> categoriesPlayed = new ArrayList<>();

    SerializedData bruh = new SerializedData();
    SerializedData bruh2;

    // Button 1
    Button JMathRockBands = new Button("Japanese Math Rock Bands");

    // Button 2
    Button ScampireBeats = new Button("Scampire's Beats");

    // Button 3
    Button Food = new Button("Food");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple GUI Client");
        BorderPane root = new BorderPane();

        TextField serverPort = new TextField("Enter port number");
        Button sendRequestButton = new Button("Send Request");
        TextArea responseArea = new TextArea();

        VBox temp = new VBox(serverPort, sendRequestButton);
        temp.setAlignment(Pos.CENTER);

        root.setCenter(temp);

        sendRequestButton.setOnAction(event -> {
            int portNum = Integer.parseInt(serverPort.getText());
            primaryStage.setTitle("This is a client");
            clientConnection = new Client(data -> {
                Platform.runLater(() -> {
                    //listItems2.getItems().add(data.toString());
                    bruh2 = (SerializedData)data;
                    if (bruh2.getGuess() == 0) { // if you guessed all the chars wrong
                        System.out.println("attempts : " + bruh2.getAttempts());
                        bruh2.setAttempts(3-counter);
                        counter++;
                        if (bruh2.getAttempts() == 0) {
                            System.out.println("we want the losing screen");
                            primaryStage.setScene(losingScreen(primaryStage));
                            primaryStage.show();
                        } else {
                            // Reset guess count to 6 only if attempts are not exhausted
                            bruh2.setGuess(6);

                            System.out.println("go back to choose category");
                            primaryStage.setScene(createServerGui(primaryStage));
                            primaryStage.show();
                        }
                    }
                    else if(Objects.equals(bruh2.getDisplay(), "you win")){
                        categoriesPlayed.add(bruh2.getCurrentCategory());
                        bruh2.setGuess(6);

                        if(categoriesPlayed.size() == 3){
                            primaryStage.setScene(winningScreen(primaryStage));
                            primaryStage.show();
                        }
                        else{
                            primaryStage.setScene(createServerGui(primaryStage));
                            primaryStage.show();
                        }
                    }
                    else{
                        primaryStage.setScene(guessWord(primaryStage));
                        primaryStage.show();
                    }
                });
            }, portNum);

            clientConnection.start();
            primaryStage.setScene(createServerGui(primaryStage));
        });

        listItems2 = new ListView<>();

        Scene scene = new Scene(root, 700, 700);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Pick categories
    public Scene createServerGui(Stage primaryStage) {

        BorderPane pane = new BorderPane();

        //disable appropriate buttons
        if(bruh2 != null){
            for(int i = 0; i < categoriesPlayed.size(); i++){
                if(Objects.equals(categoriesPlayed.get(i), "Food")){
                    Food.setDisable(true);
                }
                if(Objects.equals(categoriesPlayed.get(i), "JMathRockBands")){
                    JMathRockBands.setDisable(true);
                }
                if(Objects.equals(categoriesPlayed.get(i), "ScampireBeats")){
                    ScampireBeats.setDisable(true);
                }
            }
        }


        JMathRockBands.setOnAction(event -> {
            if (clientConnection != null) {
                bruh.setCurrentCategory("JMathRockBands");
                bruh.setCategory("Japanese Math Rock");
                bruh.setUpdate("category");
                System.out.println("Sending category: " + bruh.getCategory());
                clientConnection.send(bruh);

            }
        });

        ScampireBeats.setOnAction(event -> {
            if (clientConnection != null) {
                bruh.setCurrentCategory("ScampireBeats");
                bruh.setCategory("Scampire Beats");
                bruh.setUpdate("category");
                System.out.println("Sending category: " + bruh.getCategory());
                clientConnection.send(bruh);
            }
        });

        Food.setOnAction(event -> {
            if (clientConnection != null) {
                bruh.setCurrentCategory("Food");
                bruh.setCategory("Food");
                bruh.setUpdate("category");
                System.out.println("Sending category: " + bruh.getCategory());
                clientConnection.send(bruh);
            }
        });

        Label attempt = new Label("Attempts: " + String.valueOf(bruh.getAttempts() - counter + 1));
        attempt.setFont(new Font(30));
        attempt.setAlignment(Pos.CENTER);

        HBox buttons = new HBox(JMathRockBands, ScampireBeats, Food);
        buttons.setAlignment(Pos.CENTER); // Center the HBox content horizontally
        buttons.setSpacing(30); // Set spacing between buttons in the HBox

        VBox finale = new VBox(attempt,buttons);
        finale.setAlignment(Pos.CENTER);

        pane.setCenter(finale);
        BorderPane.setAlignment(finale, Pos.CENTER);

        return new Scene(pane, 700, 700);


    }

    public Scene guessWord(Stage primaryStage){
        BorderPane pane = new BorderPane();
        Label main = new Label(bruh2.getDisplay());
        main.setPrefSize(300,400);
        main.setFont(new Font(30));
        main.setAlignment(Pos.CENTER);

        TextField user = new TextField("Guess a letter");
        user.setPrefSize(100,50);

        Label attempt = new Label("Attempts: " + String.valueOf(bruh2.getAttempts()));
        attempt.setFont(new Font(30));
        attempt.setAlignment(Pos.CENTER);

        Label guesses = new Label("Guesses: " + String.valueOf(bruh2.getGuess()));
        guesses.setFont(new Font(30));
        guesses.setAlignment(Pos.CENTER);

        Button send = new Button("Submit");
        send.setPrefSize(100,50);

        send.setOnAction(actionEvent -> {
            String userInput = user.getText();
            if (!userInput.isEmpty()) {
                char guessedChar = userInput.charAt(0);
                bruh2.setChar(guessedChar);
                bruh2.setUpdate("letter");
                clientConnection.send(bruh2);
            }
        });

        VBox temp = new VBox(guesses,main,user,send);
        temp.setAlignment(Pos.CENTER);

        pane.setCenter(temp);

        return new Scene(pane,700,700);
    }

    public Scene losingScreen(Stage primaryStage) {
        BorderPane pane = new BorderPane();

        Label losingLabel = new Label("You lost! Better luck next time.");
        losingLabel.setPrefSize(300, 100);
        losingLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Button restartButton = new Button("Restart Game");
        restartButton.setPrefSize(150, 50);

        restartButton.setOnAction(event -> {
            resetGame();
            bruh.setGuess(6);
            bruh.setAttempts(3);
            counter = 1;
            categoriesPlayed.clear();
            bruh.setDisplay("");
            primaryStage.setScene(createServerGui(primaryStage));
            primaryStage.show();
        });

        Button end = new Button("Exit");

        end.setOnAction(event -> {
            exit(1);
        });

        VBox losingLayout = new VBox(losingLabel, restartButton, end);
        losingLayout.setAlignment(Pos.CENTER);
        losingLayout.setSpacing(20);

        pane.setCenter(losingLayout);

        return new Scene(pane, 700, 700);
    }

    public Scene winningScreen(Stage primaryStage) {
        BorderPane pane = new BorderPane();

        Label winningLabel = new Label("You Won!");
        winningLabel.setPrefSize(300, 100);
        winningLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Button restartButton = new Button("Restart Game");
        restartButton.setPrefSize(150, 50);
        restartButton.setOnAction(event -> {
            resetGame();
            bruh2.setGuess(6);
            bruh2.setAttempts(3);
            counter = 1;
            categoriesPlayed.clear();
            bruh2.setDisplay("");
            primaryStage.setScene(createServerGui(primaryStage));
            primaryStage.show();
        });

        Button end = new Button("Exit");

        end.setOnAction(event -> {
            exit(1);
        });

        VBox winningLayout = new VBox(winningLabel, restartButton, end);
        winningLayout.setAlignment(Pos.CENTER);
        winningLayout.setSpacing(20);

        pane.setCenter(winningLayout);

        return new Scene(pane, 700, 700);
    }

    private void resetGame() {
        categoriesPlayed.clear();
        JMathRockBands.setDisable(false);
        ScampireBeats.setDisable(false);
        Food.setDisable(false);
    }
}
