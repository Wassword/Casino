package org.casino.models.interfaces;

import org.casino.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD and custom operations on User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findAllUsersByOrderByTotalWinsDesc();


    List<User> findAllByOrderByBalanceDesc();

}
