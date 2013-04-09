package org.glassfish.samples.tictactoe.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

/**
 *
 * @author Johan
 */
public class LocalEndpoint extends Endpoint implements MessageHandler.Whole<String> {

    public static TicTacToeClient tictactoe;
    private Session session;

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("Endpoint opened, session = " + session + ", config = " + config);
        this.session = session;
        session.addMessageHandler(this);
        tictactoe.setEndpoint(this);
    }

    @Override
    public void onMessage(final String message) {
        Platform.runLater(new Runnable() {
            public void run() {

                System.out.println("GOT MESSAGE: " + message);
                if ("p1".equals(message)) {
                    tictactoe.setInfo("Waiting for a second player to join...");
                }
                if ("p2".equals(message)) {
                    tictactoe.setInfo("You play 'O'");
                    tictactoe.setSymbol(1);
                    tictactoe.setOtherSymbol(2);
                    tictactoe.myTurn(true);
                }
                if ("p3".equals(message)) {
                    tictactoe.setInfo("You play 'X'");
                    tictactoe.setSymbol(2);
                    tictactoe.setOtherSymbol(1);
                }
                if (message.startsWith("o")) {
                    int c = Integer.parseInt(message.substring(1));
                    tictactoe.doMove(c);
                }
                if (message.startsWith("x")) {
                    int c = Integer.parseInt(message.substring(1));
                    tictactoe.doMove(c);
                }

            }
        });

    }

    void myMove(int coords, int symbol) {
        String move = ((symbol == 1) ? "o" : "x") + coords;
        System.out.println("send this move: " + move);
        try {
            session.getBasicRemote().sendText(move);
        } catch (IOException ex) {
            Logger.getLogger(LocalEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void done() throws IOException {
        System.out.println("closing session");
        session.close();
        System.out.println("closed session");
    }
}
