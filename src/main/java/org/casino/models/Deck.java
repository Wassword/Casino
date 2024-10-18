package org.casino.models;
import org.example.Values;

import java.util.ArrayList;
import java.util.Collections;


public class Deck {
    private ArrayList<Card> cards;


    public Deck() {
        cards = new ArrayList<>();
        //Go through all the suits
        for (Suits suit : Suits.values()) {
            //go through all values(ranks)
            for (Values value : Values.values()) {
                //add a new card containing each iterations suit and rank
                cards.add(new Card(suit, value));
            }
        }
    }
    public void shuffle(){
        Collections.shuffle(cards);
    }

    public Card dealCard(){
        return cards.remove(0);
    }

    public int getRemainingCards(){
        return cards.size();
    }
}
