package org.casino.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;

@Setter
@Getter
@Entity
@Table(name = "users")
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

    // Game-specific (Player-related) attributes
    @Transient
    private ArrayList<Card> hand = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "integer default 100")
    private int balance;  // Player balance (set default as 100)

    @Column(nullable = false, columnDefinition = "integer default 100")
    private int currentBet;  // Player current bet (set default as 100)

    /**
     * Constructor with username, password, and balance
     */
    public User(String username, String password, int initialPlayerBalance) {
        this.username = username;
        this.password = password;
        this.enabled = true;
        this.balance = initialPlayerBalance;
        this.currentBet = 100;  // Default bet amount
    }

    // Default constructor
    public User() {
        this.hand = new ArrayList<>();
        this.balance = 100;  // Default balance
        this.currentBet = 100;  // Default bet amount
    }

    // --- Game-specific methods ---

    // Place a bet and deduct the amount from balance
    public void placeBet(int betAmount) {
        if (betAmount <= 0 || betAmount > balance || betAmount % 5 != 0) {
            throw new IllegalArgumentException("Invalid bet amount.");
        }
        currentBet = betAmount;
        balance -= currentBet;
        System.out.println("Your bet is now " + currentBet);
        System.out.println("Your current balance is now " + balance);
    }

    // Calculate total points of player's hand
    public int calculatePoints() {
        int totalPoints = 0;
        int aceCount = 0;

        for (Card card : hand) {
            totalPoints += card.getValue().getValue();
            if (card.getValue() == Values.ACE) {
                aceCount++;
            }
        }

        while (totalPoints > 21 && aceCount > 0) {
            totalPoints -= 10;
            aceCount--;
        }

        return totalPoints;
    }

    // Double down the bet
    public void doubleDown() {
        if (balance >= currentBet) {
            balance -= currentBet;
            currentBet *= 2;
        } else {
            System.out.println("You don't have enough balance to double down");
        }
    }

    // Adjust the balance based on game result (win or lose)
    public void adjustBalance(boolean playerWins) {
        if (playerWins) {
            balance += 2 * currentBet;  // Winnings include the initial bet + profit
        }
        currentBet = 100;  // Reset to a default bet for the next round
    }

    // Add a card to player's hand
    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    // Calculate total value of cards in player's hand
    public int calculateHandValue() {
        int value = 0;
        int aceCount = 0;
        for (Card card : hand) {
            value += card.getValue().getValue();
            if (card.getValue() == Values.ACE) {
                aceCount++;
            }
        }

        while (value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }
        return value;
    }

    // Clear the player's hand after each round or game
    public void clearHand() {
        this.hand.clear();
    }
}
