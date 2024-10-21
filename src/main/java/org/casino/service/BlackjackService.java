package org.casino.service;
import org.casino.models.*;
import org.casino.models.interfaces.GameSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class BlackjackService {

    private final GameSessionRepository gameSessionRepository;
    private Player player;
    private Dealer dealer;
    private Deck deck;
    private boolean gameOver;

    // Constructor
    public BlackjackService(GameSessionRepository gameSessionRepository) {
        this.gameSessionRepository = gameSessionRepository;
        this.player = new Player(100); // Example initialization
        this.dealer = new Dealer();    // Example initialization
        this.deck = new Deck();        // Example initialization
        this.gameOver = false;
    }

    public String startGame() {
        GameSession session = new GameSession("Player 1", 100, "Started");
        gameSessionRepository.save(session); // Save session to the database
        return "Game started for Player 1 with 100 balance";
    }

    public String getGameStatus(Long gameId) {
        GameSession session = gameSessionRepository.findById(gameId).orElse(null);
        if (session != null) {
            return "Game Status: " + session.getGameStatus();
        } else {
            return "Game not found!";
        }
    }

    public String playerHit() {
    }
}
