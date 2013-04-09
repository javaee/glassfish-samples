package org.glassfish.samples.tictactoe.server;

import java.util.UUID;

/**
 *
 * @author johan
 */
public class Game {

	private String uid;
	
	private int[][] board = new int[3][3];
	private String player1;
	private String player2;

	public Game () {
		this.uid = UUID.randomUUID().toString();
	}
	
	/**
	 * @return the player1
	 */
	public String getPlayer1() {
		return player1;
	}

	/**
	 * @param player1 the player1 to set
	 */
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	/**
	 * @return the player2
	 */
	public String getPlayer2() {
		return player2;
	}

	/**
	 * @param player2 the player2 to set
	 */
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
	
	
}
