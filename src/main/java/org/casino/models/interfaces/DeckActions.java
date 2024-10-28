package org.casino.models.interfaces;

import org.casino.models.Card;

import java.util.List;

/**
 * Interface defining core actions for deck and hand management in card games.
 * Suitable for games that involve shuffling, dealing, and determining outcomes.
 */
public interface DeckActions {

    /**
     * Shuffles the deck to randomize the order of the cards.
     */
    void shuffle();

    /**
     * Deals the next card from the deck.
     *
     * @return the next card in the deck.
     * @throws IllegalStateException if the deck is empty.
     */
    Card dealNextCard();

    /**
     * Plays a hand by adding a dealt card to the specified hand.
     *
     * @param hand the hand of cards to add to.
     * @return the updated hand after dealing a new card.
     */
    List<Card> playHand(List<Card> hand);

    /**
     * Determines the winner based on the hand values.
     *
     * @param playerHand the player's hand of cards.
     * @param dealerHand the dealer's hand of cards.
     * @return a string message indicating the winner ("Player", "Dealer", or "Tie").
     */
    String determineWinner(List<Card> playerHand, List<Card> dealerHand);
}
