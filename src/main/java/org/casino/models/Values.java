package org.casino.models;

import lombok.Getter;

@Getter
public enum Values {
    TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    JACK(10), QUEEN(10), KING(10), ACE(11);

    /**
     * -- GETTER --
     *  Returns the card's default Blackjack value.
     *  For ACE, the value defaults to 11 but can be adjusted in game logic.
     *
     */
    private final int blackjackValue; // Blackjack point value for each card

    // Constructor to initialize the card value
    Values(int blackjackValue) {
        this.blackjackValue = blackjackValue;
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
     * @return A string representing the display name of the value.
     */
    @Override
    public String toString() {
        String name = name();
        return name.charAt(0) + name.substring(1).toLowerCase();  // Capitalizes first letter only
    }
}
