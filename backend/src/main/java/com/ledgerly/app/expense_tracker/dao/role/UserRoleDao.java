package com.ledgerly.app.expense_tracker.dao.role;

import com.ledgerly.app.expense_tracker.entity.Role;

public interface UserRoleDao {

    Role findRoleByRoleName(String name);
}
