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


        @Override
        public String toString() {
        String[] words = this.value.toString().split(" ");
        StringBuilder capitalizedValue = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalizedValue.append(word.substring(0, 1).toUpperCase())
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return capitalizedValue.toString().trim();
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
    public String getImagePath() {
        // Generates a path for each card image based on its suit and value
        return "/images/Playing-Cards/"
                + suit.toString() + "_"
                + value.getAbbreviation()
                + ".png";
    }


}
