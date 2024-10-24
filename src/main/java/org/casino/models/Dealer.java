package org.casino.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
@AllArgsConstructor
@Setter
@Getter
@Component
public class Dealer {
    private final ArrayList<Card> hand;

    public Dealer() {
        this.hand = new ArrayList<>();
    }

    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    public int calculateHandValue() {
        int value = 0;
        int aceCount = 0;

        for (Card card : hand) {
            value += card.getValue().getValue();
            if (card.getValue() == Values.ACE) {
                aceCount++;
            }
        }

        // Adjust for Aces
        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }

        return value;
    }

    public void playTurn(Deck deck) {
        while (calculateHandValue() < 17) {
            addCardToHand(deck.dealCard());
        }
    }

    public String getFaceUpCard() {
        if (!hand.isEmpty()) {
            return hand.getFirst().toString(); // Assuming the first card is the face-up card
        }
        return "No cards in hand";
    }

    public void clearHand() {
        this.hand.clear();
    }

    public boolean shouldHit() {
        return calculateHandValue() < 21;
    }
}
