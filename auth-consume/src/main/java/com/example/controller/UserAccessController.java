package com.example.controller;

import com.example.entity.dto.EntityResponseBody;
import com.example.entity.dto.UserRegisterRequestDTO;
import com.example.security.JWTTokenUtil;
import com.example.security.dto.AuthenticationRequest;
import com.example.security.dto.AuthenticationResponse;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserAccessController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    private JWTTokenUtil jwtTokenUtil;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (Exception e) {
            return EntityResponseBody.generateResponse("Authentication",
                    HttpStatus.UNAUTHORIZED, "Invalid credentials, please check details and try again.");
        }


        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        return EntityResponseBody.generateResponse("Authentication", HttpStatus.OK, new AuthenticationResponse(token, refreshToken));


    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        } catch (Exception e) {
            throw new Exception("INVALID_CREDENTIALS", e.getCause());
        }
    }

    @PostMapping("register")
    public ResponseEntity<Object>register(@RequestBody UserRegisterRequestDTO request){
//        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return EntityResponseBody.generateResponse("Register User",HttpStatus.OK,userService.createUser(request));
    }

    @GetMapping("profile")
    public ResponseEntity<Object>retrieveUserProfile() {
        return EntityResponseBody.generateResponse("User Profile", HttpStatus.OK, userService.findCurrentUser());

    }
}
