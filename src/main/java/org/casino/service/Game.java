package org.casino.service;

import org.casino.models.Card;
import org.casino.models.Dealer;
import org.casino.models.Player;
import org.casino.models.Deck;

import java.util.ArrayList;

public class Game {

    private final Player player;
    private final Dealer dealer;
    private final Deck deck;
    private boolean gameOver;

    // Constructor to initialize the game
    public Game() {
        this.deck = new Deck();
        this.player = new Player(initialPlayerBalance);
        this.dealer = new Dealer();
        this.gameOver = false;

        // Deal initial cards to both player and dealer
        player.addCardToHand(deck.dealCard());
        player.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());
    }

    // Check if the game is over
    public boolean isGameOver() {
        return gameOver;
    }

    // Get player's hand (to display it)
    public ArrayList<Card> getPlayerHand() {
        return player.getHand(); // Assuming Player class has a method getHand() to return player's hand as a String
    }

    // Get dealer's face-up card (to display it during the game)
    public String getDealerFaceUpCard() {
        return dealer.getFaceUpCard(); // Assuming Dealer class has a method to return the first card
    }

    // Player hits (draws a card from the deck)
    public void playerHit() {
        player.addCardToHand(deck.dealCard());
        // If the player exceeds 21 points, game is over
        if (player.calculatePoints() > 21) {
            gameOver = true;
        }
    }

    // Check if the player has busted (i.e., exceeded 21 points)
    public boolean isPlayerBusted() {
        return player.calculatePoints() > 21;
    }

    // Player stands, which means the dealer will now play
    public void playerStand() {
        dealerPlay(); // Trigger dealer's turn
        gameOver = true;
    }

    // Dealer's turn logic: Dealer must draw cards until reaching 17 points or more
    public void dealerPlay() {
        while (dealer.calculatePoints() < 17) {
            dealer.addCardToHand(deck.dealCard());
        }
    }

    // Get the dealer's entire hand at the end of the game
    public String getDealerHand() {
        return dealer.getHand();
    }

    // Check if the player won
    public boolean didPlayerWin() {
        int playerPoints = player.calculatePoints();
        int dealerPoints = dealer.calculatePoints();

        // Player wins if they have more points than the dealer without busting, or if the dealer busts
        return playerPoints <= 21 && (dealerPoints > 21 || playerPoints > dealerPoints);
    }

    // Check if the dealer won
    public boolean didDealerWin() {
        int playerPoints = player.calculatePoints();
        int dealerPoints = dealer.calculatePoints();

        // Dealer wins if they have more points than the player without busting
        return dealerPoints <= 21 && dealerPoints >= playerPoints;
    }
}

