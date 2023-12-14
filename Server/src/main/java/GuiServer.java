import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class GuiServer extends Application {

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
    static int portNum;
    ListView<String> listItems;
    Server serverConnection;

    @Override
    public void start(Stage primaryStage) {
        // Create and configure the GUI
        BorderPane root = new BorderPane();


        // Button to start the server
        Button start = new Button("Start Server");
        start.setPrefSize(100, 50); // Set preferred size

        //Text box to get port number
        TextField portInput = new TextField("Enter port number");
        portInput.setPrefSize(50,50);

        //Initialize vbox
        VBox temp = new VBox(portInput,start);
        temp.setPrefWidth(200);
        temp.setAlignment(Pos.CENTER);

        // Add components to the layout
        root.setCenter(temp);

        // Configure the stage
        Scene scene = new Scene(root, 700, 700);
        primaryStage.setTitle("Server");
        primaryStage.setScene(scene);

        // Start the server in a separate thread
        start.setOnAction(e -> {
            portNum = Integer.parseInt(portInput.getText());
            primaryStage.setScene(createServerGui());

            serverConnection = new Server(data -> {
                Platform.runLater(()->{
                    listItems.getItems().add(data.toString());
                });

            }, portNum);


        });
        listItems = new ListView<String>();

        // Show the stage
        primaryStage.show();
    }

    public Scene createServerGui() {

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(70));
        pane.setStyle("-fx-background-color: coral");

        pane.setCenter(listItems);

        return new Scene(pane, 700, 700);

    }

}
