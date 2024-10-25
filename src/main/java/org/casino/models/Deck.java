package org.casino.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Getter
@Setter
@Component
public class Deck {
    private final ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        // Go through all the suits
        for (Suits suit : Suits.values()) {
            // Go through all values (ranks)
            for (Values value : Values.values()) {
                // Add a new card containing each iteration's suit and rank
                cards.add(new Card(suit, value));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card dealCard() {
        // Check if the deck is empty before dealing
        if (cards.isEmpty()) {
            throw new IllegalStateException("The deck is empty, no more cards to deal.");
        }
        return cards.removeFirst(); // Consider using remove(cards.size() - 1) to deal from the end
    }

    public int getRemainingCards() {
        return cards.size();
    }

    // Optional: Reset the deck if needed
    public void resetDeck() {
        cards.clear();
        for (Suits suit : Suits.values()) {
            for (Values value : Values.values()) {
                cards.add(new Card(suit, value));
            }
        }
        shuffle();
    }
}
