package org.casino.models;

import lombok.Getter;

/**
 * Enum representing the suits of a card in a standard deck.
 * Each suit has a name and color associated with it.
 */
@Getter
public enum Suits {
    CLUB("Clubs", "Black"),
    DIAMOND("Diamonds", "Red"),
    HEART("Hearts", "Red"),
    SPADE("Spades", "Black");

    // Attributes
    private final String suitName;
    /**
     * -- GETTER --
     *  Get the color of the suit.
     *
     */
    @Getter
    private final String color;

    // Constructor to initialize the suit name and color
    Suits(String suitName, String color) {
        this.suitName = suitName;
        this.color = color;
    }

    /**
     * Returns a friendly name of the suit.
     * @return a string representing the suit name.
     */
    @Override
    public String toString() {
        return suitName;
    }

    /**
     * Checks if the suit is a red color suit.
     * @return true if the suit is red (Hearts or Diamonds), otherwise false.
     */
    public boolean isRed() {
        return "Red".equalsIgnoreCase(color);
    }

    /**
     * Checks if the suit is a black color suit.
     * @return true if the suit is black (Clubs or Spades), otherwise false.
     */
    public boolean isBlack() {
        return "Black".equalsIgnoreCase(color);
    }
}
