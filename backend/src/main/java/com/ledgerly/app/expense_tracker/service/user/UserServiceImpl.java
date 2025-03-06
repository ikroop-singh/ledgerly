package com.ledgerly.app.expense_tracker.service.user;

import com.ledgerly.app.expense_tracker.dao.user.UserDao;
import com.ledgerly.app.expense_tracker.entity.Role;
import com.ledgerly.app.expense_tracker.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserDao userDao;


    @Autowired
    UserServiceImpl(UserDao userDao){
        this.userDao=userDao;
    }

    @Override
    public User findUserName(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("userDetails");
        User user=findUserName(username);
        if(user==null)
                throw new UsernameNotFoundException("user not found");

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }


}
