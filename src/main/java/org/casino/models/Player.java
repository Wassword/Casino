package org.casino.models;
import lombok.Getter;
import java.util.ArrayList;
import java.util.Scanner;


@Getter
public class Player {
    private String name;
    private ArrayList<Card> hand;
    private int balance;
    private int currentBet;


    /**
     * Create a new Person
     */
    public Player(int initialPlayerBalance) {
        Scanner sc = new Scanner(System.in);// Pass scanner as parameter
        System.out.print("Enter your name: ");
        this.name = sc.nextLine();
        this.hand = new ArrayList<>();
        this.balance = initialPlayerBalance;
        this.currentBet = 100; // Default bet amount
    }



    public String getName() {
        return name;
    }
    public ArrayList<Card> getHand() {
        return hand;
    }

    public int getBalance() {
        return balance;
    }
    public int getCurrentBet(){
        return currentBet;
    }
    public void placeBet() {
        Scanner sc = new Scanner(System.in);
        int betAmount;
        do {
            System.out.print("Enter your bet (in increments of 5 and up to your current balance of " + balance + "): ");
            betAmount = sc.nextInt();
        } while (betAmount % 5 != 0 || betAmount > balance || betAmount <= 0);

        currentBet =betAmount;
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



    public void doubleDown(){
        if (balance >= currentBet) {
            balance -= currentBet;
            currentBet *= 2;
        } else {
            System.out.println("You don't have enough balance to double down");
        }
    }
    public void adjustBalance(boolean playerWins) {
        if (playerWins) {
            balance += 2 * currentBet; // Winnings include the initial bet + profit
        }
        currentBet = 10; // Reset to a default bet for the next round
        //        // No need to deduct a bet again as it was already deducted in placeBet()
    }
    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    public int calculateHandValue(){
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