package org.casino.models.interfaces;

import java.util.ArrayList;
import org.casino.models.Card;

public interface DeckActions {

    // Methods without 'public' as they are implicitly public in interfaces
    void shuffle();
    void dealNextCard();
    void playHand(ArrayList<Card> hand);
    String determineWinner();
}


