package org.casino.service;

import lombok.Getter;
import org.casino.config.AppProperties;
import org.casino.models.*;
import org.casino.models.interfaces.GameSessionRepository;
import org.casino.models.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlackjackService {

    private final GameSessionRepository gameSessionRepository;
    private final UserRepository userRepository;

    private final User user;
    private final Dealer dealer;
    private final Deck deck;

    @Getter
    private boolean gameOver;

    // Constructor
    @Autowired
    public BlackjackService(GameSessionRepository gameSessionRepository, UserRepository userRepository, AppProperties appProperties) {
        this.gameSessionRepository = gameSessionRepository;
        this.userRepository = userRepository;

        // Example initialization using AppProperties
        this.user = new User(appProperties.getDefaultUsername(), appProperties.getDefaultPassword(), appProperties.getDefaultBalance());
        this.dealer = new Dealer();
        this.deck = new Deck();
        this.gameOver = false;
    }

    // Start the game
    public String startGame() {
        GameSession session = new GameSession(user.getUsername(), user.getBalance(), "Started");
        gameSessionRepository.save(session); // Save session to the database

        // Deal initial cards to both the user and dealer
        user.clearHand();
        dealer.clearHand();

        deck.shuffle(); // Shuffle the deck before dealing

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
            user.adjustBalance(true); // Adjust balance if player wins
            user.setTotalWins(user.getTotalWins() + 1); // Increment wins
            userRepository.save(user); // Save the updated user
            return "Dealer busted! You win. Dealer hand: " + dealer.getHand();
        } else if (userPoints > dealerPoints) {
            user.adjustBalance(true); // Adjust balance if player wins
            user.setTotalWins(user.getTotalWins() + 1); // Increment wins
            userRepository.save(user); // Save the updated user
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
        return dealer.getHand().get(0).toString();  // Assuming dealer reveals the first card
    }

    // Helper to reset game
    public void resetGame() {
        this.gameOver = false;
        user.clearHand();
        dealer.clearHand();
        deck.shuffle();
    }
}
