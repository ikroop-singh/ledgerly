package com.ledgerly.app.expense_tracker.service.user;

import com.ledgerly.app.expense_tracker.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findUserName(String username);
}
