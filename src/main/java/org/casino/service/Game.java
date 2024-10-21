package org.casino.service;

import lombok.Getter;
import org.casino.models.Dealer;
import org.casino.models.Deck;
import org.casino.models.Player;

public class Game {
    private final Player player;
    private final Dealer dealer;
    private final Deck deck;
    // Check if the game is over
    @Getter
    private boolean gameOver;

    // Constructor to initialize the game
    public Game(int initialPlayerBalance) {
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

    // Get the player's current hand as a string
    public String getPlayerHand() {
        return player.getHand().toString();
    }

    // Get the dealer's face-up card (the first card in their hand)
    public String getDealerFaceUpCard() {
        return dealer.getHand().get(0).toString();
    }

    // Player takes another card (hit)
    public String playerHit() {
        if (!gameOver) {
            player.addCardToHand(deck.dealCard());
            if (player.calculateHandValue() > 21) {
                gameOver = true;
                return "You busted! Final hand: " + player.getHand() + " with " + player.calculateHandValue() + " points.";
            }
        }
        return "Your hand: " + player.getHand() + " with " + player.calculateHandValue() + " points.";
    }

    // Player stands (end their turn)
    public String playerStand() {
        if (!gameOver) {
            // Dealer plays automatically (must hit until they reach 17)
            while (dealer.calculateHandValue() < 17) {
                dealer.addCardToHand(deck.dealCard());
            }
            gameOver = true;
            return determineWinner();
        }
        return "The game is already over.";
    }

    // Determine the winner of the game
    private String determineWinner() {
        int playerPoints = player.calculateHandValue();
        int dealerPoints = dealer.calculateHandValue();

        if (dealerPoints > 21) {
            return "Dealer busted! You win! Dealer hand: " + dealer.getHand() + " with " + dealerPoints + " points.";
        } else if (playerPoints > dealerPoints) {
            return "You win! Your hand: " + player.getHand() + " with " + playerPoints + " points. Dealer hand: " + dealer.getHand() + " with " + dealerPoints + " points.";
        } else if (playerPoints < dealerPoints) {
            return "Dealer wins. Your hand: " + player.getHand() + " with " + playerPoints + " points. Dealer hand: " + dealer.getHand() + " with " + dealerPoints + " points.";
        } else {
            return "It's a tie! Both you and the dealer have " + playerPoints + " points.";
        }
    }

}
