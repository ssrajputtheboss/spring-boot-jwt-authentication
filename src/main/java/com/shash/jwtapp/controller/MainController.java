package com.shash.jwtapp.controller;

import com.shash.jwtapp.model.AuthenticationRequest;
import com.shash.jwtapp.model.AuthenticationResponse;
import com.shash.jwtapp.service.JwtService;
import com.shash.jwtapp.service.MyUserDetailService;
import com.shash.jwtapp.utils.Verifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    AuthenticationManager manager;
    
    @Autowired
    MyUserDetailService userDetailsService;

    @Autowired
    JwtService jwtService;

    @GetMapping("/hello")
    public String hello(){return "Hello";}

    @GetMapping("/authenticate")
    public ResponseEntity<?> authorize(@RequestBody AuthenticationRequest request){
        boolean isAuthenticated;
        try{
            manager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername() ,
                request.getPassword()
            ));
            isAuthenticated = true;
        }catch(BadCredentialsException e){
            isAuthenticated = false;
            System.out.println("bad credentials");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = "None";
        if(userDetails != null && isAuthenticated){
            jwt = jwtService.generateToken(userDetails);
        }
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest user){
        System.out.println(user.toString());
        if(Verifier.email(user.getUsername()) && Verifier.pass(user.getPassword())){
            //userDetailsService.save();
            return ResponseEntity.ok("User Added");
        }else{
            System.out.println(user.getUsername()+","+user.getPassword());
            return ResponseEntity.ok("Invalid Credentials");
        }
    }
}
