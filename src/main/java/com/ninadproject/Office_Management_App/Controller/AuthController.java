package com.ninadproject.Office_Management_App.Controller;

import com.ninadproject.Office_Management_App.Entity.Employee;
import com.ninadproject.Office_Management_App.Entity.RegisterRequest;
import com.ninadproject.Office_Management_App.Repository.EmployeeRepository;
import com.ninadproject.Office_Management_App.Utils.JwtUtil;
import com.ninadproject.Office_Management_App.dto.AuthRequest;
import com.ninadproject.Office_Management_App.dto.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5174", "http://localhost:5173", "http://localhost:5175"})
public class AuthController {

	@Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    EmployeeRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            log.info("login attempt for user: {} ", request.getUsername());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
            String token = jwtUtil.generateToken(userDetails);

            log.info("Login successful for user: {}", request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));

        }catch (BadCredentialsException ex) {
            log.warn("invalid user credential for user: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid username or password");
        }catch (Exception ex){
            log.error("Unexpected error during login for user: {}", request.getUsername(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("login failed due to server ");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            log.info("Registering user: {}", request.getUsername());

            String usernameRegex = "^(?![0-9])[A-Za-z0-9_]{3,}$";
            if(!request.getUsername().matches(usernameRegex)){
                return ResponseEntity.badRequest().body("Invalid Username : Please Enter Valid UserName" );
            }

            String passwordRegex = "^[\\x21-\\x7E]{8,}$";
            if(!request.getPassword().matches(passwordRegex)){
                return ResponseEntity.badRequest().body("Invalid Password : Please Enter valid password must be at least 8 characters");
            }


            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                log.warn("username '{}' is already taken", request.getUsername());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is already taken");
            }


            Employee user = new Employee();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            userRepository.save(user);

            log.info("User '{}' registered successfully", request.getUsername());
            return ResponseEntity.ok("User registered successfully");

        } catch (Exception ex) {
            log.error("Unexpected error during registration for user: {}", request.getUsername(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed due to server error");
        }
    }


    }