package com.ledgerly.app.expense_tracker.controller;
import com.ledgerly.app.expense_tracker.entity.User;
import com.ledgerly.app.expense_tracker.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {

    UserService userService;
    @Autowired
    Demo(UserService userService){
        this.userService=userService;
    }
    @GetMapping("/hello")
    public User getHello(){
        return userService.findUserName("test11");
    }
}
