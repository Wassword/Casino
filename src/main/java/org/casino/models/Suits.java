package org.casino.models;
/**
 * Contains the suits of a Card, Names
 */
public enum Suits {
    CLUB("Clubs"),
    DIAMOND("Diamonds"),
    HEART("Hearts"),
    SPADE("Spades");

    String suitName;

    Suits(String suitName) {
        this.suitName = suitName;
    }
    public String toString() {
        return suitName;
    }

    }

