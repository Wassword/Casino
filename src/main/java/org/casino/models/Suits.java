package org.casino.models;

import lombok.Getter;

/**
 * Contains the suits of a Card, Names
 */
@Getter
public enum Suits {
    CLUB("Clubs"),
    DIAMOND("Diamonds"),
    HEART("Hearts"),
    SPADE("Spades");

    // Getter for suit name
    private final String suitName;

    // Constructor to initialize the suit name
    Suits(String suitName) {
        this.suitName = suitName;
    }

    // Override toString to return suit name
    @Override
    public String toString() {
        return suitName;
    }
}
