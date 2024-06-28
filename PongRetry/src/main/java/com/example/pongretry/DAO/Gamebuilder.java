package com.example.pongretry.DAO;

public class Gamebuilder {
    private String gameId;
    private String gameName;
    private String playerName1;
    private String playerName2;
    private int player1Score;
    private int player2Score;
    private int racketSize;
    private int ballSpeed;
    private int ballFrequencyIncrease;
    private int amountToWin;

    // Constructor
    public Gamebuilder(String gameId, String gameName, String playerName1, String playerName2, int player1Score, int player2Score, int racketSize, int ballSpeed, int ballFrequencyIncrease, int amountToWin) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.playerName1 = playerName1;
        this.playerName2 = playerName2;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.racketSize = racketSize;
        this.ballSpeed = ballSpeed;
        this.ballFrequencyIncrease = ballFrequencyIncrease;
        this.amountToWin = amountToWin;
    }

    // Getters
    public String getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public String getPlayerName1() {
        return playerName1;
    }

    public String getPlayerName2() {
        return playerName2;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public int getRacketSize() {
        return racketSize;
    }

    public int getBallSpeed() {
        return ballSpeed;
    }

    public int getBallFrequencyIncrease() {
        return ballFrequencyIncrease;
    }

    public int getAmountToWin() {
        return amountToWin;
    }

    public String getPlayer1Name() {
        return playerName1;
    }

    public String getPlayer2Name() {
        return playerName2;
    }


}
