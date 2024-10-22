package org.casino;

import lombok.Getter;
import org.casino.config.AppProperties;
import org.casino.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Scanner;

@Getter
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class CasinoApplication {  // Implementing CommandLineRunner to use Spring Beans

//    @Autowired
//    private BlackjackService blackjackService;  // Inject BlackjackService

    public static void main(String[] args) {
        SpringApplication.run(CasinoApplication.class, args);
    }
}

//    @Override
//    public void run(String... args) throws Exception {
//        Scanner scanner = new Scanner(System.in);
//        boolean playAgain = true;
//
//        System.out.println("Welcome to Blackjack!");
//
//        while (playAgain) {
//            System.out.println(blackjackService.startGame());  // Start a new game
//
//            while (!blackjackService.isGameOver()) {
//                System.out.println("\nYour hand: " + blackjackService.getPlayerHand());
//                System.out.println("Dealer's face-up card: " + blackjackService.getDealerFaceUpCard());
//
//                // Prompt player for action (hit/stand)
//                System.out.print("Do you want to 'hit' or 'stand'? ");
//                String action = scanner.nextLine();
//
//                if (action.equalsIgnoreCase("hit")) {
//                    System.out.println(blackjackService.playerHit());
//                } else if (action.equalsIgnoreCase("stand")) {
//                    System.out.println(blackjackService.playerStand());
//                } else {
//                    System.out.println("Invalid action. Please choose 'hit' or 'stand'.");
//                }
//            }
//
//            // After game over, ask the player if they want to play again
//            System.out.print("Do you want to play again? (yes/no): ");
//            String response = scanner.nextLine();
//            playAgain = response.equalsIgnoreCase("yes");
//        }
//
//        System.out.println("Thank you for playing Blackjack!");
//    }
//}
