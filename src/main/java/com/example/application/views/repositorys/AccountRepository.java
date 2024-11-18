package com.example.application.views.repositorys;

import com.example.application.views.ownClass.Account;
import com.example.application.views.ownClass.AppUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByAppUser(AppUser appUser);
    Optional<Account> findById(Long id);
}
