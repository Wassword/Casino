package org.casino.service;

import lombok.*;
import org.casino.models.*;
import org.casino.models.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@Service
public class BlackjackService {

    private static final Logger logger = LoggerFactory.getLogger(BlackjackService.class);

    private final UserRepository userRepository;
    @Setter
    private User user;
    private final Dealer dealer;
    private final Deck deck;
    private boolean gameOver;

    @Autowired
    public BlackjackService(UserRepository userRepository, User user, Dealer dealer, Deck deck) {
        this.userRepository = userRepository;
        this.user = user;
        this.dealer = dealer;
        this.deck = deck;
        this.gameOver = false;
    }


        /**
         * Starts a new Blackjack game by resetting the game state and dealing initial cards.
         * @return a status message indicating the game has started.
         */
    public String startGame() {
        logger.info("Starting new game for user: {}", user.getUsername());
        resetGame();
        dealInitialCards();
        return String.format("Game started for %s with a balance of %d", user.getUsername(), user.getBalance());
    }

    /**
     * Handles the player "hit" action, dealing an additional card to the player's hand.
     * @return a message with the updated game state.
     */
    public String playerHit() {
        if (gameOver) {
            return "Game is already over.";
        }

        user.addCardToHand(deck.dealCard());
        int points = user.calculateHandValue();
        logger.info("Player hit. Current hand: {} (Total Points: {})", user.showHand(), points);

        if (points > 21) {
            gameOver = true;
            return handleBust();
        }

        return String.format("Your hand: %s (Total Points: %d)", user.showHand(), points);
    }

    /**
     * Handles the player "stand" action, allowing the dealer to play their turn.
     * @return the final game outcome.
     */
    public String playerStand() {
        if (gameOver) {
            return handleBust();
        }

        playDealerTurn();
        gameOver = true;
        return handleGameOutcome();
    }

    /**
     * Retrieves the player's current hand as a formatted string.
     * @return a string representing the player's hand.
     */
    public List<String> getPlayerHand() {
        if (user.getHand().isEmpty()) {
            throw new NoSuchElementException("Player's hand is empty");
        }
        return user.getHand().stream().map(Card::getImagePath).collect(Collectors.toList());
    }

    /**
     * Retrieves the dealer's face-up card (first card dealt).
     * @return a string representing the dealer's face-up card.
     */
    public String getDealerFaceUpCard() {
        if (dealer.getHand().isEmpty()) {
            throw new NoSuchElementException("Dealer has no cards to show");
        }
        return dealer.getHand().getFirst().getImagePath();  // Get image path for the first card
    }

    // --- Private Methods for Better Code Structure ---

    /**
     * Resets the game state, clearing hands, shuffling the deck, and setting gameOver to false.
     */
    private void resetGame() {
        user.clearHand();
        dealer.clearHand();
        deck.resetDeck();
        gameOver = false;
        logger.info("Game reset and deck shuffled.");
    }

    /**
     * Deals initial cards to both the player and the dealer.
     */
    private void dealInitialCards() {
        user.addCardToHand(deck.dealCard());
        user.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());

        logger.info("Initial cards dealt. Player hand: {}, Dealer face-up card: {}", user.showHand(), getDealerFaceUpCard());
    }

    /**
     * Plays the dealer's turn, dealing additional cards until reaching a minimum hand value of 17.
     */
    private void playDealerTurn() {
        while (dealer.shouldHit()) {
            dealer.addCardToHand(deck.dealCard());
        }
        logger.info("Dealer turn complete. Final hand: {}", dealer.showHand());
    }

    /**
     * Handles the outcome when the player busts.
     * @return a message indicating the player has busted.
     */
    private String handleBust() {
        logger.info("Player busted with hand: {}", user.showHand());
        return String.format("You busted! Final hand: %s", user.showHand());
    }

    /**
     * Determines the outcome of the game by comparing the player's and dealer's hand values.
     * @return a message indicating the result of the game.
     */
    private String handleGameOutcome() {
        int playerPoints = user.calculateHandValue();
        int dealerPoints = dealer.calculateHandValue();

        logger.info("Player stood with hand: {}, Dealer finished with hand: {}", user.showHand(), dealer.showHand());

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

    /**
     * Handles the player's win, updates the balance, and saves the user data.
     * @param message the message to display for a player win.
     * @return a formatted message indicating the player's win.
     */
    private String handlePlayerWin(String message) {
        user.adjustBalance(true);
        user.setTotalWins(user.getTotalWins() + 1);
        userRepository.save(user);
        logger.info("Player won the game. Updated balance: {}", user.getBalance());
        return String.format("%s Your hand: %s (Total Points: %d)", message, user.showHand(), user.calculateHandValue());
    }

    /**
     * Handles the dealer's win outcome.
     * @return a message indicating the dealer's win.
     */
    private String handleDealerWin() {
        logger.info("Dealer won the game. Dealer hand: {}", dealer.showHand());
        return String.format("Dealer wins. Dealer hand: %s (Total Points: %d)", dealer.showHand(), dealer.calculateHandValue());
    }

    public String getDealerHand() {
        if (dealer.getHand().isEmpty()) {
            return "Dealer has no cards.";
        }
        return dealer.getHand().toString();
    }
    public String getDealerFaceDownCard() {
        if (dealer.getHand().size() < 2) {
            return "No face-down card available.";
        }
        return "Hidden";  // Placeholder for the face-down card
    }


    public Object calculateHandValue() {
        return user.calculateHandValue();
    }
    public boolean canPlaceBet(int betAmount) {
        return user.canPlaceBet(betAmount);
    }
    public void placeBet(int betAmount) {
        if (betAmount <= 0 || betAmount > user.getBalance() || betAmount % 5 != 0) {
            throw new IllegalArgumentException("Invalid bet amount. It must be a multiple of 5 and less than or equal to your current balance.");
        }

        // Deduct the bet amount from the user's balance
        user.setCurrentBet(betAmount);
        user.setBalance(user.getBalance() - betAmount);
        userRepository.save(user);  // Save the updated balance in the database

        System.out.println("Bet placed: " + betAmount + ". Current balance: " + user.getBalance());
    }
    public int getBalance(){
        return user.getBalance();
    }





}
