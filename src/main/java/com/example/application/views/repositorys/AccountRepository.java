package com.example.application.views.repositorys;

import com.example.application.views.ownClass.Account;
import com.example.application.views.ownClass.AppUser;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    //Returns all accounts from a user
    List<Account> findByAppUser(AppUser appUser);
}

