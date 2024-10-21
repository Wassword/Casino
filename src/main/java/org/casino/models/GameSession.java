package org.casino.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class GameSession {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String playerName;
    private int playerBalance;
    @Getter
    private String gameStatus;

    public GameSession() {}

    public GameSession(String playerName, int playerBalance, String gameStatus) {
        this.playerName = playerName;
        this.playerBalance = playerBalance;
        this.gameStatus = gameStatus;
    }

}
