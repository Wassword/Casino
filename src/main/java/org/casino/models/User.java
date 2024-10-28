package org.casino.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
@Component
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled = true;

    // Track total wins for leaderboard
    @Column(nullable = false, columnDefinition = "integer default 0")
    private int totalWins = 0;

    // Game-specific (player-related) attributes
    @Transient
    private List<Card> hand = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "integer default 100")
    private int balance = 100;  // Player balance (default: 100)

    @Column(nullable = false)
    private int currentBet;  // Player current bet (default: 100)

    // Constructor with username, password, and initial balance
    public User(String username, String password, int balance) {
        this.username = username;
        this.password = password;
        this.enabled = true;
        this.balance = balance;
    }

    // --- Game-Specific Methods ---

    /**
     * Calculates the total points in the player's hand.
     * Aces can be worth 1 or 11, adjusted to avoid busting.
     *
     * @return Total points of the hand.
     */
    public int calculateHandValue() {
        int value = 0;
        int aceCount = 0;

        for (Card card : hand) {
            value += card.getBlackjackValue();
            if (card.isAce()) {
                aceCount++;
            }
        }

        // Adjust Ace values if over 21
        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }

        return value;
    }

    /**
     * Doubles down on the current bet, if the balance allows.
     */
    public void doubleDown() {
        if (balance >= currentBet) {
            balance -= currentBet;
            currentBet *= 2;
        } else {
            throw new IllegalStateException("Insufficient balance to double down.");
        }
    }

    /**
     * Adjusts the player's balance based on whether they won or lost.
     * Resets the current bet for the next game.
     *
     * @param playerWins true if the player won the game, false otherwise.
     */
    public void adjustBalance(boolean playerWins) {
        if (playerWins) {
            balance += 2 * currentBet;  // Profit includes initial bet + winnings
            totalWins++;
        }
        resetBet();
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to add.
     */
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    /**
     * Clears the player's hand for the next round.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Resets the current bet to a default value.
     */
    private void resetBet() {
        currentBet = 100;
    }

    /**
     * Checks if the player is eligible to place a bet based on balance.
     *
     * @param betAmount The amount to bet.
     * @return true if the player can place the bet, false otherwise.
     */
    public boolean canPlaceBet(int betAmount) {
        return betAmount > 0 && betAmount <= balance && betAmount % 5 == 0;
    }

    /**
     * Displays the player's hand as a string (useful for debugging or display).
     *
     * @return A formatted string of the player's hand.
     */
    public String showHand() {
        StringBuilder sb = new StringBuilder("Hand: ");
        for (Card card : hand) {
            sb.append(card.toString()).append(", ");
        }
        return sb.length() > 6 ? sb.substring(0, sb.length() - 2) : "No cards in hand"; // Trim last comma
    }
}
