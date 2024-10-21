package org.casino;

import lombok.Getter;
import org.casino.service.BlackjackService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
@Getter

@SpringBootApplication
public class CasinoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasinoApplication.class, args);

        Scanner scanner = new Scanner(System.in);
        int initialPlayerBalance = 100;  // Set initial balance
        BlackjackService.Game game = new BlackjackService.Game(initialPlayerBalance);

        System.out.println("Welcome to Blackjack!");

        boolean playAgain = true;

        while (playAgain) {
            game.startGame();  // Start a new game

            while (!game.isGameOver()) {
                System.out.println("\nYour hand: " + game.getPlayerHand());
                System.out.println("Dealer's face-up card: " + game.getDealerFaceUpCard());

                // Prompt player for action (hit/stand)
                System.out.print("Do you want to 'hit' or 'stand'? ");
                String action = scanner.nextLine();

                if (action.equalsIgnoreCase("hit")) {
                    System.out.println(game.playerHit());
                } else if (action.equalsIgnoreCase("stand")) {
                    System.out.println(game.playerStand());
                } else {
                    System.out.println("Invalid action. Please choose 'hit' or 'stand'.");
                }
            }

            // After game over, ask the player if they want to play again
            System.out.print("Do you want to play again? (yes/no): ");
            String response = scanner.nextLine();
            playAgain = response.equalsIgnoreCase("yes");
        }

        System.out.println("Thank you for playing Blackjack!");
    }
}
