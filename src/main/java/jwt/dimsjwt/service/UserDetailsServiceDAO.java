package jwt.dimsjwt.service;

import jwt.dimsjwt.model.User;
import jwt.dimsjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceDAO implements UserDetailsService {
    private final UserRepository ur;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = ur.getReferenceByUname(username);
        String role = (user.getRole() == 1) ? "ADMIN" : "USER"; // Convert integer role to string representation
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(role)
                .accountLocked(user.isLocked())
                .disabled(false)
                .credentialsExpired(false)
                .build();
    }

}
