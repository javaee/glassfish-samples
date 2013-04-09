package org.glassfish.samples.tictactoe.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;

/**
 * Hello world!
 *
 */
public class TicTacToeClient extends Application {

    private String SERVER = "ws://localhost:8080/tictactoeserver/endpoint";
    final StackPane[] tile = new StackPane[9];
    final StringProperty info = new SimpleStringProperty("starting the game");
    final StringProperty turnInfo = new SimpleStringProperty("");
    private boolean myTurn = false;
    private int[] board = new int[9];
    private Rectangle[] rect = new Rectangle[9];
    private int symbol = 0;
    private int otherSymbol = 0;
    private final Font font = new Font(60);
    private final Color COLOR_WIN = Color.GREEN;
    private boolean winner = false;
    
    public static void main(String[] args) {
        launch(args);
    }
    private LocalEndpoint endpoint;

    @Override
    public void start(Stage stage) throws Exception {
        prepareBoard();
        LocalEndpoint.tictactoe = this;
        Label infoLabel = new Label();
        infoLabel.textProperty().bind(info);
        Label turnInfoLabel = new Label();
        turnInfoLabel.textProperty().bind(turnInfo);
        VBox rows = new VBox();
        rows.setStyle("-fx-background-color: black; -fx-background-radius: 1000");
        rows.setSpacing(10);
        for (int i = 0; i < 3; i++) {
            HBox cols = new HBox();
            cols.setSpacing(10);
            cols.getChildren().addAll(tile[3 * i], tile[3 * i + 1], tile[3 * i + 2]);
            rows.getChildren().add(cols);
        }
        BorderPane root = new BorderPane();
        root.setStyle("-fx-padding: 20");
        root.setMargin(infoLabel, new Insets(10, 0, 10, 0));
        root.setTop(infoLabel);
        root.setCenter(rows);
        root.setBottom(turnInfoLabel);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        startGame();
    }

    private void prepareBoard() {
        for (int i = 0; i < 9; i++) {
            tile[i] = new StackPane();
            rect[i] = new Rectangle(80, 80);
            rect[i].setFill(Color.WHITE);
            tile[i].getChildren().add(rect[i]);
            final int coords = i;
            tile[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent t) {
                    System.out.println("mouse clicked on " + coords);
                    if (myTurn && !winner) {
                        if (board[coords] == 0) {
                            board[coords] = symbol;
                            String s = (symbol == 1) ? "O" : "X";
                            Label l = new Label(s);
                            l.setFont(font);
                            tile[coords].getChildren().add(l);
                            myTurn(false);
                            endpoint.myMove(coords, symbol);
                            System.out.println("Winner? " + checkWinner());
                            turnInfo.set("Waiting for your opponent to make a move...");
                        }
                    }
                }
            });
        }
    }

    private void startGame() throws URISyntaxException, DeploymentException, IOException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(LocalEndpoint.class, null, new URI(SERVER));
    }

    public void setInfo(String txt) {
        System.out.println("change infotext from " + info.get() + " to " + txt);
        info.set(txt);
    }

    public void doMove(int coords) {
        String s = (otherSymbol == 1) ? "O" : "X";

        Label l = new Label(s);
        l.setFont(font);
        tile[coords].getChildren().add(l);

        board[coords] = otherSymbol;
        myTurn(true);
        System.out.println("winner?" + checkWinner());
    }

    public void setSymbol(int s) {
        symbol = s;
    }

    public void setOtherSymbol(int s) {
        otherSymbol = s;
    }

    public void myTurn(boolean b) {
        myTurn = b;
        if (b) {
            turnInfo.set("Your turn...");
        }

    }

    void setEndpoint(LocalEndpoint endpoint) {
        this.endpoint = endpoint;
    }

    public int checkWinner() {

        for (int i = 0; i < 3; i++) {
            // check horizontal
            if ((board[3 * i] != 0) && (board[3 * i] == board[3 * i + 1]) && (board[3 * i] == board[3 * i + 2])) {
                setWinner(board[3 * i]);
                rect[3 * i].setFill(COLOR_WIN);
                rect[3 * i + 1].setFill(COLOR_WIN);
                rect[3 * i + 2].setFill(COLOR_WIN);
                return board[3 * i];
            }
            // check vertical
            if ((board[i] != 0) && (board[i] == board[i + 3]) && (board[i] == board[i + 6])) {
                setWinner(board[i]);
                rect[i].setFill(COLOR_WIN);
                rect[3 + i].setFill(COLOR_WIN);
                rect[6 + i].setFill(COLOR_WIN);
                return board[i];
            }
        }
        if ((board[0] != 0) && (board[0] == board[4]) && (board[0] == board[8])) {
            rect[0].setFill(COLOR_WIN);
            rect[4].setFill(COLOR_WIN);
            rect[8].setFill(COLOR_WIN);

            setWinner(board[0]);
            return board[0];
        }
        if ((board[2] != 0) && (board[2] == board[4]) && (board[2] == board[6])) {
            rect[2].setFill(COLOR_WIN);
            rect[4].setFill(COLOR_WIN);
            rect[6].setFill(COLOR_WIN);
            setWinner(board[2]);
            return board[2];
        }

        return 0;
    }

    private void setWinner(int w) {
        winner = true;
        try {
            endpoint.done();
        } catch (IOException ex) {
            Logger.getLogger(TicTacToeClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
