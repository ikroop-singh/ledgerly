package com.ledgerly.app.expense_tracker.controller;


import com.ledgerly.app.expense_tracker.entity.User;
import com.ledgerly.app.expense_tracker.service.auth.AuthService;
import com.ledgerly.app.expense_tracker.service.user.UserService;
import com.ledgerly.app.expense_tracker.util.JwtUtil;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class Auth {

    UserService userService;
    AuthService authService;
    AuthenticationManager authenticationManager;
    JwtUtil jwtUtill;

    Auth(UserService userService, AuthService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.userService=userService;
        this.authService = authService;
        this.jwtUtill=jwtUtil;
        this.authenticationManager=authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody User user){

        User userExists=userService.findUserName(user.getUsername());
        if(userExists!=null){
            return ResponseEntity.status(400).body("username already in use. Try different user");
        }
        else{
            return authService.createUser(user);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody User user, HttpServletResponse response){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
            UserDetails userDetails=userService.loadUserByUsername(user.getUsername());
            String jwt=jwtUtill.generateToken(userDetails.getUsername());
            Cookie cookie=new Cookie("authToken",jwt);
            cookie.setHttpOnly(true); //This setting makes cookie accessible by server only not Javascript
            cookie.setSecure(true);  // Allows https only
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24);        // 1 day

            response.addCookie(cookie);
            return  ResponseEntity.status(200).body(jwt);
        }catch (Exception exception){
            log.error("Exception occurred "+exception);
            return ResponseEntity.status(400).body("Bad credentials");
        }
    }

    @PostMapping("/logout")
    ResponseEntity<String> logout(HttpServletResponse response){
        Cookie cookie=new Cookie("authToken",null);
        cookie.setHttpOnly(true); //This setting makes cookie accessible by server only not Javascript
        cookie.setSecure(true);  // Allows https only
        cookie.setPath("/");
        cookie.setMaxAge(0);        // 1 day
        response.addCookie(cookie);
        return ResponseEntity.status(200).body("logged out");
    }

}
