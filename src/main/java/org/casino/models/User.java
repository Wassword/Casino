package org.casino.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Set;

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

    // Game-specific (previously Player-related) attributes
    @Transient  // Marking this as transient if it's not intended to be stored in the DB
    private ArrayList<Card> hand = new ArrayList<>();

    @Column
    private int balance;

    @Column
    private int currentBet;

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

    public User() {
        this.hand = new ArrayList<>();
        this.balance = 100;  // Default balance
        this.currentBet = 100;  // Default bet amount
    }

    // --- Game-specific methods ---

    public void placeBet(int betAmount) {
        if (betAmount % 5 != 0 || betAmount > balance || betAmount <= 0) {
            throw new IllegalArgumentException("Invalid bet amount.");
        }
        currentBet = betAmount;
        balance -= currentBet;
        System.out.println("Your bet is now " + currentBet);
        System.out.println("Your current balance is now " + balance);
    }

    public int calculatePoints() {
        int totalPoints = 0;
        int aceCount = 0;

        for (Card card : hand) {
            totalPoints += card.getValue().getValue();  // Assuming Card has a getValue() method
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

    public void doubleDown() {
        if (balance >= currentBet) {
            balance -= currentBet;
            currentBet *= 2;
        } else {
            System.out.println("You don't have enough balance to double down");
        }
    }

    public void adjustBalance(boolean playerWins) {
        if (playerWins) {
            balance += 2 * currentBet;  // Winnings include the initial bet + profit
        }
        currentBet = 10;  // Reset to a default bet for the next round
    }

    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

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

    public void clearHand() {
        this.hand.clear();
    }
}
