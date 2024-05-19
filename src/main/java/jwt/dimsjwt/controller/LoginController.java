package jwt.dimsjwt.controller;

import jwt.dimsjwt.DTO.LoginRequest;
import jwt.dimsjwt.repository.UserRepository;
import jwt.dimsjwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    public LoginController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.loginAndCreateAuthenticationToken(loginRequest);
            int role = userService.getUserRoleByUsername(loginRequest.getUsername());

            // Return token in response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } catch (AuthenticationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/admin/resource")
    @ResponseBody
    public ResponseEntity<String> adminResource(@RequestHeader("Authorization") String token) {
        // Check if the token is present in the header
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }

        // Extract token value
        String authToken = token.substring(7);

        // Validate token and retrieve role
        int role = userService.getRoleFromToken(authToken);

        // Check if the user has the ADMIN role
        if (role != 1) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        // Authorized access
        return ResponseEntity.ok("Admin resource accessed successfully");
    }

    @GetMapping("/user/resource")
    @ResponseBody
    public ResponseEntity<String> userResource(@RequestHeader("Authorization") String token) {
        System.out.println("Received token: " + token);

        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println("Token is missing or does not start with Bearer");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
        }

        String authToken = token.substring(7);
        System.out.println("Extracted auth token: " + authToken);

        int role = userService.getRoleFromToken(authToken);
        System.out.println("Extracted role: " + role);

        if (role != 0) { // Assuming 0 is the role ID for USER
            System.out.println("Role is not USER. Access denied.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        System.out.println("Access granted to user resource.");
        return ResponseEntity.ok("User resource accessed successfully");
    }
}
