package mk.ukim.finki.wp.jan2022.g2.service.impl;

import mk.ukim.finki.wp.jan2022.g2.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        mk.ukim.finki.wp.jan2022.g2.model.User user = userRepository.findByUsername(s);
        return new User(
                user.getUsername(),
                user.getPassword(),
                Stream.of(new SimpleGrantedAuthority(user.getRole())).collect(Collectors.toList())
        );
    }
}