package com.ledgerly.app.expense_tracker.service.role;

import com.ledgerly.app.expense_tracker.dao.role.UserRoleDao;
import com.ledgerly.app.expense_tracker.entity.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class UserRoleServiceImpl implements UserRoleService {

    UserRoleDao userRoleDao;

    @Override
    public Role getRoleByName(String name) {
        return userRoleDao.findRoleByRoleName(name);
    }
}
