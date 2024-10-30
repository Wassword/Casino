package org.casino.models;

import lombok.Getter;

/**
 * Enum representing the suits of a card in a standard deck.
 * Each suit has a name and color associated with it.
 */
@Getter
public enum Suits {
    CLUB("Clubs"),
    DIAMOND("Diamonds"),
    HEART("Hearts"),
    SPADE("Spades");

    // Attributes
    private final String suitName;

    /**
     * -- GETTER --
     * Get the color of the suit.
     */
    // Constructor to initialize the suit name and color
    Suits(String suitName) {
        this.suitName = suitName;
    }

    /**
     * Returns a friendly name of the suit.
     *
     * @return a string representing the suit name.
     */
    @Override
    public String toString() {
        return suitName;
    }
}