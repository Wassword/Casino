package org.casino.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@Component
public class Dealer {

    private final List<Card> hand;

    // Default constructor initializing an empty hand
    public Dealer() {
        this.hand = new ArrayList<>();
    }

    // Add a card to the dealer's hand
    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    // Calculate the total value of the dealer's hand
    public int calculateHandValue() {
        int value = 0;
        int aceCount = 0;

        for (Card card : hand) {
            value += card.getBlackjackValue();
            if (card.isAce()) {
                aceCount++;
            }
        }

        // Adjust for Aces if the value exceeds 21
        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }

        return value;
    }

    // Clears the dealer's hand at the beginning of each new game
    public void clearHand() {
        this.hand.clear();
    }

    // Determines if the dealer should hit (continue playing) based on Blackjack rules
    public boolean shouldHit() {
        return calculateHandValue() < 17;
    }

    // Helper method to display the dealer's entire hand (useful for game results)
    public String showHand() {
        if (hand.isEmpty()) {
            return "Dealer has no cards.";
        }
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append(card.toString()).append(", ");
        }
        return sb.toString().replaceAll(", $", ""); // Remove trailing comma
    }
}
