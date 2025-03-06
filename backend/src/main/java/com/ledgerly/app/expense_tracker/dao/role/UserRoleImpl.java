package com.ledgerly.app.expense_tracker.dao.role;


import com.ledgerly.app.expense_tracker.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRoleImpl implements UserRoleDao{

    EntityManager entityManager;
    @Override
    public Role findRoleByRoleName(String name) {
        TypedQuery<Role>query=entityManager.createQuery("from Role where name=:name",Role.class);
        query.setParameter("name",name);
        return query.getSingleResult();
    }
}
