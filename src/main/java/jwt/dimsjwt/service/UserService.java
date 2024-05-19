package jwt.dimsjwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jwt.dimsjwt.DTO.LoginRequest;
import jwt.dimsjwt.model.User;
import jwt.dimsjwt.repository.UserRepository;
import jwt.dimsjwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtility;
    private final AuthenticationManager authenticationManager;
    @Value("${jwt.secret}")
    private String jwtSecret; // Your JWT secret key


    public String loginAndCreateAuthenticationToken(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            // Generate token and return
            return jwtUtility.createToken(authentication);
        } catch (AuthenticationException e) {
            // Handle authentication failure
            throw new RuntimeException("Invalid credentials", e);
        }
    }

    public int getUserRoleByUsername(String username) {
        User user = userRepository.getReferenceByUname(username);
        if(user != null){
            return user.getRole();
        }
        return -1;
    }

    public int getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String roles = claims.get("roles", String.class);
            System.out.println("Roles from token: " + roles);

            if (roles != null && roles.contains("ROLE_USER")) {
                return 0; // Assuming 0 is the role ID for USER
            }
        } catch (Exception e) {
            System.out.println("Error extracting role from token: " + e.getMessage());
        }
        return -1; // Return a value indicating invalid or no role
    }

    public void createUser(User user) {
        userRepository.save(user);
    }
}
