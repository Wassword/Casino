package org.casino.models.interfaces;

import org.casino.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD and custom operations on User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username to search for.
     * @return an Optional containing the user if found, or empty if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Retrieves the top 10 users ordered by total wins in descending order.
     * Used to display the leaderboard.
     *
     * @return a list of the top 10 users by total wins.
     */
    List<User> findTop10ByOrderByTotalWinsDesc();

    /**
     * Retrieves all enabled (active) users.
     *
     * @return a list of active users.
     */
    List<User> findByEnabledTrue();

    /**
     * Finds users with a balance greater than a specified amount.
     *
     * @param minBalance the minimum balance to filter users.
     * @return a list of users with a balance above the specified amount.
     */
    @Query("SELECT u FROM User u WHERE u.balance > :minBalance")
    List<User> findUsersWithBalanceAbove(@Param("minBalance") int minBalance);

    /**
     * Finds users by status (enabled or disabled).
     *
     * @param enabled the status to filter users by.
     * @return a list of users matching the specified status.
     */
    List<User> findByEnabled(Boolean enabled);

    /**
     * Retrieves the top N users ordered by total wins in descending order.
     * Useful for displaying a flexible leaderboard.
     *
     * @param limit the maximum number of users to retrieve.
     * @return a list of the top users by total wins.
     */
    @Query("SELECT u FROM User u ORDER BY u.totalWins DESC")
    List<User> findTopUsersByTotalWins(@Param("limit") int limit);
}
