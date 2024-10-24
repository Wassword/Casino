package org.casino.service;

import lombok.*;
import org.casino.config.AppProperties;
import org.casino.models.*;
import org.casino.models.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Service
public class BlackjackService {

    private static final Logger logger = LoggerFactory.getLogger(BlackjackService.class);

    private final UserRepository userRepository;
    private final User user;
    private final Dealer dealer;
    private final Deck deck;
    private boolean gameOver;


    @Autowired
    public BlackjackService(UserRepository userRepository, Dealer dealer, Deck deck, AppProperties appProperties) {
        this.userRepository = userRepository;
        this.user = new User(appProperties.getDefaultUsername(), appProperties.getDefaultPassword(), appProperties.getDefaultBalance());
        this.dealer = dealer;
        this.deck = deck;
        this.gameOver = false;
    }

    // Start the game
    public String startGame() {
        logger.info("Starting new game for user: {}", user.getUsername());

        resetGame();
        dealInitialCards();

        return String.format("Game started for %s with a balance of %d", user.getUsername(), user.getBalance());
    }

    // Player hits
    public String playerHit() {
        if (gameOver) {
            return "Game is already over.";
        }

        user.addCardToHand(deck.dealCard());
        int points = user.calculateHandValue();
        logger.info("Player hit. Current hand: {} (Total Points: {})", user.getHand(), points);

        if (points > 21) {
            gameOver = true;
            return handleBust();
        }

        return String.format("Your hand: %s (Total Points: %d)", user.getHand(), points);
    }

    // Player stands
    public String playerStand() {
        if (gameOver) {
            return "Game is already over.";
        }

        while (dealer.shouldHit()) {  // Dealer should hit if hand value < 17
            dealer.addCardToHand(deck.dealCard());
        }

        gameOver = true;
        return handleGameOutcome();
    }

    // Get player's current hand
    public String getPlayerHand() {
        return user.getHand().toString();
    }

    // Get dealer's face-up card
    public String getDealerFaceUpCard() {
        return dealer.getHand().getFirst().toString();
    }

    // Private methods for better code structure

    private void resetGame() {
        user.clearHand();
        dealer.clearHand();
        deck.shuffle();
        gameOver = false;
        logger.info("Game reset and deck shuffled.");
    }

    private void dealInitialCards() {
        user.addCardToHand(deck.dealCard());
        user.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());

        logger.info("Initial cards dealt. Player hand: {}, Dealer hand: {}", user.getHand(), dealer.getHand());
    }

    private String handleBust() {
        logger.info("Player busted with hand: {}", user.getHand());
        return String.format("You busted! Final hand: %s", user.getHand());
    }

    private String handleGameOutcome() {
        int playerPoints = user.calculateHandValue();
        int dealerPoints = dealer.calculateHandValue();

        logger.info("Player stood with hand: {}, Dealer finished with hand: {}", user.getHand(), dealer.getHand());

        if (dealerPoints > 21) {
            return handlePlayerWin("Dealer busted!");
        } else if (playerPoints > dealerPoints) {
            return handlePlayerWin("You win!");
        } else if (dealerPoints > playerPoints) {
            return handleDealerWin();
        } else {
            return "It's a tie!";
        }
    }

    private String handlePlayerWin(String message) {
        user.adjustBalance(true);
        user.setTotalWins(user.getTotalWins() + 1);
        userRepository.save(user);
        logger.info("Player won the game. Updated balance: {}", user.getBalance());
        return String.format("%s Your hand: %s (Total Points: %d)", message, user.getHand(), user.calculateHandValue());
    }

    private String handleDealerWin() {
        logger.info("Dealer won the game. Dealer hand: {}", dealer.getHand());
        return String.format("Dealer wins. Dealer hand: %s (Total Points: %d)", dealer.getHand(), dealer.calculateHandValue());
    }
}
