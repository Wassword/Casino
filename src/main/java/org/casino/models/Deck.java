package org.casino.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Component
public class Deck {
    private final List<Card> cards;

    // Constructor to initialize and populate the deck with all cards
    public Deck() {
        this.cards = new ArrayList<>();
        initializeDeck();
    }

    // Initialize the deck with a full set of cards
    private void initializeDeck() {
        for (Suits suit : Suits.values()) {
            for (Values value : Values.values()) {
                cards.add(new Card(suit, value));
            }
        }
    }

    // Shuffle the deck to randomize card order
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // Deal a card from the deck
    public Card dealCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("The deck is empty, no more cards to deal.");
        }
        return cards.removeLast(); // Deal from the end of the list for improved performance
    }

    // Get the number of remaining cards in the deck
    public int getRemainingCards() {
        return cards.size();
    }

    // Reset the deck by clearing, re-initializing, and shuffling
    public void resetDeck() {
        cards.clear();
        initializeDeck();
        shuffle();
    }

    // Peek at the top card without removing it
    public Card peekTopCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("The deck is empty.");
        }
        return cards.getLast();
    }

    // Check if the deck is empty
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // Display the entire deck (useful for debugging)
    public String showDeck() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.toString()).append("\n");
        }
        return sb.toString();
    }
}
