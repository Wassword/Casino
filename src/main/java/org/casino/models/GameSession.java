package org.casino.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;
    private int playerBalance;
    private String gameStatus;

    // Default constructor (required by JPA)
    public GameSession() {}

    // Parameterized constructor for easier initialization
    public GameSession(String playerName, int playerBalance, String gameStatus) {
        this.playerName = playerName;
        this.playerBalance = playerBalance;
        this.gameStatus = gameStatus;
    }
}
