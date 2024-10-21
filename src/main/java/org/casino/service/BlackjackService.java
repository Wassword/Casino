package org.casino.service;
import org.casino.models.*;
import org.casino.models.interfaces.DeckActions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Scanner;
@Service
public class BlackjackService {
    private final GameSessionRepository gameSessionRepository;

    public BlackjackService(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
    }

    private Player player;
    private Dealer dealer;
    private Deck deck;
    private static boolean gameOver;

    public String startGame() {
        // Assume game logic is implemented here
        GameSession session = new GameSession("Player 1", 100, "Started");
        gameSessionRepository.save(session);  // Save session to the database

        return "Game started for Player 1 with 100 balance";
    }

    public String getGameStatus(Long gameId) {
        // Fetch the game session by its ID
        GameSession session = gameSessionRepository.findById(gameId).orElse(null);

        if (session != null) {
            return "Game Status: " + session.getGameStatus();
        } else {
            return "Game not found!";
        }
    }



    public String playerHit() {
        if (!gameOver) {
            player.addCardToHand(deck.dealCard());
            if (player.calculatePoints() > 21) {
                gameOver = true;
                return "You busted! Final hand: " + player.getHand();
            }
        }
        return "Your hand: " + player.getHand();
    }

    public String playerStand() {
        if (!gameOver) {
            while (dealer.calculatePoints() < 17) {
                dealer.addCardToHand(deck.dealCard());
            }
            gameOver = true;
            return getFinalResults();
        }
        return "The game is over.";
    }

    private String getFinalResults() {
        if (dealer.calculatePoints() > 21) {
            return "Dealer busted! You win. Dealer hand: " + dealer.getHand();
        } else if (player.calculatePoints() > dealer.calculatePoints()) {
            return "You win! Your hand: " + player.getHand() + " Dealer hand: " + dealer.getHand();
        } else if (player.calculatePoints() < dealer.calculatePoints()) {
            return "Dealer wins. Your hand: " + player.getHand() + " Dealer hand: " + dealer.getHand();
        } else {
            return "It's a tie!";
        }
    }

    public String getGameStatus() {
        return "Player hand: " + player.getHand() + ", Dealer hand: " + dealer.getFaceUpCard();
    }

    public static class Game implements DeckActions {
        private Deck deck;
        private Player player;
        private Dealer dealer;

        // Constructor
        public Game(int initialPlayerBalance) {
            Scanner sc = new Scanner(System.in);  // Create a Scanner object
            deck = new Deck();
            deck.shuffle();
            player = new Player(initialPlayerBalance);
            dealer = new Dealer();
        }

        @Override
        public void shuffle() {

        }

        @Override
        public void dealNextCard() {

        }

        @Override
        public void playHand(ArrayList<Card> hand) {
            boolean handFinished = false;
            Scanner scanner = new Scanner(System.in);

            while (!handFinished) {
                System.out.print(player.getName() + ", do you want to hit, stand, or double down? (h/s/d): ");
                String action = scanner.nextLine();

                switch (action.toLowerCase()) {
                    case "h": // Hit: Add another card to the player's hand
                        hand.add(deck.dealCard());
                        System.out.println("Your hand: " + hand);
                        if (player.calculateHandValue() > 21) {
                            System.out.println("Bust! Your hand value is " + player.calculateHandValue());
                            handFinished = true;
                        }
                        break;
                    case "s": // Stand: End the player's turn
                        handFinished = true;
                        break;
                    case "d": // Double down: Double the bet and add one more card
                        player.doubleDown();
                        hand.add(deck.dealCard());
                        System.out.println("Your hand: " + hand);
                        handFinished = true; // After double down, no more actions allowed
                        break;
                    default:
                        System.out.println("Invalid action. Please choose 'h', 's', or 'd'.");
                }
            }
            // After the player's turn, handle the dealer's turn and determine the winner
            determineWinner();
        }

        public String determineWinner() {
            dealer.playTurn(deck);
            int playerValue = player.calculateHandValue();
            int dealerValue = dealer.calculateHandValue();

            System.out.println("Dealer's hand value: " + dealerValue);

            if (playerValue > 21) {
                System.out.println(player.getName() + " busts! Dealer wins.");
                player.adjustBalance(false);
            } else if (dealerValue > 21) {
                System.out.println("Dealer busts! " + player.getName() + " wins.");
                player.adjustBalance(true);
            } else if (playerValue > dealerValue) {
                System.out.println(player.getName() + " wins!");
                player.adjustBalance(true);
            } else if (dealerValue > playerValue) {
                System.out.println("Dealer wins!");
                player.adjustBalance(false);
            } else {
                System.out.println("It's a tie! No money won or lost.");
                // In a tie, typically the bet is returned, so no change in balance.
            }
            return null;
        }

        public void startGame() {
            boolean playing = true;
            Scanner scanner = new Scanner(System.in);

            while (playing && player.getBalance() > 0) {
                player.clearHand();
                dealer.clearHand();

                if (player.getBalance() < player.getCurrentBet()) {
                    System.out.println("You don't have enough money to place the minimum bet! Game over!");
                    break;
                }

                player.placeBet(); // Place the default bet at the start of the game
                dealInitialCards();

                // Show the player's hand and dealer's visible card
                System.out.println("Your hand: " + player.getHand());
                System.out.println("Dealer's Visible Card: " + dealer.getHand().get(0));

                // Player turn
                playHand(player.getHand());
                // Dealer's turn takes place in determineWinner automatically

                // Show the player's balance after the round
                System.out.println("Your current balance: " + player.getBalance());

                // Check if the player wants to continue playing
                System.out.print("Do you want to play again? (yes/no): ");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("no")) {
                    playing = false;
                }
            }

            // Display final balance and game over message
            if (player.getBalance() <= 0) {
                System.out.println("You don't have enough money to play again! Game over!");
            }
            System.out.println("Thanks for playing! Your final balance is: " + player.getBalance());
        }

        private void dealInitialCards() {
            player.addCardToHand(deck.dealCard());
            player.addCardToHand(deck.dealCard());
            dealer.addCardToHand(deck.dealCard());
            dealer.addCardToHand(deck.dealCard());
        }

        public boolean isGameOver() {
            return gameOver;
        }

        public String getPlayerHand() {
            return player.getHand().toString();
        }

        public String getDealerFaceUpCard() {
            return dealer.getHand().get(0).toString();
        }

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

        public String playerStand() {
            if (!gameOver) {
                while (dealer.calculateHandValue() < 17) {
                    dealer.addCardToHand(deck.dealCard());
                }
                gameOver = true;
                return determineWinner();  // Call a method to determine the winner
            }
            return "The game is already over.";
        }
        }
    }


