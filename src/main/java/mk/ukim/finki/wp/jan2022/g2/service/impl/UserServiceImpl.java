package mk.ukim.finki.wp.jan2022.g2.service.impl;

import mk.ukim.finki.wp.jan2022.g2.model.User;
import mk.ukim.finki.wp.jan2022.g2.model.exceptions.InvalidUserIdException;
import mk.ukim.finki.wp.jan2022.g2.repository.UserRepository;
import mk.ukim.finki.wp.jan2022.g2.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService
{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(Long id) {
        return this.userRepository.findById(id).orElseThrow(InvalidUserIdException::new);
    }

    @Override
    public List<User> listAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User create(String username, String password, String role) {
        User user = new User(username, passwordEncoder.encode(password),role);
        return this.userRepository.save(user);
    }
}
