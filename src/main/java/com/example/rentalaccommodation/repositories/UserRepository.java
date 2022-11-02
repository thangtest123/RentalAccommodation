package com.example.rentalaccommodation.repositories;

import com.example.rentalaccommodation.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUsername(String username);

    User findUserById(Long id);
}
