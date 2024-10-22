package org.casino.service;

import lombok.Getter;
import org.casino.config.AppProperties;
import org.casino.models.*;
import org.casino.models.interfaces.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlackjackService {

    private final GameSessionRepository gameSessionRepository;
    private User user;
    private Dealer dealer;
    private Deck deck;
    // Game status helper methods
    @Getter
    private boolean gameOver;

    // Constructor
    @Autowired
    public BlackjackService(GameSessionRepository gameSessionRepository, AppProperties appProperties) {
        this.gameSessionRepository = gameSessionRepository;
        // Injected AppProperties

        // Using AppProperties for initialization
        this.user = new User("john_doe", "password123", 100); // Example initialization
        this.dealer = new Dealer();  // Example initialization
        this.deck = new Deck();      // Example initialization
        this.gameOver = false;
    }

    // Start the game
    public String startGame() {
        GameSession session = new GameSession(user.getUsername(), user.getBalance(), "Started");
        gameSessionRepository.save(session); // Save session to the database

        // Deal initial cards to both the user and dealer
        user.clearHand();  // Clear previous hand
        dealer.clearHand();  // Clear previous dealer hand

        user.addCardToHand(deck.dealCard());
        user.addCardToHand(deck.dealCard());

        dealer.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());

        return "Game started for " + user.getUsername() + " with " + user.getBalance() + " balance.";
    }
    // Get the game status
    public String getGameStatus(Long gameId) {
        GameSession session = gameSessionRepository.findById(gameId).orElse(null);
        if (session != null) {
            return "Game Status: " + session.getGameStatus();
        } else {
            return "Game not found!";
        }
    }

    // Player hits
    public String playerHit() {
        if (!gameOver) {
            user.addCardToHand(deck.dealCard());
            int points = user.calculateHandValue();
            if (points > 21) {
                gameOver = true;
                return "You busted! Final hand: " + user.getHand();
            }
            return "Your hand: " + user.getHand() + " (Total Points: " + points + ")";
        }
        return "Game is already over.";
    }

    // Player stands
    public String playerStand() {
        if (!gameOver) {
            while (dealer.calculateHandValue() < 17) {
                dealer.addCardToHand(deck.dealCard());
            }

            gameOver = true;
            return getFinalResults();
        }
        return "Game is already over.";
    }

    // Get the final results of the game
    private String getFinalResults() {
        int userPoints = user.calculateHandValue();
        int dealerPoints = dealer.calculateHandValue();

        if (dealerPoints > 21) {
            return "Dealer busted! You win. Dealer hand: " + dealer.getHand();
        } else if (userPoints > dealerPoints) {
            return "You win! Your hand: " + user.getHand() + " (Total Points: " + userPoints + ")";
        } else if (dealerPoints > userPoints) {
            return "Dealer wins. Dealer hand: " + dealer.getHand() + " (Total Points: " + dealerPoints + ")";
        } else {
            return "It's a tie!";
        }
    }

    public String getPlayerHand() {
        return user.getHand().toString();
    }

    public String getDealerFaceUpCard() {
        return dealer.getHand().getFirst().toString();  // Assuming dealer reveals the first card
    }
}