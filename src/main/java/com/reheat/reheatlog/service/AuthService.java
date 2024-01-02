package com.reheat.reheatlog.service;

import com.reheat.reheatlog.crypto.PasswordEncoder;
import com.reheat.reheatlog.domain.User;
import com.reheat.reheatlog.exception.AlreadyExistsEmailException;
import com.reheat.reheatlog.repository.UserRepository;
import com.reheat.reheatlog.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());

        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = encoder.encrypt(signup.getPassword());

        var user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail()).build();
        userRepository.save(user);
    }
}
