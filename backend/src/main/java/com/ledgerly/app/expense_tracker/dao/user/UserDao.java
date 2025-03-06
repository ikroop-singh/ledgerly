package com.ledgerly.app.expense_tracker.dao.user;

import com.ledgerly.app.expense_tracker.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserDao {
    User findUserByUsername(String name);
}
