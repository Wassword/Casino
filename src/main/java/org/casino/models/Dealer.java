package org.casino.models;

import org.example.Values;

import java.util.ArrayList;

public class Dealer {
    private final ArrayList<Card> hand;
    private ArrayList<Card> cards;

    public Dealer() {
        this.hand =new ArrayList<>();
    }

    public ArrayList<Card> getHand() {
        return hand;
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
        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;

        }
        return value;
    }
    public void playTurn( Deck deck ) {
        while (calculateHandValue() < 17) {
            addCardToHand(deck.dealCard());
        }
    }

    public void clearHand(){
        this.hand.clear();
    }
}
