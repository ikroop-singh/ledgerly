package com.ledgerly.app.expense_tracker.dao.user;

import com.ledgerly.app.expense_tracker.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao{

    EntityManager entityManager;
    UserDaoImpl(EntityManager  entityManager){
        this.entityManager=entityManager;
    }

    @Override
    public User findUserByUsername(String name) {

        TypedQuery<User> query=entityManager.createQuery("from User where username=:name",User.class);
        query.setParameter("name",name);
        User theUser = null;
        try {
            theUser = query.getSingleResult();
            System.out.println(theUser);
        } catch (Exception e) {
            theUser = null;
        }

        return theUser;
    }


}
