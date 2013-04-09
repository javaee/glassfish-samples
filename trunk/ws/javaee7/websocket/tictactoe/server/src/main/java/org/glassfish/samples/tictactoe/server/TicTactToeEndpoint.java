package org.glassfish.samples.tictactoe.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author johan
 */
@ServerEndpoint("/endpoint")
public class TicTactToeEndpoint {

    private static final List<Game> activeGames = new LinkedList<Game>();
    private static final Map<String, Session> sessions = new HashMap<String, Session>();
    private static final Logger LOGGER = Logger.getLogger("tictactoe");
    private static Game lastGame = new Game();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        LOGGER.info("Opening Endpoint for session " + session);

        synchronized (activeGames) {
            String sid = session.getId();
            sessions.put(sid, session);
            if (lastGame.getPlayer1() == null) {
                lastGame.setPlayer1(sid);
                session.getBasicRemote().sendText("p1");

            } else {
                lastGame.setPlayer2(sid);
                activeGames.add(lastGame);
                session.getBasicRemote().sendText("p3");
                String p1 = lastGame.getPlayer1();
                Session s1 = sessions.get(p1);
                System.out.println("second one");
                System.out.println("p1 = " + p1);
                System.out.println("sessions = " + sessions);
                System.out.println("s1 = " + s1);
                s1.getBasicRemote().sendText("p2");
                System.out.println("done sending message to first player");
                lastGame = new Game();

            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        LOGGER.info("CLOSED! " + session);
        LOGGER.info("reason: " + reason.getCloseCode() + "-- " + reason.getReasonPhrase());
        sessions.remove(session.getId());
    }

    @OnMessage
    public void onMessage(String obj, Session session) throws IOException {
        LOGGER.info("Got message from session " + session + ": " + obj);
        String sid = session.getId();
        for (Game candidate : activeGames) {

            if (sid.equals(candidate.getPlayer1())) {
                String other = candidate.getPlayer2();
                Session s2 = sessions.get(other);
                s2.getBasicRemote().sendText(obj);
            }
            if (sid.equals(candidate.getPlayer2())) {
                String other = candidate.getPlayer1();
                Session s1 = sessions.get(other);
                s1.getBasicRemote().sendText(obj);
            }
        }
    }
}
