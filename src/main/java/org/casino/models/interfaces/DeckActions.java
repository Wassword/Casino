package org.casino.models.interfaces;

import org.example.models.Card;

import java.util.ArrayList;

public interface DeckActions {
     public void shuffle() ;

    public Card dealNextCard();

    public void printDeck(int numToPrint) ;

    void playHand(ArrayList<Card> hand);
    void determineWinner();
}
}
