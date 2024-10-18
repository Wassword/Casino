package org.casino.models;

public class Card {
    //Attributes
    private Suits suit;
    private Values value;

    //Create a (Constructor method) card given a suit and a value(Rank)
    public Card(Suits suit, Values value) {
        this.suit = suit;
        this.value = value;
    }
    //Getters
    public Suits getSuit() {
        return suit;
    }
    public Values getValue() {
        return value;
    }
    //
    @Override
    public String toString() {
        return value + " of " + suit;
    }


}
