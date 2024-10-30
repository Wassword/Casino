package org.casino.service;

import lombok.*;
import org.casino.config.AppProperties;
import org.casino.models.*;
import org.casino.models.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class BlackjackService {

    @Autowired
    private AppProperties appProperties;
    public void configureGame() {


       }


    private static final Logger logger = LoggerFactory.getLogger(BlackjackService.class);

    private final UserRepository userRepository;
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
        if (user.getCurrentBet() <= 0) {
            throw new IllegalArgumentException("Please place a bet before starting the game.");
        }
        logger.info("Starting new game for user: {}", user.getUsername());
        resetGame();
        dealInitialCards();
        int playerPoints = user.calculateHandValue();
        int dealerPoints = dealer.calculateHandValue();

        if (playerPoints == 21){
            gameOver = true;
            return handleGameOutcome();
        } else if (dealerPoints == 21){
            gameOver = true;
            return handleGameOutcome();
        } else {
            return String.format("Game started for %s with a balance of %d", user.getUsername(), user.getBalance());
        }
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
        logger.info("Player hit. Current hand: {} (Total Points: {})", getPlayerHand(), points);

        if (points > 21) {
            gameOver = true;
            return handleBust();
        }

        return String.format("Your hand: %s (Total Points: %d)", getPlayerHand(), points);
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
     * Retrieves the player's current hand as a list of image paths.
     * @return a list of strings representing the paths to the player's card images.
     */
    public List<String> getPlayerHand() {
        if (user.getHand().isEmpty()) {
            throw new NoSuchElementException("Player's hand is empty");
        }
        return user.getHand().stream().map(Card::getImagePath).collect(Collectors.toList());
    }
    public String getPlayerHandS() {
        if (user.getHand().isEmpty()) {
            throw new NoSuchElementException("Player's hand is empty");
        }
        return user.getHand().toString().strip().trim();
    }

    /**
     * Retrieves the dealer's face-up card (first card dealt).
     * @return a string representing the dealer's face-up card image path.
     */
    public String getDealerFaceUpCard() {
        if (dealer.getHand().isEmpty()) {
            throw new NoSuchElementException("Dealer has no cards to show");
        }
        return dealer.getHand().getFirst().getImagePath();  // Get image path for the first card
    }

    // --- Private Methods for Better Code Structure ---

    private void resetGame() {
        user.clearHand();
        dealer.clearHand();
        deck.resetDeck();
        gameOver = false;
        logger.info("Game reset and deck shuffled.");
    }

    private void dealInitialCards() {
        user.addCardToHand(deck.dealCard());
        user.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());
        dealer.addCardToHand(deck.dealCard());




        logger.info("Initial cards dealt. Player hand: {}, Dealer face-up card: {}", getPlayerHandS(), getDealerFaceUpCard());
    }

    private void playDealerTurn() {
        while (dealer.shouldHit()) {
            dealer.addCardToHand(deck.dealCard());
        }
        logger.info("Dealer turn complete. Final hand: {}", dealer.showHand());
    }

    private String handleBust() {
        logger.info("Player busted with hand: {}", getPlayerHand());
        return String.format("You busted! Final hand: %s", getPlayerHandS());
    }

    private String handleGameOutcome() {
        int playerPoints = user.calculateHandValue();
        int dealerPoints = dealer.calculateHandValue();

        logger.info("Player stood with hand: {}, Dealer finished with hand: {}", getPlayerHand(), dealer.showHand());

        if (dealerPoints > 21) {
            return handlePlayerWin("Dealer busted!");
        }else if(playerPoints == 21){
            return handlePlayerWin("BLACKJACK!");
        } else if (dealerPoints == 21) {
            return handleDealerWin();
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
        return String.format("%s Your hand: %s (Total Points: %d)", message, getPlayerHandS(), user.calculateHandValue());
    }

    private String handleDealerWin() {
        logger.info("Dealer won the game. Dealer hand: {}", dealer.showHand());
        
        return String.format("Dealer wins. Dealer hand: %s (Total Points: %d)", dealer.showHand(), dealer.calculateHandValue());
    }

    public List<String> getDealerHand() {
        return dealer.getHand().stream().map(Card::getImagePath).collect(Collectors.toList());
    }

    public boolean canPlaceBet(int betAmount) {
        return user.canPlaceBet(betAmount);
    }

    public void placeBet(int betAmount) {
        if (betAmount <= 0 || betAmount > user.getBalance() || betAmount % 5 != 0) {
            throw new IllegalArgumentException("Invalid bet amount. It must be a multiple of 5 and less than or equal to your current balance.");
        }

        user.setCurrentBet(betAmount);
        user.setBalance(user.getBalance() - betAmount);
        userRepository.save(user);
        logger.info("Bet placed: {}. Current balance: {}", betAmount, user.getBalance());
    }

    public int getBalance() {
        return user.getBalance();
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
    public String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    public String playerDoubleDown() {
        if (gameOver) {
            return "Game is already over.";
        }

        // Check if the user has enough balances to double down
        if (!canDoubleDown()) {
            return "Insufficient balance to double down.";
        }

        // Deduct double down amount and double the current bet
        user.doubleDown();
        user.addCardToHand(deck.dealCard());

        int points = user.calculateHandValue();
        String statusMessage = String.format("You doubled down. Your hand: %s (Total Points: %d)", getPlayerHandS(), points);

        // If the player busts after doubling down
        if (points > 21) {
            gameOver = true;
            return handleBust();
        }

        // Otherwise, end the player’s turn and move to dealer's turn
        gameOver = false;
        return statusMessage + "\n" + playerStand();
    }
    private boolean canDoubleDown() {
        return user.getBalance() >= user.getCurrentBet();
    }

}
