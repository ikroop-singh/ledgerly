package com.ledgerly.app.expense_tracker.dao.auth;

import com.ledgerly.app.expense_tracker.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public class AuthDaoImpl implements AuthDao {

    EntityManager entityManager;

    AuthDaoImpl(EntityManager  entityManager){
        this.entityManager=entityManager;
    }

    @Override
    @Transactional
    public ResponseEntity<String> createUser(User user) {
        entityManager.merge(user);
        return  ResponseEntity.status(201).body("User created successfully");
    }
}