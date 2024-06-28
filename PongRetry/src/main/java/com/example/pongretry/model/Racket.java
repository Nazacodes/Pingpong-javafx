package com.example.pongretry.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

/**
 * Represents a racket in the Pong game.
 */
public class Racket extends Rectangle implements Serializable {
    private static final double DEFAULT_WIDTH = 5;
    private static final double DEFAULT_HEIGHT = 150;
    private static final Color PURPLE = Color.PURPLE;
    private static final Color PINK = Color.PINK;

    /**
     * Constructs a racket object with the specified size.
     *
     * @param size The size of the racket (1 for small, 2 for medium, 3 for large).
     */
    public Racket(int size) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setSize(size);
        setFill(PURPLE);
        setStroke(PINK);
        setStrokeWidth(2);
    }

    /**
     * Sets the size of the racket based on the provided size.
     *
     * @param size The size of the racket (1 for small, 2 for medium, 3 for large).
     */
    public void setSize(int size) {
        switch (size) {
            case 1: // small size
                setWidth(DEFAULT_WIDTH);
                setHeight(DEFAULT_HEIGHT * 0.5);
                break;
            case 2: // medium size
                setWidth(DEFAULT_WIDTH);
                setHeight(DEFAULT_HEIGHT);
                break;
            case 3: // large size
                setWidth(DEFAULT_WIDTH);
                setHeight(DEFAULT_HEIGHT * 1.5);
                break;
        }
    }
}
