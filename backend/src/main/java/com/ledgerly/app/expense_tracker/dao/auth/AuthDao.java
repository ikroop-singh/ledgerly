package com.ledgerly.app.expense_tracker.dao.auth;

import com.ledgerly.app.expense_tracker.entity.User;
import org.springframework.http.ResponseEntity;

public interface AuthDao {
    ResponseEntity<String> createUser(User user);
}
