package org.casino.models;

/**
 * @param suit Getters Attributes
 */

public record Card(Suits suit, Values value) {
    // Constructor for creating a card with a given suit and value (rank)

    // Calculate the Blackjack value of the card
    public int getBlackjackValue() {
        return switch (value) {
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
            case SIX -> 6;
            case SEVEN -> 7;
            case EIGHT -> 8;
            case NINE -> 9;
            case TEN, JACK, QUEEN, KING -> 10;
            case ACE -> 11;  // Typically 11, but can be adjusted to 1 in game logic
        };
    }

    // Override toString for a more readable representation
    @Override
    public String toString() {
        return value + " of " + suit;
    }

    // Override equals to compare cards based on suit and value
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return suit == card.suit && value == card.value;
    }

    // Utility method to check if the card is a face card (Jack, Queen, King)
    public boolean isFaceCard() {
        return value == Values.JACK || value == Values.QUEEN || value == Values.KING;
    }

    // Utility method to check if the card is an Ace
    public boolean isAce() {
        return value == Values.ACE;
    }
}
