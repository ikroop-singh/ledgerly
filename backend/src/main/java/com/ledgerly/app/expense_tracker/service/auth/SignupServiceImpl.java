package com.ledgerly.app.expense_tracker.service.auth;

import com.ledgerly.app.expense_tracker.dao.auth.AuthDao;
import com.ledgerly.app.expense_tracker.entity.User;
import com.ledgerly.app.expense_tracker.service.role.UserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class SignupServiceImpl implements AuthService {

    AuthDao authDao;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    UserRoleService userRoleService;

    SignupServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, AuthDao authDao, UserRoleService userRoleService){
        this.authDao = authDao;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.userRoleService=userRoleService;
    }


    @Override
    public ResponseEntity<String> createUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList(userRoleService.getRoleByName("ROLE_USER")));
            return authDao.createUser(user);
        }
        catch (Exception exception){
            System.out.println(exception);
            return ResponseEntity.status(400).body(exception.getMessage());
        }
    }
}
