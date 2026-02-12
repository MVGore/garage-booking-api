package com.mvgore.garageapi.auth;

import com.mvgore.garageapi.entity.User;
import com.mvgore.garageapi.security.JwtUtil;
import com.mvgore.garageapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
        public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
            if(userService.findByEmail(request.getEmail()).isPresent()){
                return ResponseEntity.badRequest().body(new AuthResponse(null, "Email already in use"));
            }

            try {
                User user = new User();
                user.setName(request.getName());
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(request.getRole());

                userService.saveUser(user);

                // Reload the user to get the generated ID
                User savedUser = userService.findByEmail(user.getEmail()).orElseThrow();

                String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name());
                return ResponseEntity.ok(new AuthResponse(token, savedUser.getRole().name()));
            } catch (Exception e) {
                e.printStackTrace(); // for debugging
                return ResponseEntity.status(500).body(new AuthResponse(null, "Registration failed"));
            }
        }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userService.findByEmail(request.getEmail()).orElse(null);

            if (user == null) {
                return ResponseEntity.status(401).body(new AuthResponse(null, "Invalid credentials"));
            }

            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
            return ResponseEntity.ok(new AuthResponse(token, user.getRole().name()));

        } catch (BadCredentialsException e){
            return ResponseEntity.status(401).body(new AuthResponse(null, "Invalid credentials"));
        }
    }
}
