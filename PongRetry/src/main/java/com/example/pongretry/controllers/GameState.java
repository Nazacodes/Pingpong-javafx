package com.example.pongretry.controllers;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L; // Ensure version compatibility
    private int player1Score;
    private int player2Score;
    private double racket1Position;
    private double racket2Position;
    private double ballX;
    private double ballY;

    // Constructor
    public GameState(int player1Score, int player2Score, double racket1Position, double racket2Position,
                     double ballX, double ballY) {
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.racket1Position = racket1Position;
        this.racket2Position = racket2Position;
        this.ballX = ballX;
        this.ballY = ballY;
    }

    // Getters and setters
    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public double getRacket1Position() {
        return racket1Position;
    }

    public void setRacket1Position(double racket1Position) {
        this.racket1Position = racket1Position;
    }

    public double getRacket2Position() {
        return racket2Position;
    }

    public void setRacket2Position(double racket2Position) {
        this.racket2Position = racket2Position;
    }

    public double getBallX() {
        return ballX;
    }

    public void setBallX(double ballX) {
        this.ballX = ballX;
    }

    public double getBallY() {
        return ballY;
    }

    public void setBallY(double ballY) {
        this.ballY = ballY;
    }
}
