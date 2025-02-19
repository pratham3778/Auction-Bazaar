package com.ceeras.auctionBazar.repository;

import com.ceeras.auctionBazar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// The UserRepository is an interface that allows us to interact with the database without writing SQL queries manually. 


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(User.Role role);

    void deleteByEmail(String email);
}
