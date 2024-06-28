package com.example.pongretry.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.Serializable;

/**
 * Represents the ball in the Pong game.
 */
public class Ball extends Circle implements Serializable {
    private static final double DEFAULT_RADIUS = 15;
    private static final Color BALL_COLOR = Color.BLACK;
    private static final Color OUTLINE_COLOR = Color.ORANGE;

    private double frequency; // Instance variable to store frequency

    /**
     * Constructs a Ball object with default properties.
     */
    public Ball() {
        super(DEFAULT_RADIUS);
        setFill(BALL_COLOR);
        setStroke(OUTLINE_COLOR);
        setStrokeWidth(2);
        frequency = 1; // Default frequency
    }

    /**
     * Sets the frequency of the ball.
     *
     * @param frequency The frequency to set.
     */
    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    /**
     * Gets the speed based on the selected speed.
     *
     * @param selectedSpeed The selected speed level.
     * @return The speed of the ball.
     */
    public double getSpeed(int selectedSpeed) {
        return switch (selectedSpeed) {
            case 1 -> 0.8;
            case 3 -> 4.0;
            default -> 3.0;
        };
    }

    /**
     * Gets the frequency based on the selected frequency.
     *
     * @param selectedFrequency The selected frequency level.
     * @return The frequency of the ball.
     */
    public double getFrequency(int selectedFrequency) {
        return switch (selectedFrequency) {
            case 1 -> 25.0; // Adjust this value based on your requirement
            case 2 -> 20.0; // Adjust this value based on your requirement
            case 3 -> 15.0; // Adjust this value based on your requirement
            case 4 -> 10.0; // Adjust this value based on your requirement
            default -> 5.0; // Default value
        };
    }
}
