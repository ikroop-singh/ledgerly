package com.ledgerly.app.expense_tracker.service.auth;

import com.ledgerly.app.expense_tracker.entity.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<String> createUser(User user);
}
