package org.casino.models;

import lombok.Getter;

@Getter
public enum Values {
    TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"), SIX(6, "6"),
    SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"), TEN(10, "10"),
    JACK(10, "J"), QUEEN(10, "Q"), KING(10, "K"), ACE(11, "A");

    /**
     * -- GETTER --
     * Returns the card's default Blackjack value.
     * For ACE, the value defaults to 11 but can be adjusted in game logic.
     */
    private final int blackjackValue; // Blackjack point value for each card

    private final String abbreviation; // Abbreviation for card value (e.g., "A" instead of "ACE")

    // Constructor to initialize the card value and abbreviation
    Values(int blackjackValue, String abbreviation) {
        this.blackjackValue = blackjackValue;
        this.abbreviation = abbreviation;
    }

    /**
     * Checks if the card is a face card (Jack, Queen, or King).
     *
     * @return true if the card is a face card, otherwise false.
     */
    public boolean isFaceCard() {
        return this == JACK || this == QUEEN || this == KING;
    }

    /**
     * Checks if the card is an Ace.
     *
     * @return true if the card is an Ace, otherwise false.
     */
    public boolean isAce() {
        return this == ACE;
    }

    /**
     * Returns the display name of the card value (e.g., "Ace" instead of "ACE").
     *
     * @return string representing the display name of the value.
     */
    @Override
    public String toString() {
        String name = name();
        return name.charAt(0) + name.substring(1).toLowerCase();  // Capitalizes first letter only
    }
}
