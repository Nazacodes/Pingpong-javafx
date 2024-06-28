package com.example.pongretry.model;

import javafx.scene.control.Label;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a player in the Pong game.
 */
public class Player implements Serializable {
    private String name;
    private int score;
    private transient Label label;
    private final Racket racket;

    /**
     * Constructs a Player object with the specified name and racket.
     *
     * @param name   The name of the player.
     * @param racket The racket associated with the player.
     */
    public Player(String name, Racket racket) {
        this.name = name;
        this.score = 0;
        this.label = new Label();
        this.racket = racket;
        updateLabelAndStyle();
    }

    public Player(String name, Racket racket, int score) {
        this.name = name;
        this.score = score;
        this.label = new Label();
        this.racket = racket;
        updateLabelAndStyle();
    }

    public Player() {
      Racket racket = new Racket(5);
        this.name = "test";
        this.score = 0;
        this.racket = racket;


    }

    /**
     * Gets the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the racket associated with the player.
     *
     * @return The racket associated with the player.
     */
    public Racket getRacket() {
        return racket;
    }

    /**
     * Sets the name of the player.
     *
     * @param name The name to set for the player.
     */
    public void setName(String name) {
        this.name = name;
        updateLabelAndStyle();
    }

    /**
     * Gets the score of the player.
     *
     * @return The score of the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the player.
     *
     * @param score The score to set for the player.
     */
    public void setScore(int score) {
        this.score = score;
        updateLabelAndStyle(); // Update label and style after score change
    }

    /**
     * Gets the label associated with the player.
     *
     * @return The label associated with the player.
     */
    public Label getLabel() {
        return label;
    }

    /**
     * Updates the label text and style to reflect the player's name and score.
     */
    private void updateLabelAndStyle() {
        label.setText(name + ": " + score);
        label.setStyle("-fx-text-fill: red; " +
                "-fx-effect: dropshadow(gaussian, black, 1, 1, 1, 1);");
    }
    // Serialization method to save the player state
    private void writeObject(ObjectOutputStream out) throws IOException {
        // Write player properties to the ObjectOutputStream
        out.writeObject(name);
        out.writeInt(score);
        // Add other properties as needed
    }

    // Deserialization method to restore the player state
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Read player properties from the ObjectInputStream
        name = (String) in.readObject();
        score = in.readInt();
        // Read other properties as needed
    }
}
