package com.herocompany.repositories;

import com.herocompany.entities.CustomerPasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPasswordResetTokenRepository extends JpaRepository<CustomerPasswordResetToken, Long> {
}